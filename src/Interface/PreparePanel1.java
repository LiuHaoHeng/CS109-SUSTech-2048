package Interface;

import Setting.Fonts;
import Setting.ImageIcons;

import javax.swing.*;
import java.awt.*;

public class PreparePanel1 extends JPanel {

    PreparePanel1() {
        setLayout(null);

        JButton continueButton = getjButton(159, "Continue");
        JButton classicButton = getjButton(405, "Classic Mode");
        JButton barrierButton = getjButton(535, "Barrier Mode");
        JButton itemButton = getjButton(674, "Item Mode");

        JLabel jLabel = new JLabel();
        jLabel.setText("New Game");
        jLabel.setFont(Fonts.NORMAL_TITLE.font);
        jLabel.setBounds(298, 320, 242, 72);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton backButton = new JButton(ImageIcons.backImageDefault);
        initializeIconButton(24, 26, backButton, ImageIcons.backImagePressed);
        
        backButton.addActionListener(e-> MainFrame.getMainFrame().switchTo("Login"));

        continueButton.addActionListener(e -> {
            if (MainFrame.getMainFrame().getNowGameBoard() != null) {
                if (MainFrame.getMainFrame().getNowGameBoard().size == 0) {
                    MainFrame.getMainFrame().switchTo("Prepare2");
                } else {
                    MainFrame.getMainFrame().switchTo("Game");
                }
            } else {
                if (MainFrame.getMainFrame().getNowUser().isNoGuest()) {
                    if (MainFrame.getMainFrame().getNowUser().getGameBoard() == null)
                        JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "该用户暂无存档", "Error", JOptionPane.ERROR_MESSAGE);
                    else {
                        MainFrame.getMainFrame().setNowGameBoard(MainFrame.getMainFrame().getNowUser().getGameBoard());
                    }
                } else {
                    JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "游客模式无法读取存档", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
//            if(MainFrame.getMainFrame().getNowUser().isNoGuest()){
//                if(MainFrame.getMainFrame().getNowGameBoard()==null){
//                    JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "该用户暂无存档", "Error", JOptionPane.ERROR_MESSAGE);
//                }else {
//                    MainFrame.getMainFrame().setNowGameBoard(MainFrame.getMainFrame().getNowUser().getGameBoard());
//
//                }
//            }else{
//                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "游客模式无法读取存档", "Error", JOptionPane.ERROR_MESSAGE);
//            }
        });
        classicButton.addActionListener(e -> {
            MainFrame.getMainFrame().getNowUser().setGameBoard(new GameBoard());
            MainFrame.getMainFrame().setNowGameBoard(MainFrame.getMainFrame().getNowUser().getGameBoard());
            MainFrame.getMainFrame().switchTo("Prepare2");
        });

        add(backButton);
        add(continueButton);
        add(classicButton);
        add(barrierButton);
        add(itemButton);
        add(jLabel);

        //repaint();

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
    private static void initializeIconButton(int x, int y, JButton button, ImageIcon imageIcon) {
        button.setBounds(x, y, 125, 77);
        button.setRolloverIcon(imageIcon);
        button.setPressedIcon(imageIcon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }
}
