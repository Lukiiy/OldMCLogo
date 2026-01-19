package me.lukiiy.oldLogo;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OldLogo implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("oldlogo");
    public static Minecraft minecraft;

    public static boolean isLogoRendering = false;

    @Override
    public void onInitialize() {}
}
