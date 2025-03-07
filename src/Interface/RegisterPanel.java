package Interface;

import Interface.TextFieldWithTips.JPassFieldWithPlaceholder;
import Interface.TextFieldWithTips.JTextFieldWithPlaceholder;
import Main.User;
import Setting.Fonts;
import Setting.ImageIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RegisterPanel extends JPanel {
    
    public RegisterPanel() {
        setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("2048 Game");
        titleLabel.setFont(Fonts.TITLE.font);

        ImageIcon icon = new ImageIcon(new ImageIcon("Images\\Icon.png").getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH));
        JLabel titleImage = new JLabel(icon);

        //Font
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Fonts.NORMAL_THICK.font);

        JTextFieldWithPlaceholder userText = new JTextFieldWithPlaceholder("输入用户名");
        userText.setHorizontalAlignment(JTextFieldWithPlaceholder.CENTER);
        userText.setColumns(20);
        userText.setFont(Fonts.NORMAL_THIN.font);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Fonts.NORMAL_THICK.font);

        JPassFieldWithPlaceholder passText = new JPassFieldWithPlaceholder("输入密码(8-16位)");
        passText.setHorizontalAlignment(JTextFieldWithPlaceholder.CENTER);
        passText.setColumns(20);
        passText.setFont(Fonts.NORMAL_THIN.font);
        passText.setEchoChar((char) 0x25CF);

        JLabel confirmLabel = new JLabel("Confirm");
        confirmLabel.setFont(Fonts.NORMAL_THICK.font);

        JPassFieldWithPlaceholder confirmText = new JPassFieldWithPlaceholder("确认密码");
        confirmText.setHorizontalAlignment(JTextFieldWithPlaceholder.CENTER);
        confirmText.setColumns(20);
        confirmText.setFont(Fonts.NORMAL_THIN.font);
        confirmText.setEchoChar((char) 0x25CF);

        ImageIcon registerButtonDefault = new ImageIcon((new ImageIcon("Images\\RegisterButton1.png")).getImage().getScaledInstance(214, 89, Image.SCALE_SMOOTH));
        ImageIcon registerButtonPressed = new ImageIcon((new ImageIcon("Images\\RegisterButton2.png")).getImage().getScaledInstance(214, 89, Image.SCALE_SMOOTH));

        JButton registerButton = new JButton(registerButtonDefault);
        initializeIconButton(registerButton, registerButtonPressed);

        JButton backButton = new JButton(ImageIcons.backImageDefault);
        initializeIconButton(backButton, ImageIcons.backImagePressed);

        JButton invisibleButton = new JButton();
        invisibleButton.setPreferredSize(new Dimension(0, 0)); // 设置按钮为不可见
        invisibleButton.setVisible(true); // 隐藏按钮


        SwingUtilities.invokeLater(registerButton::requestFocusInWindow);

        registerButton.addActionListener(e -> {
            if (User.checkAccount(userText.getText(), "") == 0) {
                if ((new String(passText.getPassword())).equals(new String(confirmText.getPassword()))) {
                    if ((new String(passText.getPassword())).length() >= 8 && (new String(passText.getPassword())).length() <= 16) {
                        (new User(userText.getText(), new String(passText.getPassword()), true)).setSettings(MainFrame.getMainFrame().getNowSettings());
                        MainFrame.getMainFrame().getLoginPanel().userText.setText(userText.getText());
                        MainFrame.getMainFrame().getLoginPanel().passText.setText(new String(confirmText.getPassword()));
                        userText.setText("");
                        passText.setText("");
                        confirmText.setText("");
                        JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "注册成功", "Reminder", JOptionPane.PLAIN_MESSAGE);
                        MainFrame.getMainFrame().switchTo("Login");
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "密码的长度应为8-16位", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "两次输入的密码不一致", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "账号已存在", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        backButton.addActionListener(e -> MainFrame.getMainFrame().switchTo("Login"));

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                registerButton.doClick();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        };
        userText.addActionListener(e -> passText.requestFocusInWindow());
        passText.addActionListener(e -> confirmText.requestFocusInWindow());
        confirmText.addActionListener(e -> registerButton.doClick());
        registerButton.addKeyListener(keyListener);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTH;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.weighty = 0.9;
        gbc.gridy = 2;
        add(titleImage, gbc);

        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.weighty = 2;
        gbc.insets = new Insets(0, 5, 0, 5);
        add(userLabel, gbc);

        gbc.gridx = 1;
        add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(passLabel, gbc);

        gbc.gridx = 1;
        add(passText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(confirmLabel, gbc);

        gbc.gridx = 1;
        add(confirmText, gbc);

        gbc.weighty = 0.8;
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(registerButton, gbc);

        gbc.weighty = 1;
        gbc.gridy = 9;
        add(backButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.weighty = 10;
        add(invisibleButton, gbc);

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

