package org.lushplugins.guihandler.config.slot.action;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.slot.SlotAction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SlotActionRegistry {
    private static final Map<String, Function<@Nullable ConfigurationSection, SlotAction>> types = new ConcurrentHashMap<>();

    static {
        register("next_page", (ignored) -> new NextPageSlotAction());
        register("previous_page", (ignored) -> new PreviousPageSlotAction());
    }

    public static SlotAction construct(String type, @Nullable ConfigurationSection config) {
        Function<@Nullable ConfigurationSection, SlotAction> constructor = types.get(type);
        return constructor != null ? constructor.apply(config) : null;
    }

    public static void register(String type, Function<@Nullable ConfigurationSection, SlotAction> constructor) {
        types.put(type, constructor);
    }

    public static void unregister(String type) {
        types.remove(type);
    }
}
