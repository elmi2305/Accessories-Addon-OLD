package net.fabricmc.accessories;

import net.fabricmc.accessories.items.AccessoryItem;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotAccessory extends Slot {
    public SlotAccessory(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    /** Only ever allow a single item in this slot */
    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    /** Make sure if someone shift-clicks in a bigger stack, only one goes in */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() instanceof AccessoryItem;
    }

    /** Clamp incoming stacks to size 1 */
    @Override
    public void putStack(ItemStack stack) {
        if (stack != null && stack.stackSize > 1) {
            stack = stack.copy();
            stack.stackSize = 1;
        }
        super.putStack(stack);
        onSlotChanged();  // still fire your name-print if you want
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        ItemStack stack = this.getStack();
        if (stack != null) {
            System.out.println("Placed in accessory slot: " + stack.getDisplayName());
        }
    }
}
