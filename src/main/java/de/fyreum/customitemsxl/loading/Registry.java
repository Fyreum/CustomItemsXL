package de.fyreum.customitemsxl.loading;

import java.util.Set;

public interface Registry<T> {

    void register(T value);

    void unregister(T value);

    Set<T> getRegistered();

    boolean isRegistered(T value);

}
