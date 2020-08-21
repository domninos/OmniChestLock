package net.omni.chestlock.lockedchests;

import net.omni.chestlock.ChestLockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LockedChestsConfig {
    private final File file;
    private final FileConfiguration config;

    public LockedChestsConfig(ChestLockPlugin plugin) {
        if (plugin.getDataFolder().mkdirs())
            plugin.sendConsole("&aCreated ../ChestLock");

        this.file = new File(plugin.getDataFolder(), "lockedChests.yml");

        if (!file.exists())
            plugin.saveResource("lockedChests.yml", false);

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setNoSave(String path, Object object) {
        config.set(path, object);
    }

    public void set(String path, Object object) {
        setNoSave(path, object);
        save();
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
