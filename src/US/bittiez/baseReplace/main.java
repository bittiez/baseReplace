package US.bittiez.baseReplace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class main extends JavaPlugin implements Listener {
    private static Logger log;

    @Override
    public void onEnable() {
        log = getLogger();
        getServer().getPluginManager().registerEvents(this, this);
    }
    public boolean onCommand(CommandSender who, Command cmd, String label, String[] args) {
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) //They right clicked a block
            return;
        if(e.isAsynchronous() || e.getItem() == null || e.getItem().getType().isEdible()) //Item isn't null, or edible.
            return;

        final int hotBarSlot = getItemSlot(e.getPlayer(), e.getHand());
        if(hotBarSlot == -1 || hotBarSlot > 8) //Make sure their active selection is in their hand or offhand
            return;

        final ItemStack itemClone = e.getItem().clone();
        Refill refill = new Refill(e.getPlayer().getInventory(), hotBarSlot, itemClone);
        Bukkit.getScheduler().runTaskLater(this, refill::run, 1l);
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e){
        if(e.isAsynchronous() || e.isCancelled() || e.getItem().getAmount() > 1)
            return;
        final int hotBarSlot = e.getPlayer().getInventory().getHeldItemSlot();
        if(hotBarSlot < 0 || hotBarSlot > 8)
            return;
        Refill refill = new Refill(e.getPlayer().getInventory(), hotBarSlot, e.getItem().clone(), 1);
        Bukkit.getScheduler().runTaskLater(this, refill::run, 1l);
    }

    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent e){
        if(e.isAsynchronous())
            return;
        final int hotBarSlot = e.getPlayer().getInventory().getHeldItemSlot();
        if(hotBarSlot < 0 || hotBarSlot > 8)
            return;
        Refill refill = new Refill(e.getPlayer().getInventory(), hotBarSlot, e.getBrokenItem().clone(), 1);
        Bukkit.getScheduler().runTaskLater(this, refill::run, 1l);
    }

    private int getItemSlot(Player player, EquipmentSlot hand){
        if(!hand.equals(EquipmentSlot.HAND) && !hand.equals(EquipmentSlot.OFF_HAND))
            return -1;
        return player.getInventory().getHeldItemSlot();
    }
}
