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

    public static void renderScrollingBackground(Screen screen) { // TODO
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1, 1, 1, 1);

        float scale = 32;
        float scroll = (float) (((MinecraftAccessor) minecraft).getTicks()) * .01f;
        Tessellator tessellator = Tessellator.INSTANCE;

        tessellator.startQuads();
        tessellator.color(0x404040);
        tessellator.vertex(0, screen.height, 0, 0, screen.height / scale + scroll);
        tessellator.vertex(screen.width, screen.height, 0, screen.width / scale, screen.height / scale + scroll);
        tessellator.vertex(screen.width, 0, 0, screen.width / scale, scroll);
        tessellator.vertex(0, 0, 0, 0, scroll);
        tessellator.draw();
    }
}
