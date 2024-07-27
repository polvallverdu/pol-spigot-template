package dev.pov.template.hooks;

import dev.pov.template.TemplatePlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class HookManager {

    private final @Nullable PlaceholderApiHook placeholderApiHook;

    public HookManager() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderApiHook = new PlaceholderApiHook();
        } else {
            this.placeholderApiHook = null;
            TemplatePlugin.getInstance().getLogger().warning("PlaceholderAPI not found! Some features may not work.");
        }
    }

    public @Nullable PlaceholderApiHook getPlaceholderApi() {
        return placeholderApiHook;
    }

    public boolean isPlaceholderApiEnabled() {
        return placeholderApiHook != null;
    }

}
