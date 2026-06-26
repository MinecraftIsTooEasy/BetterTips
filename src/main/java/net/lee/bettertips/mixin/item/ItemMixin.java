package net.lee.bettertips.mixin.item;

import java.util.List;
import net.lee.bettertips.config.BetterTipsConfig;
import net.minecraft.Enchantment;
import net.minecraft.EntityPlayer;
import net.minecraft.EnumChatFormatting;
import net.minecraft.EnumRarity;
import net.minecraft.Item;
import net.minecraft.ItemBow;
import net.minecraft.ItemStack;
import net.minecraft.ItemTool;
import net.minecraft.Minecraft;
import net.minecraft.NBTTagCompound;
import net.minecraft.NBTTagList;
import net.minecraft.Slot;
import net.minecraft.TileEntityFurnace;
import net.minecraft.Translator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Item.class, priority = 2000)
public abstract class ItemMixin {
    @Shadow private boolean has_protein;
    @Shadow private boolean has_essential_fats;
    @Shadow private boolean has_phytonutrients;
    @Shadow @Final public int itemID;
    @Shadow public abstract int getProtein();
    @Shadow public abstract int getPhytonutrients();
    @Shadow public abstract int getEssentialFats();
    
    @Inject(method = "addInformation(Lnet/minecraft/ItemStack;Lnet/minecraft/EntityPlayer;Ljava/util/List;ZLnet/minecraft/Slot;)V", at = @At("RETURN"))
    private void addFoodInfo(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot, CallbackInfo callbackInfo) {
        if (!BetterTipsConfig.nutritionInfo.getBooleanValue()) {
            return;
        }
        Item item = Item.getItem(item_stack.itemID);
        int insulin_response = item.getInsulinResponse();
        if (extended_info) {
            if (this.has_protein) {
                info.add(EnumChatFormatting.YELLOW + Translator.getFormatted("item.tooltip.protein", getProtein()));
            }
            if (Minecraft.inDevMode() && this.has_essential_fats) {
                info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.essential_fats", getEssentialFats()));
            }
            if (this.has_phytonutrients) {
                info.add(EnumChatFormatting.GREEN + Translator.getFormatted("item.tooltip.phytonutrients", getPhytonutrients()));
            }
            if (insulin_response > 0) {
                info.add(EnumChatFormatting.AQUA + Translator.getFormatted("item.tooltip.insulin_response", insulin_response));
            }
        }
    }
    
    public void addInformationBeforeEnchantments(ItemStack item_stack, EntityPlayer player, List list, boolean extended_info, Slot slot) {
        NBTTagList tagList;
        if (BetterTipsConfig.highLevelEnchantmentInfo.getBooleanValue() && item_stack.hasTagCompound() && (tagList = item_stack.getEnchantmentTagList()) != null) {
            if (tagList.tagCount() > 0) {
                list.add("");
            }
            for (int i = 0; i < tagList.tagCount(); i++) {
                short id = ((NBTTagCompound) tagList.tagAt(i)).getShort("id");
                short lvl = ((NBTTagCompound) tagList.tagAt(i)).getShort("lvl");
                Enchantment enchantment = Enchantment.enchantmentsList[id];
                if (enchantment != null) {
                    if (enchantment.rarity == EnumRarity.common) {
                        list.add(EnumChatFormatting.WHITE + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    } else if (enchantment.rarity == EnumRarity.uncommon) {
                        list.add(EnumChatFormatting.YELLOW + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    } else if (enchantment.rarity == EnumRarity.rare) {
                        list.add(EnumChatFormatting.AQUA + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    } else if (enchantment.rarity == EnumRarity.epic) {
                        list.add(EnumChatFormatting.LIGHT_PURPLE + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    }
                }
            }
        }
    }
    
    @Inject(method = "addInformation(Lnet/minecraft/ItemStack;Lnet/minecraft/EntityPlayer;Ljava/util/List;ZLnet/minecraft/Slot;)V", at = @At("RETURN"))
    public void addInformationFishingRodBeforeEnchantments(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot, CallbackInfo ci) {
        NBTTagList tagList;
        if (!BetterTipsConfig.highLevelEnchantmentInfo.getBooleanValue()) {
            return;
        }
        Item item = Item.itemsList[this.itemID];
        if (item_stack.hasTagCompound() && (tagList = item_stack.getEnchantmentTagList()) != null) {
            if (tagList.tagCount() > 0 && !(item instanceof ItemTool) && !(item instanceof ItemBow)) {
                info.add("");
            }
            for (int i = 0; i < tagList.tagCount(); i++) {
                short id = ((NBTTagCompound) tagList.tagAt(i)).getShort("id");
                short lvl = ((NBTTagCompound) tagList.tagAt(i)).getShort("lvl");
                Enchantment enchantment = Enchantment.enchantmentsList[id];
                if (enchantment != null && !(item instanceof ItemTool) && !(item instanceof ItemBow)) {
                    if (enchantment.rarity == EnumRarity.common) {
                        info.add(EnumChatFormatting.WHITE + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    } else if (enchantment.rarity == EnumRarity.uncommon) {
                        info.add(EnumChatFormatting.YELLOW + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    } else if (enchantment.rarity == EnumRarity.rare) {
                        info.add(EnumChatFormatting.AQUA + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    } else if (enchantment.rarity == EnumRarity.epic) {
                        info.add(EnumChatFormatting.LIGHT_PURPLE + Enchantment.enchantmentsList[id].getTranslatedName(lvl, item_stack));
                    }
                }
            }
        }
    }
    
    @Inject(method = "addInformation(Lnet/minecraft/ItemStack;Lnet/minecraft/EntityPlayer;Ljava/util/List;ZLnet/minecraft/Slot;)V", at = @At("RETURN"))
    private void addHeatLevelInfo(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot, CallbackInfo callbackInfo) {
        if (!BetterTipsConfig.burnInfo.getBooleanValue()) {
            return;
        }
        int required_heat_level = TileEntityFurnace.getHeatLevelRequired(this.itemID);
        Item item = Item.getItem(item_stack.itemID);
        if (extended_info) {
            if (item.getHeatLevel(item_stack) != 0) {
                info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.heat_level", item.getHeatLevel(item_stack)));
            }
            if (required_heat_level > 1) {
                info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.required_heat_level", required_heat_level));
            }
            if (item.getBurnTime(item_stack) > 0) {
                info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.burn_time", item.getBurnTime(item_stack) / 20));
            }
        }
    }
}
