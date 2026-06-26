package net.lee.bettertips.config;

import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import java.util.ArrayList;
import java.util.List;

public class BetterTipsConfig extends SimpleConfigs {
    private static final BetterTipsConfig Instance;
    public static final ConfigBoolean nutritionInfo = new ConfigBoolean("bettertips.nutritionInfo", true);
    public static final ConfigBoolean highLevelEnchantmentInfo = new ConfigBoolean("bettertips.highLevelEnchantmentInfo", true);
    public static final ConfigBoolean burnInfo = new ConfigBoolean("bettertips.burnInfo", true);
    public static final ConfigBoolean CreativeTabInfo = new ConfigBoolean("bettertips.CreativeTabInfo", true);
    public static final ConfigBoolean modInfo = new ConfigBoolean("bettertips.modInfo", true);
    public static final ConfigBoolean reachBonusInfo = new ConfigBoolean("bettertips.reachBonusInfo", true);
    public static final ConfigBoolean unlocalizedNameInfo = new ConfigBoolean("bettertips.unlocalizedNameInfo", false);
    public static final ConfigBoolean maxQualityInfo = new ConfigBoolean("bettertips.maxQualityInfo", false);
    public static final ConfigBoolean enchantabilityInfo = new ConfigBoolean("bettertips.enchantabilityInfo", false);
    public static final ConfigBoolean repairItemInfo = new ConfigBoolean("bettertips.repairItemInfo", false);
    public static final List<ConfigBase<?>> base = List.of(nutritionInfo, highLevelEnchantmentInfo, burnInfo, CreativeTabInfo, modInfo, reachBonusInfo, unlocalizedNameInfo, repairItemInfo, maxQualityInfo, enchantabilityInfo);

    static {
        List<ConfigBase<?>> configValues = new ArrayList<>();
        configValues.addAll(base);
        Instance = new BetterTipsConfig();
    }

    public BetterTipsConfig() {
        super("BetterTips", null, base);
    }

    public static BetterTipsConfig getInstance() {
        return Instance;
    }
}
