package de.fyreum.customitemsxl.serialization.roots;

public abstract class RootBase<T> implements Root<T> {

    private final String id;

    public RootBase(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
