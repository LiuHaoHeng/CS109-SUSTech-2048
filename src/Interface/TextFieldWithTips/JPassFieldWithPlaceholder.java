package Interface.TextFieldWithTips;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JPassFieldWithPlaceholder extends JPasswordField  {
    private String placeholder;

    public JPassFieldWithPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        setBorder(null);

        // 添加焦点监听器，当获得或失去焦点时重绘组件
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 如果密码字段为空且未获得焦点，显示占位符
        if (getPassword().length == 0 && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, (getWidth() - fm.stringWidth(placeholder)) / 2, g.getFontMetrics().getMaxAscent() + getInsets().top);
                g2.dispose();
        }
    }

}