package org.lushplugins.guihandler.config.slot;

import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.guihandler.config.slot.action.SlotActionRegistry;
import org.lushplugins.guihandler.slot.SlotAction;

public class SlotConfig {
    private final IconConfig icon;
    private final SlotAction action;

    public SlotConfig(ConfigurationSection config) {
        this.icon = readIcon(config, "icon");
        this.action = readAction(config, "action");
    }

    public IconConfig icon() {
        return icon;
    }

    public SlotAction action() {
        return action;
    }

    private static IconConfig readIcon(ConfigurationSection config, String path) {
        return config.contains(path) ? new IconConfig(config.getConfigurationSection(path)) : null;
    }

    private static SlotAction readAction(ConfigurationSection config, String path) {
        if (config.isConfigurationSection(path)) {
            String type = config.getString("type");
            return SlotActionRegistry.construct(type, config.getConfigurationSection(path));
        } else if (config.isString(path)) {
            String type = config.getString(path);
            if (type == null) {
                type = switch (config.getName()) {
                    case ">" -> "next_page";
                    case "<" -> "previous_page";
                    default -> null;
                };
            }

            if (type != null) {
                return SlotActionRegistry.construct(type, null);
            }
        }

        return null;
    }
}
