package Setting;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public enum Fonts implements Serializable {
    TITLE("Fonts\\B_Minecrafter.Reg(主标题).ttf", 50f),

    NORMAL_TITLE("Fonts\\3_Minecraft-Bold(英粗).otf", 45f),
    NORMAL_THICK("Fonts\\5_Minecraft AE(支持中文).ttf", 26f),
    NORMAL_THICK_SMALL("Fonts\\5_Minecraft AE(支持中文).ttf", 20f),
    NORMAL_THIN("Fonts\\类像素字体_俐方体11号.ttf", 24f),
    NORMAL_BUTTON("Fonts\\B_Minecrafter.Reg(主标题).ttf", 45f);

    final public Font font;

    Fonts(String pathname, float size) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(pathname)).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        // 注册字体到GraphicsEnvironment
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
    }
}
