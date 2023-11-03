package com.github.cygnusx.cygsskyblockmod.GUIs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InventoryViewerContainer extends Container {
    private IInventory containerInventory;
    public InventoryViewerContainer(IInventory containerInventory) {
        this.containerInventory = containerInventory;
        containerInventory.openInventory(null);

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = col + row * 9;
                this.addSlotToContainer(new Slot(this.containerInventory, slotIndex, 8 + col * 18, 17 + row * 18));
            }
        }
    }

    @Override
    public boolean canDragIntoSlot(Slot slot) {
        return false;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.containerInventory.closeInventory(player);
    }

    @Override
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public void detectAndSendChanges() {
        assert true;
    }

    @Override
    public void putStacksInSlots(ItemStack[] p_75131_1_) {
        assert true;
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        assert true;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return false;
    }

}
