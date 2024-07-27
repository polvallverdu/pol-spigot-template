package dev.pov.template;

import de.themoep.minedown.adventure.MineDown;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TemplatePlugin extends JavaPlugin {

    private static TemplatePlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        sendConsoleMessage("&3&l===============================");
        sendConsoleMessage("&bTemplatePlugin is loading...");
        sendConsoleMessage("");
        sendConsoleMessage("Enabling Hooks");

        sendConsoleMessage("&aTemplatePlugin loaded successfully! Template made by Pol (polv.dev)");
        sendConsoleMessage("&3&l===============================");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static TemplatePlugin getInstance() {
        return instance;
    }

    private void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(MineDown.parse(message));
    }

}
