package com.bleudev.ppl_utils.feature.cache;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import static com.bleudev.ppl_utils.util.UIConstants.TOTAL_INVENTORY_SLOTS;

/**
 * Caches inventory slot count to avoid recalculating every frame.
 * Updates only when inventory actually changes.
 */
public class InventorySlotCache {
    private static int cachedOccupiedSlots = 0;
    private static long lastInventoryHash = 0;
    
    /**
     * Gets the number of occupied slots, using cache if inventory hasn't changed.
     * 
     * @param client The Minecraft client
     * @return Number of occupied slots
     */
    public static int getOccupiedSlots(@NotNull MinecraftClient client) {
        if (client.player == null) {
            cachedOccupiedSlots = 0;
            return 0;
        }
        
        PlayerInventory inventory = client.player.getInventory();
        long currentHash = calculateInventoryHash(inventory);
        
        if (currentHash != lastInventoryHash) {
            cachedOccupiedSlots = countOccupiedSlots(inventory);
            lastInventoryHash = currentHash;
        }
        
        return cachedOccupiedSlots;
    }
    
    /**
     * Calculates a hash of the inventory state for change detection.
     * Uses a simple hash based on item presence.
     * 
     * @param inventory The player inventory
     * @return Hash value representing inventory state
     */
    private static long calculateInventoryHash(@NotNull PlayerInventory inventory) {
        long hash = 0;
        for (int i = 0; i < TOTAL_INVENTORY_SLOTS; i++) {
            var stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                hash = hash * 31 + stack.hashCode();
            }
        }
        return hash;
    }
    
    /**
     * Counts the number of occupied slots in the inventory.
     * 
     * @param inventory The player inventory
     * @return Number of occupied slots
     */
    private static int countOccupiedSlots(@NotNull PlayerInventory inventory) {
        int count = 0;
        for (int i = 0; i < TOTAL_INVENTORY_SLOTS; i++) {
            if (!inventory.getStack(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Invalidates the cache. Should be called when inventory is known to have changed.
     */
    public static void invalidate() {
        lastInventoryHash = 0;
        cachedOccupiedSlots = 0;
    }
}

