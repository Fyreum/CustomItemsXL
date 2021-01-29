package de.fyreum.customitemsxl.serialization.roots;

public interface Root<T> {

    String getId(); // name of the value

    String getSerialized();

    String setSerialized(String s);

    T getDeserialized();

    void setDeserialized(T deserialized);
}
