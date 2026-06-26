package net.lee.bettertips.mixin.item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.llamalad7.mixinextras.sugar.Local;
import net.lee.bettertips.config.BetterTipsConfig;
import net.lee.bettertips.util.ModIdentity;
import net.minecraft.EntityPlayer;
import net.minecraft.EnumChatFormatting;
import net.minecraft.I18n;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.Slot;
import net.minecraft.StatCollector;
import net.minecraft.Translator;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ItemStack.class, priority = 2000)
public abstract class ItemStackMixin {

    @Shadow @Final public static DecimalFormat field_111284_a;
    @Shadow public abstract Item getItem();

    @Redirect(method = "getTooltip(Lnet/minecraft/EntityPlayer;ZLnet/minecraft/Slot;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z", ordinal = 3))
    private boolean removeOriginallyEnchantmentInfo0(ArrayList list, Object o) {
        return !BetterTipsConfig.highLevelEnchantmentInfo.getBooleanValue();
    }

    @Redirect(method = "getTooltip(Lnet/minecraft/EntityPlayer;ZLnet/minecraft/Slot;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z", ordinal = 2))
    private boolean removeOriginallyEnchantmentInfo1(ArrayList list, Object o) {
        return !BetterTipsConfig.highLevelEnchantmentInfo.getBooleanValue();
    }

    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void addModNameInfoACreativeTabName(EntityPlayer par1EntityPlayer, boolean par2, Slot slot, CallbackInfoReturnable<List> cir, @Local(name = "var3") ArrayList var3) {
        if (!BetterTipsConfig.modInfo.getBooleanValue()) {
            return;
        }
        var3.add(ModIdentity.getMod(ReflectHelper.dyCast(this)));
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER))
    private void addCreativeTabName(EntityPlayer par1EntityPlayer, boolean par2, Slot slot, CallbackInfoReturnable<List> cir, @Local(name = "var3") ArrayList var3) {
        if (BetterTipsConfig.CreativeTabInfo.getBooleanValue() && par1EntityPlayer.inCreativeMode() && getItem().getCreativeTab() != null) {
            var3.add(EnumChatFormatting.BLUE + I18n.getString(getItem().getCreativeTab().getTranslatedTabLabel()));
        }
        if (par2 && BetterTipsConfig.unlocalizedNameInfo.getBooleanValue() && Minecraft.inDevMode() && getItem().getUnlocalizedName() != null) {
            var3.add(getItem().getUnlocalizedName());
        }
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void addAttributeModifierExtraInfo(EntityPlayer par1EntityPlayer, boolean par2, Slot slot, CallbackInfoReturnable<List> cir, @Local(name = "var3") ArrayList var3) {
        if (BetterTipsConfig.reachBonusInfo.getBooleanValue() && par2 && getItem().getReachBonus() != 0.0f) {
            var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus.0", new Object[]{field_111284_a.format(getItem().getReachBonus()), StatCollector.translateToLocal("attribute.name.reach_bonus")}));
        }
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/Item;addInformation(Lnet/minecraft/ItemStack;Lnet/minecraft/EntityPlayer;Ljava/util/List;ZLnet/minecraft/Slot;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addOtherInfo(EntityPlayer par1EntityPlayer, boolean par2, Slot slot, CallbackInfoReturnable<List> cir, ArrayList var3) {
        if (par2) {
            if (BetterTipsConfig.repairItemInfo.getBooleanValue() && getItem().isDamageable() && getItem().getRepairItem() != null && getItem().getMaxDamage((ItemStack) ReflectHelper.dyCast(this)) != 0) {
                var3.add(Translator.getFormatted("item.tooltip.repair_item", String.valueOf(getItem().getRepairItem())));
            }
            if (BetterTipsConfig.maxQualityInfo.getBooleanValue() && getItem().hasQuality() && getItem().getMaxQuality() != null) {
                var3.add(Translator.getFormatted("item.tooltip.max_quality", String.valueOf(getItem().getMaxQuality().getDescriptor())));
            }
            if (BetterTipsConfig.enchantabilityInfo.getBooleanValue() && getItem().getItemEnchantability() != 0) {
                var3.add(Translator.getFormatted("item.tooltip.max_enchantability", String.valueOf(getItem().getItemEnchantability())));
            }
        }
    }
}
