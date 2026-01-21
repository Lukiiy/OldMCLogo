package me.lukiiy.oldLogo;

import com.google.gson.*;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject properties = new JsonObject();
    private final File file;

    public Config(String fileName) {
        File confDir = new File(Minecraft.getRunDirectory(), "config");
        if (!confDir.exists()) confDir.mkdirs();

        this.file = new File(confDir, fileName + ".json");
        load();
    }

    public void load() {
        if (!file.exists()) {
            save();
            return;
        }

        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            properties = gson.fromJson(reader, JsonObject.class);

            if (properties == null) properties = new JsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
            gson.toJson(properties, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean has(String key) {
        return properties.has(key);
    }

    public void set(String key, Object value) {
        if (value instanceof String) properties.addProperty(key, (String) value);
        else if (value instanceof Boolean) properties.addProperty(key, (Boolean) value);
        else if (value instanceof Number) properties.addProperty(key, (Number) value);
        else if (value instanceof String[]) {
            JsonArray array = new JsonArray();

            for (String s : (String[]) value) array.add(s);
            properties.add(key, array);
        }

        save();
    }

    public Object get(String key, Object defaultVal) { // omygod
        if (!has(key)) set(key, defaultVal);

        JsonElement elemeent = properties.get(key);
        if (elemeent == null) return defaultVal;

        if (defaultVal instanceof String) return elemeent.isJsonPrimitive() ? elemeent.getAsString() : defaultVal;
        else if (defaultVal instanceof Boolean) return elemeent.isJsonPrimitive() ? elemeent.getAsBoolean() : defaultVal;
        else if (defaultVal instanceof Integer) return elemeent.isJsonPrimitive() ? elemeent.getAsInt() : defaultVal;
        else if (defaultVal instanceof Long) return elemeent.isJsonPrimitive() ? elemeent.getAsLong() : defaultVal;
        else if (defaultVal instanceof String[]) {
            if (!elemeent.isJsonArray()) return defaultVal;

            JsonArray array = elemeent.getAsJsonArray();
            String[] result = new String[array.size()];

            for (int i = 0; i < array.size(); i++) result[i] = array.get(i).getAsString();
            return result;
        }

        return defaultVal;
    }

    public void setIfAbsent(String key, Object value) {
        if (has(key)) return;

        set(key, value);
    }

    public Map<Character, Integer> getCharMapping(String key, Map<Character, Integer> def) {
        if (!has(key)) {
            JsonObject obj = new JsonObject();

            for (Map.Entry<Character, Integer> e : def.entrySet()) obj.addProperty(String.valueOf(e.getKey()), e.getValue());
            properties.add(key, obj);
            save();

            return new HashMap<>(def);
        }

        JsonElement el = properties.get(key);
        if (!el.isJsonObject()) return new HashMap<>(def);

        Map<Character, Integer> map = new HashMap<>();
        JsonObject obj = el.getAsJsonObject();

        for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
            if (e.getKey().length() == 1 && e.getValue().isJsonPrimitive()) map.put(e.getKey().charAt(0), e.getValue().getAsInt());
        }

        return map;
    }
}
