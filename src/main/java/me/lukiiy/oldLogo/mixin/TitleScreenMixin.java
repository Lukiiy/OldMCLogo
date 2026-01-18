package me.lukiiy.oldLogo.mixin;

import me.lukiiy.oldLogo.LogoEffectRandomizer;
import me.lukiiy.oldLogo.OldLogo;
import net.minecraft.block.Block;
import net.minecraft.class_564;
import net.minecraft.class_13;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Shadow @Final private static Random RANDOM;

    @Unique
    private static final String[] OLD_LOGO = new String[]{
            " *   * * *   * *** *** *** *** *** ***",
            " ** ** * **  * *   *   * * * * *    * ",
            " * * * * * * * **  *   **  *** **   * ",
            " *   * * *  ** *   *   * * * * *    * ",
            " *   * * *   * *** *** * * * * *    * "
    };

    @Unique
    private LogoEffectRandomizer[][] logoEffects;

    public TitleScreenMixin(LogoEffectRandomizer[][] logoEffects) {
        this.logoEffects = logoEffects;
    }

    @Redirect(method = "render(IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(IIIIII)V"))
    private void oldLogo$cancelOriginal(TitleScreen instance, int x, int y, int u, int v, int w, int h) {
        if (w == 155 && h == 44) return;

        instance.drawTexture(x, y, u, v, w, h);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void oldLogo$tick(CallbackInfo ci) {
        if (logoEffects == null) return;

        for (LogoEffectRandomizer[] logoEffect : logoEffects) {
            for (LogoEffectRandomizer logoEffectRandomizer : logoEffect) logoEffectRandomizer.tick();
        }
    }

    @Inject(method = "render(IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;renderBackground()V", shift = At.Shift.AFTER))
    private void oldLogo$render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Minecraft minecraft = OldLogo.minecraft;

        if (logoEffects == null) {
            logoEffects = new LogoEffectRandomizer[OLD_LOGO[0].length()][OLD_LOGO.length];

            for (int x = 0; x < logoEffects.length; x++) {
                for (int y = 0; y < logoEffects[x].length; y++) logoEffects[x][y] = new LogoEffectRandomizer(RANDOM, x, y);
            }
        }

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_TEXTURE_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        class_564 scaledRes = new class_564(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
        int viewportHeight = 120 * scaledRes.field_2391;

        GLU.gluPerspective(70, (float) minecraft.displayWidth / (float) viewportHeight, .05f, 100);
        GL11.glViewport(0, minecraft.displayHeight - viewportHeight, minecraft.displayWidth, viewportHeight);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        class_13 blockRenderer = new class_13();

        for (int pass = 0; pass < 3; pass++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(.4f, .6f, -13f);

            if (pass == 0) {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glTranslatef(0, -.4f, 0);
                GL11.glScalef(.98f, 1, 1);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            } else if (pass == 1) {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            } else {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            GL11.glScalef(1, -1, 1);
            GL11.glRotatef(15f, 1, 0, 0);
            GL11.glScalef(.89f, 1, .4f);
            GL11.glTranslatef(-OLD_LOGO[0].length() * .5f, -OLD_LOGO.length * .5f, 0);

            if (pass == 0) GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/title/black.png"));
            else GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/terrain.png"));

            for (int y = 0; y < OLD_LOGO.length; y++) {
                for (int x = 0; x < OLD_LOGO[y].length(); x++) {
                    if (OLD_LOGO[y].charAt(x) == ' ') continue;

                    GL11.glPushMatrix();
                    LogoEffectRandomizer effect = logoEffects[x][y];

                    float z = (float) (effect.current + (effect.target - effect.current) * delta);
                    float scale = 1;
                    if (pass == 0) {
                        scale = z * .04f + 1;
                        z = 0;
                    }

                    GL11.glTranslatef(x, y, z);
                    GL11.glScalef(scale, scale, scale);
                    OldLogo.isLogoRendering = true;

                    blockRenderer.method_48(Block.STONE, 0, .77f);

                    OldLogo.isLogoRendering = false;
                    GL11.glColor4f(1, 1, 1, 1);
                    GL11.glPopMatrix();
                }
            }

            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glViewport(0, 0, minecraft.displayWidth, minecraft.displayHeight);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopAttrib();
    }

    @Redirect(method = "render(IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;renderBackground()V"))
    private void oldlogo$replaceBackground(TitleScreen screen) {
        OldLogo.renderScrollingBackground(screen);
    }
}
