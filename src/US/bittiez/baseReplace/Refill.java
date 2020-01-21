package US.bittiez.baseReplace;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Refill {
    final Inventory inventory;
    final int hotBarSlot;
    final ItemStack itemClone;
    final int minRating;

    public Refill(Inventory playerInventory, int hotBarSlot, ItemStack originalItem) {
        this(playerInventory, hotBarSlot, originalItem, 2);
    }

    public Refill(Inventory playerInventory, int hotBarSlot, ItemStack originalItem, int minRating){
        this.inventory = playerInventory;
        this.hotBarSlot = hotBarSlot;
        this.itemClone = originalItem;
        this.minRating = minRating;
    }

    public void run() {
        if(inventory.getItem(hotBarSlot) != null)
            return;
        ItemStack bestItem = null;
        int bestPoints = 0;
        int bestItemSlot = -1;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack invItem = inventory.getItem(i);
            if(invItem == null || invItem.getType().equals(Material.AIR)) //Skip if item is null or air in slot
                continue;
            int points = getRating(itemClone, invItem);
            if(points > bestPoints){
                bestItem = invItem;
                bestPoints = points;
                bestItemSlot = i;
            }
        }
        if(bestItem != null && bestItemSlot >= 0 && bestPoints >= minRating){
            //Points 0 = no match,
            //1 = same type(red/blue wool will both have 1 point for example)
            //2 = same type & data, red wool will be the only match for red wool.
            //3 = same type, data, and is a full stack
            inventory.setItem(hotBarSlot, bestItem);
            inventory.clear(bestItemSlot);
        }
    }

    private int getRating(ItemStack original, ItemStack second){
        int rating = 0;
        if(original.getType().equals(second.getType())) {
            rating++;
            if (original.getData().equals(second.getData())) {
                rating++;
                if (second.getAmount() == second.getMaxStackSize())
                    rating++;
            }
        }
        return rating;
    }

}
