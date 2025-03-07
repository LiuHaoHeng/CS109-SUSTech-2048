package Interface;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Block extends JComponent implements Serializable {
    private int val = 0;
    private MainFrame mainFrame;

    public Block(MainFrame mainFrame, int val, int gridSize) {
        this.val = val;
        this.mainFrame = mainFrame;
        this.setSize(gridSize, gridSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        if (this.val > 0) {
            ImageIcon k = new ImageIcon((new ImageIcon(mainFrame.getNowSettings().getType().getPath() + (String.valueOf((int) Math.pow(2, this.val))) + ".png")).getImage().getScaledInstance(getWidth() - 10, getHeight() - 10, Image.SCALE_SMOOTH));
            g.drawImage(k.getImage(), 5, 5, this);
        }
        if (this.val == 0) {
            g.setColor(new Color(128, 128, 128, 200));//Color.GRAY
            g.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
        }
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public void doubleVal() {
        this.val += 1;
    }

}
