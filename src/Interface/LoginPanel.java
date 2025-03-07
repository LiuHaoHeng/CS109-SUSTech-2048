package Interface;

import Interface.TextFieldWithTips.JPassFieldWithPlaceholder;
import Interface.TextFieldWithTips.JTextFieldWithPlaceholder;
import Main.User;
import Setting.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//todo: 绝对化坐标+设置按钮
public class LoginPanel extends JPanel {
    JTextFieldWithPlaceholder userText;
    JPassFieldWithPlaceholder passText;

    public LoginPanel() {
        setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("2048 Game");
        titleLabel.setFont(Fonts.TITLE.font);

        ImageIcon icon = new ImageIcon(new ImageIcon("Images\\Icon.png").getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH));
        JLabel titleImage = new JLabel(icon);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Fonts.NORMAL_THICK.font);

        userText = new JTextFieldWithPlaceholder("输入用户名");
        userText.setHorizontalAlignment(JTextFieldWithPlaceholder.CENTER);
        userText.setColumns(20);
        userText.setFont(Fonts.NORMAL_THIN.font);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Fonts.NORMAL_THICK.font);

        passText = new JPassFieldWithPlaceholder("输入密码");
        passText.setHorizontalAlignment(JTextFieldWithPlaceholder.CENTER);
        passText.setColumns(20);
        passText.setFont(Fonts.NORMAL_THIN.font);
        passText.setEchoChar((char) 0x25CF);


        ImageIcon loginButtonDefault = new ImageIcon((new ImageIcon("Images\\LoginButton1.png")).getImage().getScaledInstance(200, 155, Image.SCALE_SMOOTH));
        ImageIcon loginButtonPressed = new ImageIcon((new ImageIcon("Images\\LoginButton2.png")).getImage().getScaledInstance(200, 155, Image.SCALE_SMOOTH));
        JButton loginButton = new JButton(loginButtonDefault);
        initializeIconButton(loginButton, loginButtonPressed);
        loginButton.requestFocusInWindow();

        ImageIcon guestIcon1 = new ImageIcon(((new ImageIcon("Images\\GuestButton1.png")).getImage().getScaledInstance(133, 74, Image.SCALE_SMOOTH)));
        ImageIcon guestIcon2 = new ImageIcon(((new ImageIcon("Images\\GuestButton2.png")).getImage().getScaledInstance(133, 74, Image.SCALE_SMOOTH)));
        JButton guestButton = new JButton(guestIcon1);
        initializeIconButton(guestButton, guestIcon2);

        ImageIcon registerIcon1 = new ImageIcon(((new ImageIcon("Images\\RegisterButton1.png")).getImage().getScaledInstance(179, 74, Image.SCALE_SMOOTH)));
        ImageIcon registerIcon2 = new ImageIcon(((new ImageIcon("Images\\RegisterButton2.png")).getImage().getScaledInstance(179, 74, Image.SCALE_SMOOTH)));
        JButton registerButton = new JButton(registerIcon1);
        initializeIconButton(registerButton, registerIcon2);

        JButton invisibleButton = new JButton();
        invisibleButton.setPreferredSize(new Dimension(0, 0)); // 设置按钮为不可见
        invisibleButton.setVisible(true); // 隐藏按钮


        SwingUtilities.invokeLater(loginButton::requestFocusInWindow);


        loginButton.addActionListener(e -> {
            //todo: 更加复杂的错误判定
            switch (User.checkAccount(userText.getText(), new String(passText.getPassword()))) {
                case -1 ->
                        JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "请输入账号", "Error", JOptionPane.ERROR_MESSAGE);
                case 0 ->
                        JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "账号未注册或文件丢失", "Error", JOptionPane.ERROR_MESSAGE);

                case 1 ->
                        JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "账号文件损毁，请重新创建", "Error", JOptionPane.ERROR_MESSAGE);

                case 2 ->
                        JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "密码错误", "Error", JOptionPane.ERROR_MESSAGE);

                case 3 -> {

                    userText.setText("");
                    passText.setText("");

                    MainFrame.getMainFrame().setNowSettings(User.currentUser.getSettings());
                    MainFrame.getMainFrame().setNowUser(User.currentUser);

                    MainFrame.getMainFrame().switchTo("Prepare1");
                    //MainFrame.getMainFrame().autoSaveTimer.start();

                }
            }
        });
        registerButton.addActionListener(e -> MainFrame.getMainFrame().switchTo("Register"));
        guestButton.addActionListener(e -> {
            MainFrame.getMainFrame().setNowUser(new User("", "", false));
            //MainFrame.getMainFrame().autoSaveTimer.start();
            MainFrame.getMainFrame().switchTo("Prepare1");
        });
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                loginButton.doClick();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        };
        userText.addActionListener(e -> passText.requestFocusInWindow());
        passText.addActionListener(e -> loginButton.doClick());
        loginButton.addKeyListener(keyListener);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTH;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 2;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.weighty = 3;
        gbc.gridy = 2;
        add(titleImage, gbc);

        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.weighty = 3;
        gbc.insets = new Insets(0, 5, 0, 5);
        add(userLabel, gbc);

        gbc.gridx = 1;
        add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(passLabel, gbc);

        gbc.weighty = 2;
        gbc.gridx = 1;
        add(passText, gbc);

        gbc.weighty = 0.8;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        add(registerButton, gbc);
        gbc.gridx = 1;
        add(guestButton, gbc);

        gbc.weighty = 10;
        gbc.gridy = 11;
        add(invisibleButton, gbc);

//        gbc.weighty=0;
//        gbc.weightx=0;
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.gridwidth = 1;
//        gbc.gridheight = 1;
//        gbc.anchor = GridBagConstraints.NORTHEAST; // 设置按钮在单元格右上角
//        JButton smallButton = new JButton("?");
//        smallButton.setPreferredSize(new Dimension(40, 40)); // 设置按钮大小
//        add(smallButton, gbc);
    }

    //当你创建一个只显示icon的按钮使用
    private static void initializeIconButton(JButton button, ImageIcon imageIcon) {
        button.setRolloverIcon(imageIcon);
        button.setPressedIcon(imageIcon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = MainFrame.getMainFrame().getNowSettings().getBackground().getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}