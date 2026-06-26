package net.lee.bettertips;

import fi.dy.masa.malilib.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.lee.bettertips.config.BetterTipsConfig;
import net.xiaoyu233.fml.ModResourceManager;

public class MITEBetterTips implements ClientModInitializer {
    public void onInitializeClient() {
        BetterTipsConfig.getInstance().load();
        ConfigManager.getInstance().registerConfig(BetterTipsConfig.getInstance());
        BetterTipsEvents.register();
        ModResourceManager.addResourcePackDomain("bettertips");
    }
}
