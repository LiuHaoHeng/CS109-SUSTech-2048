package Interface;

import Main.User;
import Setting.Background;
import Setting.Music;
import Setting.Settings;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    // 使用 volatile 确保多线程环境下的可见性和有序性
    private static volatile MainFrame mainFrame;

    // 提供一个全局访问点，并使用双重检查锁来确保线程安全
    public static MainFrame getMainFrame() {
        if (mainFrame == null) {
            synchronized (MainFrame.class) {
                if (mainFrame == null) {
                    mainFrame = new MainFrame();
                }
            }
        }
        return mainFrame;
    }

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final PreparePanel1 preparePanel1;
    private final PreparePanel2 preparePanel2;

    private User nowUser = null;
    private Settings nowSettings = new Settings();
    private GameBoard nowGameBoard;


//    public Timer autoSaveTimer;

    // 私有构造函数，防止实例化
    private MainFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 创建登录面板和游戏面板
        loginPanel = new LoginPanel();
        registerPanel = new RegisterPanel();
        preparePanel1 = new PreparePanel1();
        preparePanel2 = new PreparePanel2();

        Music.setupAudio();
        Music.playBackgroundMusic();

        ImageIcon icon = new ImageIcon("Images\\Icon.png");
        setIconImage(icon.getImage());

        setResizable(false);

        // 将面板添加到主面板
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");
        mainPanel.add(preparePanel1, "Prepare1");
        mainPanel.add(preparePanel2, "Prepare2");



        //设置菜单栏
        JMenuBar menuBar = new JMenuBar(){
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 20; // 设置菜单栏高度为50像素
                return d;
            }
        };

        //设置菜单栏第一项
        JMenu backMenu = new JMenu("Background");

        JMenuItem mountainBack = new JMenuItem("Mountain");
        backMenu.add(mountainBack);

        JMenuItem cloudBack = new JMenuItem("Cloud");
        backMenu.add(cloudBack);

        JMenuItem cityBack = new JMenuItem("City");
        backMenu.add(cityBack);

        menuBar.add(backMenu);

        mountainBack.addActionListener(e -> {
            this.getNowSettings().setBackground(Background.MOUNT);
            repaint();
        });
        cloudBack.addActionListener(e->{
            this.getNowSettings().setBackground(Background.CLOUD);
            repaint();
        });
        cityBack.addActionListener(e->{
            this.getNowSettings().setBackground(Background.CITY);
            repaint();
        });

        //设置菜单栏第二项
        JMenu tileType = new JMenu("tileType");

        JMenuItem classicType = new JMenuItem("Classic");
        tileType.add(classicType);

        JMenuItem vehicleType = new JMenuItem("Vehicle");
        tileType.add(vehicleType);

        JMenuItem animalType = new JMenuItem("Animal");
        tileType.add(animalType);

        JMenuItem plantType = new JMenuItem("Plant");
        tileType.add(plantType);

        JMenuItem foodType = new JMenuItem("Food");
        tileType.add(foodType);

        menuBar.add(tileType);

        classicType.addActionListener(e -> {
            this.getNowSettings().setType(Setting.Type.Classic);
            repaint();
        });

        plantType.addActionListener(e -> {
            this.getNowSettings().setType(Setting.Type.Plant);
            repaint();
        });

        foodType.addActionListener(e -> {
            this.getNowSettings().setType(Setting.Type.Food);
            repaint();
        });

        animalType.addActionListener(e -> {
            this.getNowSettings().setType(Setting.Type.Animal);
            repaint();
        });

        vehicleType.addActionListener(e -> {
            this.getNowSettings().setType(Setting.Type.Vehicle);
            repaint();
        });

        //设置菜单栏第三项
        JMenu volumeBack = new JMenu("BackgroundVolume");
        JPanel sliderPanelBack = new JPanel();
        sliderPanelBack.setLayout(new BoxLayout(sliderPanelBack, BoxLayout.Y_AXIS));

        JLabel sliderLabelBack = new JLabel("Adjust Volume");
        JSlider sliderBack = new JSlider(0, 100, 50);
        sliderBack.addChangeListener(e -> Music.setVolumeBack(sliderBack.getValue()));

        sliderPanelBack.add(sliderLabelBack);
        sliderPanelBack.add(sliderBack);
        volumeBack.add(sliderPanelBack);

        menuBar.add(volumeBack);

        JMenu volumeSound = new JMenu("SoundEffectVolume");
        JPanel sliderPanel1 = new JPanel();
        sliderPanel1.setLayout(new BoxLayout(sliderPanel1, BoxLayout.Y_AXIS));

        JLabel sliderLabel1 = new JLabel("Adjust Volume");
        JSlider slider1 = new JSlider(0, 100, 50);
        slider1.addChangeListener(e -> Music.setVolume1(slider1.getValue()));

        sliderPanel1.add(sliderLabel1);
        sliderPanel1.add(slider1);
        volumeSound.add(sliderPanel1);

        menuBar.add(volumeSound);

        // 将菜单栏设置到JFrame
        setJMenuBar(menuBar);


        // 将主面板添加到框架
        add(mainPanel);

        // 设置框架属性
        setTitle("2048 Game");
        setSize(837, 958);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//窗口居中

    }

    // 切换到特定面板的方法
    public void switchTo(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = getMainFrame();
//            frame.autoSaveTimer = new Timer(2000, new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    try {
//                        frame.nowUser.save();
//                        JOptionPane.showMessageDialog(frame, "自动保存成功", "Reminder",JOptionPane.PLAIN_MESSAGE);
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            });
            frame.setVisible(true);
        });
    }

    public User getNowUser() {
        return nowUser;
    }

    public void setNowUser(User nowUser) {
        this.nowUser = nowUser;
    }

    public Settings getNowSettings() {
        return nowSettings;
    }

    public void setNowSettings(Settings nowSettings) {
        this.nowSettings = nowSettings;
    }

    public GameBoard getNowGameBoard() {
        return nowGameBoard;
    }

    public void setNowGameBoard(GameBoard nowGameBoard) {
        this.nowGameBoard = nowGameBoard;
        this.mainPanel.add(this.nowGameBoard, "Game");
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public RegisterPanel getRegisterPanel() {
        return registerPanel;
    }
}