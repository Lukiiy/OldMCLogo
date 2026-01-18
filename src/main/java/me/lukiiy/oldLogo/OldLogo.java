package me.lukiiy.oldLogo;

import me.lukiiy.oldLogo.mixin.MinecraftAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OldLogo implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("oldlogo");
    public static Minecraft minecraft;

    public static boolean isLogoRendering = false;

    @Override
    public void onInitialize() {}
}
