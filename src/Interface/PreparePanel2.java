package Interface;

import Setting.Fonts;

import javax.swing.*;
import java.awt.*;

public class PreparePanel2 extends JPanel {


    public PreparePanel2() {
        setLayout(null);

        JButton threeButton = getjButton(241, "3 X 3");
        JButton fourButton = getjButton(404, "4 X 4 (推荐)");
        JButton fiveButton = getjButton(568, "5 X 5");
        JButton sixButton = getjButton(731, "6 X 6");

        buttonToSize(threeButton, 3);
        buttonToSize(fourButton, 4);
        buttonToSize(fiveButton, 5);
        buttonToSize(sixButton, 6);

        JLabel jLabel = new JLabel();
        jLabel.setText("Size Selection");
        jLabel.setFont(Fonts.NORMAL_TITLE.font);
        jLabel.setBounds(219, 97, 400, 74);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(threeButton);
        add(fourButton);
        add(fiveButton);
        add(sixButton);
        add(jLabel);
    }

    private void buttonToSize(JButton threeButton, int size) {
        threeButton.addActionListener(e -> {
            MainFrame.getMainFrame().getNowGameBoard().setSize(size);
            MainFrame.getMainFrame().switchTo("Game");
        });
    }


    private static JButton getjButton(int y, String text) {
        JButton continueButton = new JButton();
        continueButton.setBounds(255, y, 326, 83);
        continueButton.setText(text);
        continueButton.setFont(Fonts.NORMAL_THICK.font);
        return continueButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = MainFrame.getMainFrame().getNowSettings().getBackground().getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}
