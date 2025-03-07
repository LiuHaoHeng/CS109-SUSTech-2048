package Setting;

import java.io.Serializable;

public enum Background implements Serializable {
    MOUNT("Background\\mountain\\mountain-2.jpg"),
    CLOUD("Background\\cloud\\cloud2.jpg"),
    CITY("Background\\city\\city.jpg");

    private String Path;

    Background(String path) {
        Path = path;
    }

    public String getPath() {
        return Path;
    }

}
