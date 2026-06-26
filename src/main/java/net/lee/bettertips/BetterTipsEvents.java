package net.lee.bettertips;

import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.listener.ITooltipListener;
import net.lee.bettertips.config.BetterTipsConfig;
import net.lee.bettertips.util.ModIdentification;
import net.minecraft.Enchantment;
import net.minecraft.EnchantmentData;
import net.minecraft.EntityPlayer;
import net.minecraft.EnumChatFormatting;
import net.minecraft.EnumRarity;
import net.minecraft.I18n;
import net.minecraft.Item;
import net.minecraft.ItemBow;
import net.minecraft.ItemStack;
import net.minecraft.ItemTool;
import net.minecraft.Minecraft;
import net.minecraft.NBTTagCompound;
import net.minecraft.NBTTagList;
import net.minecraft.Slot;
import net.minecraft.StatCollector;
import net.minecraft.TileEntityFurnace;
import net.minecraft.Translator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BetterTipsEvents extends Handlers {
	public static void register() {
		Tooltip.register(new ITooltipListener() {
			@Override
			public void onTooltipHead(ItemStack stack, List<String> tooltip, EntityPlayer player, boolean detailed, Slot slot) {
				if (BetterTipsConfig.CreativeTabInfo.getBooleanValue() && player.inCreativeMode() && stack.getItem().getCreativeTab() != null) {
					tooltip.add(EnumChatFormatting.BLUE + I18n.getString(stack.getItem().getCreativeTab().getTranslatedTabLabel()));
				}
				if (detailed && BetterTipsConfig.unlocalizedNameInfo.getBooleanValue() && Minecraft.inDevMode() && stack.getItem().getUnlocalizedName() != null) {
					tooltip.add(stack.getItem().getUnlocalizedName());
				}
			}
			
			@Override
			public void onTooltipNeck(ItemStack stack, List<String> tooltip, EntityPlayer player, boolean detailed, Slot slot) {
				if (detailed) {
					if (BetterTipsConfig.repairItemInfo.getBooleanValue() && stack.getItem().isDamageable() && stack.getItem().getRepairItem() != null && stack.getItem().getMaxDamage(stack) != 0) {
						tooltip.add(Translator.getFormatted("item.tooltip.repair_item", String.valueOf(stack.getItem().getRepairItem())));
					}
					if (BetterTipsConfig.maxQualityInfo.getBooleanValue() && stack.getItem().hasQuality() && stack.getItem().getMaxQuality() != null) {
						tooltip.add(Translator.getFormatted("item.tooltip.max_quality", String.valueOf(stack.getItem().getMaxQuality().getDescriptor())));
					}
					if (BetterTipsConfig.enchantabilityInfo.getBooleanValue() && stack.getItem().getItemEnchantability() != 0) {
						tooltip.add(Translator.getFormatted("item.tooltip.max_enchantability", String.valueOf(stack.getItem().getItemEnchantability())));
					}
				}
			}
			
			@Override
			public void onTooltipBody(ItemStack stack, List<String> tooltip, EntityPlayer player, boolean detailed, Slot slot) {
				BetterTipsEvents.addFoodExtraInfo(stack, tooltip, detailed);
				BetterTipsEvents.addInformationBeforeEnchantments(stack, tooltip);
				BetterTipsEvents.addInformationFishingRodBeforeEnchantments(stack, tooltip);
				BetterTipsEvents.addHeatLevelInfo(stack, tooltip, detailed);
			}
			
			@Override
			public void onTooltipWaist(ItemStack stack, List<String> tooltip, EntityPlayer player, boolean detailed, Slot slot) {
				if (BetterTipsConfig.reachBonusInfo.getBooleanValue() && detailed && stack.getItem().getReachBonus() != 0.0f) {
					tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus.0", new Object[]{ItemStack.field_111284_a.format(stack.getItem().getReachBonus()), StatCollector.translateToLocal("attribute.name.reach_bonus")}));
				}
			}
			
			@Override
			public void onTooltipTail(ItemStack stack, List<String> tooltip, EntityPlayer player, boolean detailed, Slot slot) {
				if (!BetterTipsConfig.modInfo.getBooleanValue()) {
					return;
				}
				tooltip.add(ModIdentification.getMod(stack));
			}
		});
	}

	public static void addFoodExtraInfo(ItemStack stack, List tooltip, boolean detailed) {
		if (!BetterTipsConfig.nutritionInfo.getBooleanValue()) {
			return;
		}
		Item item = Item.getItem(stack.itemID);
		int insulin_response = item.getInsulinResponse();
		if (detailed) {
			if (item.hasProtein()) {
				tooltip.add(EnumChatFormatting.YELLOW + Translator.getFormatted("item.tooltip.protein", item.getProtein()));
			}
			if (Minecraft.inDevMode() && item.hasEssentialFats()) {
				tooltip.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.essential_fats", item.getEssentialFats()));
			}
			if (item.hasPhytonutrients()) {
				tooltip.add(EnumChatFormatting.GREEN + Translator.getFormatted("item.tooltip.phytonutrients", item.getPhytonutrients()));
			}
			if (insulin_response > 0) {
				tooltip.add(EnumChatFormatting.AQUA + Translator.getFormatted("item.tooltip.insulin_response", insulin_response));
			}
		}
	}
	
	public static void addInformationBeforeEnchantments(ItemStack item_stack, List list) {
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
	
	public static void addInformationFishingRodBeforeEnchantments(ItemStack stack, List tooltip) {
		NBTTagList tagList;
		if (!BetterTipsConfig.highLevelEnchantmentInfo.getBooleanValue()) {
			return;
		}
		Item item = Item.itemsList[stack.itemID];
		if (stack.hasTagCompound() && (tagList = stack.getEnchantmentTagList()) != null) {
			if (tagList.tagCount() > 0 && !(item instanceof ItemTool) && !(item instanceof ItemBow)) {
				tooltip.add("");
			}
			for (int i = 0; i < tagList.tagCount(); i++) {
				short id = ((NBTTagCompound) tagList.tagAt(i)).getShort("id");
				short lvl = ((NBTTagCompound) tagList.tagAt(i)).getShort("lvl");
				Enchantment enchantment = Enchantment.enchantmentsList[id];
				if (enchantment != null && !(item instanceof ItemTool) && !(item instanceof ItemBow)) {
					if (enchantment.rarity == EnumRarity.common) {
						tooltip.add(EnumChatFormatting.WHITE + Enchantment.enchantmentsList[id].getTranslatedName(lvl, stack));
					} else if (enchantment.rarity == EnumRarity.uncommon) {
						tooltip.add(EnumChatFormatting.YELLOW + Enchantment.enchantmentsList[id].getTranslatedName(lvl, stack));
					} else if (enchantment.rarity == EnumRarity.rare) {
						tooltip.add(EnumChatFormatting.AQUA + Enchantment.enchantmentsList[id].getTranslatedName(lvl, stack));
					} else if (enchantment.rarity == EnumRarity.epic) {
						tooltip.add(EnumChatFormatting.LIGHT_PURPLE + Enchantment.enchantmentsList[id].getTranslatedName(lvl, stack));
					}
				}
			}
		}
	}
	
	private static void addHeatLevelInfo(ItemStack stack, List info, boolean extended_info) {
		if (!BetterTipsConfig.burnInfo.getBooleanValue()) {
			return;
		}
		int required_heat_level = TileEntityFurnace.getHeatLevelRequired(stack.itemID);
		Item item = Item.getItem(stack.itemID);
		if (extended_info) {
			if (item.getHeatLevel(stack) != 0) {
				info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.heat_level", item.getHeatLevel(stack)));
			}
			if (required_heat_level > 1) {
				info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.required_heat_level", required_heat_level));
			}
			if (item.getBurnTime(stack) > 0) {
				info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.burn_time", item.getBurnTime(stack) / 20));
			}
			if (item.getBurnTime(stack) > 0) {
				info.add(EnumChatFormatting.GOLD + Translator.getFormatted("item.tooltip.smelt_item", item.getBurnTime(stack) / 200.0));
			}
		}
	}
}
