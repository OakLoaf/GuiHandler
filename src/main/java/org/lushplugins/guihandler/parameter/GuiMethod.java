package org.lushplugins.guihandler.parameter;

import org.lushplugins.guihandler.slot.SlotContext;
import org.lushplugins.guihandler.util.reflect.MethodCaller;

import java.util.Map;

public class GuiMethod {
    private final MethodCaller.BoundMethodCaller caller;
    /**
     * Map of parameter name to parameter provider
     */
    private final Map<String, GuiParameter<?>> parameterProviders;

    public GuiMethod(MethodCaller.BoundMethodCaller caller, Map<String, GuiParameter<?>> parameterProviders) {
        this.caller = caller;
        this.parameterProviders = parameterProviders;
    }

    public <T> T call(SlotContext context) {
        Object[] arguments = this.parameterProviders.values().stream()
            .map(provider -> provider.asObject(context))
            .toArray();

        //noinspection unchecked
        return (T) caller.call(arguments);
    }
}
