package net.fabricmc.accessories;

import net.minecraft.src.*;
public class ContainerAccessories extends Container {

    private final EntityPlayer thePlayer;
    private final IInventory accessoryInv; // your custom accessory inventory

    // how many accessory slots you want
    private static final int ACCESSORY_COUNT = 6;

    public ContainerAccessories(InventoryPlayer playerInv, IInventory accessoryInv, EntityPlayer player) {
        this.thePlayer = player;
        this.accessoryInv = accessoryInv;

        // ---- 1. Add your accessory slots (0 → ACCESSORY_COUNT-1) ----
        for (int i = 0; i < ACCESSORY_COUNT; i++) {
            // Position them wherever in GUI
            this.addSlotToContainer(new SlotAccessory(accessoryInv, i, 80, 8 + i * 18));
        }

        // ---- 2. Add player inventory (same as ContainerPlayer, MUST match) ----

        // Player main inventory (3 rows × 9 columns)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlotToContainer(new Slot(
                        playerInv,
                        col + (row + 1) * 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlotToContainer(new Slot(
                    playerInv,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    // ---- 3. Item shift-clicking support ----
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack original = null;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            original = stack.copy();

            // slot ranges:
            // 0 → ACCESSORY_COUNT-1: accessory slots
            // ACCESSORY_COUNT → end: player inventory

            if (index < ACCESSORY_COUNT) {
                // from accessory → to player inventory
                if (!this.mergeItemStack(stack,
                        ACCESSORY_COUNT,
                        this.inventorySlots.size(),
                        true)) {
                    return null;
                }
            } else {
                // from player inventory → accessory slots
                if (!this.mergeItemStack(stack, 0, ACCESSORY_COUNT, false)) {
                    return null;
                }
            }

            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.stackSize == original.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, stack);
        }

        return original;
    }

    // Prevent putting items into invalid slots
    @Override
    public boolean func_94530_a(ItemStack stack, Slot slot) {
        return super.func_94530_a(stack, slot);
    }
}
