package com.bleudev.ppl_utils.fabric;

import com.bleudev.ppl_utils.Ppl_utils;
import net.fabricmc.api.ModInitializer;

public final class Ppl_utilsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Ppl_utils.init();
    }
}
