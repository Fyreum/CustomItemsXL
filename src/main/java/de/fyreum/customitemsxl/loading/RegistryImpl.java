package de.fyreum.customitemsxl.loading;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegistryImpl<T> implements Registry<T> {

    final String prefix;
    private final Set<T> registered;

    public RegistryImpl(Class<T> clazz) {
        this(new HashSet<>(), clazz);
    }

    public RegistryImpl(Set<T> registered, Class<T> clazz) {
        prefix = "[Registry<" + clazz.getName() + ">] ";
        CustomItemsXL.LOGGER.debug(
                new DebugMode[]{DebugMode.HIGH, DebugMode.MEDIUM, DebugMode.LOW},
                prefix + "Loading Registry, pre-existing contents: " + toString(registered),
                prefix + "Loading Registry, pre-existing contents: " + registered.isEmpty(),
                prefix + "Loading Registry..."
        );
        this.registered = registered;
    }

    public String toString(Set<T> s) {
        if (s == null)
            return "null";

        if (s.isEmpty())
            return "empty";

        List<Object> l = new ArrayList<>(s);
        return Util.toDebugString(l);
    }

    @Override
    public void register(T value) {
        CustomItemsXL.LOGGER.debug(DebugMode.LOW, prefix + "Registering value: " + value.toString());
        this.registered.add(value);
    }

    @Override
    public void unregister(T value) {
        CustomItemsXL.LOGGER.debug(DebugMode.LOW, prefix + "Unregistering value: " + value.toString());
        this.registered.remove(value);
    }

    @Override
    public Set<T> getRegistered() {
        return registered;
    }

    @Override
    public boolean isRegistered(T value) {
        return registered.contains(value);
    }

    public String getPrefix() {
        return prefix;
    }
}
