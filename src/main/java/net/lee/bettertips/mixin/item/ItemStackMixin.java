package net.lee.bettertips.mixin.item;

import java.util.ArrayList;

import net.lee.bettertips.config.BetterTipsConfig;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemStack.class, priority = 2000)
public abstract class ItemStackMixin {

    @Redirect(method = "getTooltip(Lnet/minecraft/EntityPlayer;ZLnet/minecraft/Slot;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z", ordinal = 3))
    private boolean removeOriginallyEnchantmentInfo0(ArrayList list, Object o) {
        return !BetterTipsConfig.highLevelEnchantmentInfo.getBooleanValue();
    }

    @Redirect(method = "getTooltip(Lnet/minecraft/EntityPlayer;ZLnet/minecraft/Slot;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z", ordinal = 2))
    private boolean removeOriginallyEnchantmentInfo1(ArrayList list, Object o) {
        return !BetterTipsConfig.highLevelEnchantmentInfo.getBooleanValue();
    }
}
