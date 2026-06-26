package net.lee.bettertips.mixin.item;

import net.minecraft.GuiContainer;
import net.minecraft.GuiScreen;
import net.minecraft.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiContainer.class)
public class GuiContainerMixin extends GuiScreen {

    @Shadow protected static RenderItem itemRenderer = new RenderItem();

    @Unique
    private static String splitString(String str, int length) {
        int stringLength = str.length();
        String part = str;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < stringLength) {
                int endIndex = Math.min(i2 + length, stringLength);
                part = str.substring(i2, endIndex);
                i = i2 + length;
            } else {
                return part;
            }
        }
    }
}
