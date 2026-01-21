package me.lukiiy.oldLogo;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OldLogo implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("oldlogo");
    public static Minecraft minecraft;
    private static final Config CONFIG = new Config("oldLogo");

    public static boolean isLogoRendering = false;

    public static String[] logo = new String[]{
            " *   * * *   * *** *** *** *** *** ***",
            " ** ** * **  * *   *   * * * * *    * ",
            " * * * * * * * **  *   **  *** **   * ",
            " *   * * *  ** *   *   * * * * *    * ",
            " *   * * *   * *** *** * * * * *    * "
    };
    public static Map<Character, Integer> blockMap;

    @Override
    public void onInitialize() {
        loadConfig();
    }

    private void loadConfig() {
        CONFIG.setIfAbsent("logo", logo);

        CONFIG.setIfAbsent("scrollingBackground.enabled", true);
        CONFIG.setIfAbsent("scrollingBackground.onlyTitle", false);

        Map<Character, Integer> defaultBlockMap = new HashMap<>();
        defaultBlockMap.put('*', 1);

        logo = (String[]) CONFIG.get("logo", logo);
        blockMap = CONFIG.getCharMapping("blockMapping", defaultBlockMap);
    }

    public static Config getConfig() {
        return CONFIG;
    }
}
