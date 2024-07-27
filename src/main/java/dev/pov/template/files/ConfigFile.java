package dev.pov.template.files;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class ConfigFile {

    private final JavaPlugin plugin;
    private final String fileName;

    private final File configFile;
    private YamlConfiguration config;

    public ConfigFile(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        File dataFolder = plugin.getDataFolder();
        dataFolder.mkdirs();

        this.configFile = new File(dataFolder, fileName);

        load();
    }

    public void load() {
        if (!configFile.exists()) {
            try {
                // copy from resources
                plugin.saveResource(fileName, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        plugin.getLogger().info("Loaded " + this.configFile.getName() + " file.");
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    // Functions to get Chat Messages
    public Component getComponent(String key, Player player, Object... replacement) {
        String text = this.config.getString(key);

        if (text == null) {
            return Component.text(key);
        }

        text = this.replacePlaceholders(text, player, replacement);

        return MineDown.parse(text);
    }

    public String replacePlaceholders(String text, @Nullable OfflinePlayer player, Object... replacements) {
        for (int i = 0; i < replacements.length; i+=2) {
            text = text.replaceAll("%" + replacements[i] + "%", String.valueOf(replacements[i+1]));
        }

        if (player != null && ) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public List<Component> getComponentList(String key, @Nullable OfflinePlayer player, Object... replacements) {
        List<String> lines = this.config.getStringList(key);

        lines.replaceAll(text -> replacePlaceholders(text, player, replacements));

        return lines.stream().map(MineDown::parse).toList();
    }

    public ItemStack getItem(String path, Player player, Object... replacements) {
        ItemStack item;
        Material mat = Material.getMaterial(config.getString(path + ".material", "").toUpperCase());
        if (mat == null) {
            throw new RuntimeException("Material " + config.getString(path + ".material", path + ".material").toUpperCase() + " not found.");
        }
        int amount = config.getInt(path + ".amount", 1);
        item = new ItemStack(mat, amount);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(getComponent(path + ".name", player, replacements).decoration(TextDecoration.ITALIC, false));
        meta.lore(getComponentList(path + ".lore", player, replacements).stream().map(comp -> comp.decoration(TextDecoration.ITALIC, false)).toList());
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);

        return item;
    }
}
