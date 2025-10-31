package com.bleudev.ppl_utils.neoforge;

import com.bleudev.ppl_utils.Ppl_utils;
import net.neoforged.fml.common.Mod;

@Mod(Ppl_utils.MOD_ID)
public final class Ppl_utilsNeoForge {
    public Ppl_utilsNeoForge() {
        // Run our common setup.
        Ppl_utils.init();
    }
}
