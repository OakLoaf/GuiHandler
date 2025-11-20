package org.lushplugins.guihandler.annotation;

import org.jetbrains.annotations.NotNull;
import org.lushplugins.guihandler.GuiHandler;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.gui.GuiAction;
import org.lushplugins.guihandler.parameter.GuiMethod;
import org.lushplugins.guihandler.parameter.GuiParameter;
import org.lushplugins.guihandler.parameter.ParameterProvider;
import org.lushplugins.guihandler.parameter.ParameterProviders;
import org.lushplugins.guihandler.slot.SlotContext;
import org.lushplugins.guihandler.util.reflect.MethodCaller;
import org.lushplugins.guihandler.util.reflect.MethodCallerFactory;
import org.lushplugins.guihandler.util.reflect.Reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnnotationHandler {

    public static Gui.Builder register(GuiHandler guiHandler, @NotNull Class<?> instanceClass, Object instance) {
        Gui.Builder builder = Gui.builder(guiHandler);

        CustomGui guiAnnotation = instanceClass.getAnnotation(CustomGui.class);
        if (guiAnnotation != null) {
            builder.inventoryType(guiAnnotation.inventoryType())
                .title(guiAnnotation.title());

            int size = guiAnnotation.size();
            if (size != CustomGui.DEFAULT_SIZE) {
                builder.size(size);
            }
        }

        for (Method method : Reflection.getAllMethods(instanceClass)) {
            AnnotationList annotations = new AnnotationList(method);
            if (annotations.isEmpty()) {
                continue;
            }

            if (!containsGuiAnnotation(annotations)) {
                continue;
            }

            MethodCaller.BoundMethodCaller caller;
            try {
                caller = MethodCallerFactory.defaultFactory()
                    .createFor(method)
                    .bindTo(instance);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            Map<String, GuiParameter<?>> parameters = new LinkedHashMap<>();
            for (Parameter parameter : method.getParameters()) {
                String name = parameter.getName();

                AnnotationList parameterAnnotations = new AnnotationList(parameter);
                ParameterProvider<?> provider;
                if (parameterAnnotations.contains(Provided.class)) {
                    provider = ParameterProviders.PROVIDED_TYPE;
                } else if (parameterAnnotations.contains(Slot.class)) {
                    Slot slotAnnotation = parameterAnnotations.get(Slot.class);
                    provider = ParameterProviders.createSlotProvider(slotAnnotation);
                } else if (parameterAnnotations.contains(Slots.class)) {
                    Slots slotsAnnotation = parameterAnnotations.get(Slots.class);
                    provider = ParameterProviders.createSlotsProvider(slotsAnnotation);
                } else {
                    provider = ParameterProviders.DEFAULT_PROVIDERS.get(parameter.getType());
                    if (provider == null) {
                        throw new IllegalArgumentException("Invalid parameter type defined at method '%s' with parameter name '%s'"
                            .formatted(method.getName(), name));
                    }
                }

                parameters.put(name, new GuiParameter<>(name, parameter.getType(), provider));
            }

            GuiMethod guiMethod = new GuiMethod(caller, parameters);

            if (annotations.contains(ButtonProvider.class)) {
                ButtonProvider methodAnnotation = annotations.get(ButtonProvider.class);
                for (char label : methodAnnotation.value()) {
                    builder.setButtonFor(label, (event, context) -> guiMethod.call(context));
                }
            } else if (annotations.contains(IconProvider.class)) {
                IconProvider methodAnnotation = annotations.get(IconProvider.class);
                for (char label : methodAnnotation.value()) {
                    builder.setIconProviderFor(label, guiMethod::call);
                }
            } else if (annotations.contains(GuiEvent.class)) {
                GuiEvent methodAnnotation = annotations.get(GuiEvent.class);
                GuiAction action = methodAnnotation.value();
                builder.addAction(action, (context) -> guiMethod.call(new SlotContext(context.gui(), null)));
            }
        }

        return builder;
    }

    private static boolean containsGuiAnnotation(AnnotationList annotations) {
        return annotations.contains(ButtonProvider.class)
            || annotations.contains(IconProvider.class)
            || annotations.contains(GuiEvent.class);
    }
}
