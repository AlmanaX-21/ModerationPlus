package me.almana.moderationplus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hypixel.hytale.logger.HytaleLogger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

public class ConfigManager {

    private static final HytaleLogger logger = HytaleLogger.forEnclosingClass();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_PATH = "mods/data/moderationplus_config.json";

    private JsonObject config;

    public ConfigManager() {
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(CONFIG_PATH);
        if (!configFile.exists()) {
            config = new JsonObject();
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(configFile)) {
                config = GSON.fromJson(reader, JsonObject.class);
                if (config == null) {
                    config = new JsonObject();
                }
            } catch (Exception e) {
                logger.at(Level.SEVERE).withCause(e).log("Failed to load config, using defaults");
                config = new JsonObject();
            }
        }

        if (!config.has("database")) {
            config.add("database", new JsonObject());
        }
        JsonObject db = config.getAsJsonObject("database");
        if (!db.has("flush_interval_seconds")) {
            db.addProperty("flush_interval_seconds", 600);
            saveConfig();
        }
    }

    public long getDatabaseFlushIntervalSeconds() {
        if (!config.has("database"))
            return 600;
        JsonObject db = config.getAsJsonObject("database");
        if (!db.has("flush_interval_seconds"))
            return 600;
        return db.get("flush_interval_seconds").getAsLong();
    }

    public void saveConfig() {
        File configFile = new File(CONFIG_PATH);
        try {
            configFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(config, writer);
            }
        } catch (Exception e) {
            logger.at(Level.SEVERE).withCause(e).log("Failed to save config");
        }
    }

    public void setJailLocation(double x, double y, double z) {
        JsonObject jail;
        if (config.has("jail")) {
            jail = config.getAsJsonObject("jail");
        } else {
            jail = new JsonObject();
            config.add("jail", jail);
        }
        jail.addProperty("x", x);
        jail.addProperty("y", y);
        jail.addProperty("z", z);
        saveConfig();
    }

    public void setJailRadius(double radius) {
        JsonObject jail;
        if (config.has("jail")) {
            jail = config.getAsJsonObject("jail");
        } else {
            jail = new JsonObject();
            config.add("jail", jail);
        }
        jail.addProperty("radius", radius);
        saveConfig();
    }

    public double[] getJailLocation() {
        if (!config.has("jail")) {
            return null;
        }
        JsonObject jail = config.getAsJsonObject("jail");
        if (!jail.has("x") || !jail.has("y") || !jail.has("z")) {
            return null;
        }
        return new double[] {
                jail.get("x").getAsDouble(),
                jail.get("y").getAsDouble(),
                jail.get("z").getAsDouble()
        };
    }

    public double getJailRadius() {
        if (!config.has("jail")) {
            return 10.0;
        }
        JsonObject jail = config.getAsJsonObject("jail");
        if (!jail.has("radius")) {
            return 10.0;
        }
        return jail.get("radius").getAsDouble();
    }

    public boolean hasJailLocation() {
        return config.has("jail") && getJailLocation() != null;
    }
}
