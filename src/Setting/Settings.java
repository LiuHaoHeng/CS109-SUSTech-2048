package Setting;

import javax.swing.*;
import java.io.Serializable;

public class Settings implements Serializable {
    private ImageIcon background;
    private int size;
    private Type type;
    //private


    public Settings() {
        this.background = new ImageIcon(Background.MOUNT.getPath());
        this.size = 4;
        this.type = Type.Classic;
    }

    public void setBackground(Background background) {
        this.background = new ImageIcon(background.getPath());
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ImageIcon getBackground() {
        return background;
    }

    public int getSize() {
        return size;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
