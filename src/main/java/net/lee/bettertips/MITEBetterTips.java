package net.lee.bettertips;

import fi.dy.masa.malilib.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.lee.bettertips.config.BetterTipsConfig;

public class MITEBetterTips implements ModInitializer {
    public void onInitialize() {
        BetterTipsConfig.getInstance().load();
        ConfigManager.getInstance().registerConfig(BetterTipsConfig.getInstance());
    }
}
