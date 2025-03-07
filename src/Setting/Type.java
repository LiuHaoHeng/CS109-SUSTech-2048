package Setting;

import java.io.Serializable;

public enum Type implements Serializable {
    Classic("tiles\\Classic\\"),
    Animal("tiles\\Animal\\"),
    Food("tiles\\Food\\"),
    Plant("tiles\\Plant\\"),
    Vehicle("tiles\\Vehicle\\");

    private final String path;

    Type(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
