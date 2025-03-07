package Interface;

import AI.AI2048;
import Setting.Fonts;
import Setting.ImageIcons;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import static AI.AI2048.*;

public class GameBoard extends JComponent {


    public GameModel getGameModel() {
        return gameModel;
    }

    private GameModel gameModel;

    protected int size;

    protected final int BOARD_SIZE;
    protected int GRID_SIZE;
    protected Block[][] board;

    private final JLabel timerLabel;
    private final Timer timer;
    private int elapsedSeconds;

    private final JLabel pointLabel;
    private final JLabel stepLabel;
    private final JButton upButton;
    private final JButton downButton;
    private final JButton leftButton;
    private final JButton rightButton;

    public GameBoard() {
        gameModel = new GameModel();

        AI2048.initTables();

        this.BOARD_SIZE = 500;
        this.setFocusable(true);
        this.setLayout(null);

        timerLabel = new JLabel("Time: 0");
        timerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        timerLabel.setBounds(24, 855, 255, 52);
        timerLabel.setFont(Fonts.NORMAL_TITLE.font);
        add(timerLabel);

        elapsedSeconds = gameModel.getElapsedSeconds();
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            gameModel.setElapsedSeconds(elapsedSeconds);
            timerLabel.setText("Time: " + elapsedSeconds);
            if (elapsedSeconds % 30 == 0 && elapsedSeconds != 0) {
                if (MainFrame.getMainFrame().getNowUser().isNoGuest()) {
                    try {
                        MainFrame.getMainFrame().getNowUser().setSettings(MainFrame.getMainFrame().getNowSettings());
                        MainFrame.getMainFrame().getNowUser().save();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JDialog jDialog = new JDialog(MainFrame.getMainFrame(), "Reminder", false);
                    jDialog.setLocationRelativeTo(MainFrame.getMainFrame());
                    jDialog.setSize(200, 100);
                    JLabel jLabel = new JLabel("Saved", SwingConstants.CENTER);
                    jDialog.add(jLabel);
                    jDialog.setVisible(true);
                }
            }
        });

        // 添加 AncestorListener 监听面板是否被显示
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                // 面板被添加到显示的容器时开始计时
                startTimer();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                // 面板从显示的容器中移除时停止计时
                stopTimer();
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                // 不需要处理
            }
        });

        pointLabel = new JLabel("point:" + gameModel.getPoint());
        pointLabel.setHorizontalAlignment(SwingConstants.LEFT);
        pointLabel.setBounds(24, 126, 300, 52);
        pointLabel.setFont(Fonts.NORMAL_TITLE.font);

        stepLabel = new JLabel("step:" + gameModel.getStep());
        stepLabel.setHorizontalAlignment(SwingConstants.LEFT);
        stepLabel.setBounds(582, 126, 255, 52);
        stepLabel.setFont(Fonts.NORMAL_TITLE.font);

        JButton backButton = new JButton(ImageIcons.backImageDefault);
        initializeIconButton(24, 26, backButton, ImageIcons.backImagePressed);
        backButton.setBounds(24, 26, 135, 94);

        ImageIcon saveButtonIcon = new ImageIcon(new ImageIcon("Images\\save1.png").getImage().
                getScaledInstance(77, 77, Image.SCALE_SMOOTH)),
                saveButtonIconPressed = new ImageIcon(new ImageIcon("Images\\save2.png").getImage().
                        getScaledInstance(77, 77, Image.SCALE_SMOOTH)),
                initialButtonIcon = new ImageIcon(new ImageIcon("Images\\initial1.png").getImage().
                        getScaledInstance(77, 77, Image.SCALE_SMOOTH)),
                initialButtonIconPressed = new ImageIcon(new ImageIcon("Images\\initial2.png").getImage().
                        getScaledInstance(77, 77, Image.SCALE_SMOOTH)),
                returnButtonIcon = new ImageIcon(new ImageIcon("Images\\last1.png").getImage().
                        getScaledInstance(77, 77, Image.SCALE_SMOOTH)),
                returnButtonIconPressed = new ImageIcon(new ImageIcon("Images\\last2.png").getImage().
                        getScaledInstance(77, 77, Image.SCALE_SMOOTH));

        JButton saveButton = new JButton(saveButtonIcon);
        JButton initialButton = new JButton(initialButtonIcon);
        JButton returnButton = new JButton(returnButtonIcon);
        JButton reviewButton = new JButton("AI");

        initializeIconButton(201, 26, saveButton, saveButtonIconPressed);
        initializeIconButton(380, 26, initialButton, initialButtonIconPressed);
        initializeIconButton(560, 26, returnButton, returnButtonIconPressed);
        reviewButton.setBounds(736, 26, 77, 77);

        upButton = getTextjButton(380, 177, 77, 77, "↑");
        downButton = getTextjButton(380, 820, 77, 77, "↓");
        leftButton = getTextjButton(58, 499, 77, 77, "←");
        rightButton = getTextjButton(705, 499, 77, 77, "→");

        upButton.addActionListener(e -> {
            int p = gameModel.toUp();
            MainFrame.getMainFrame().requestFocusInWindow();
            repaint();
            checkConsequence(p);
        });
        downButton.addActionListener(e -> {
            int p = gameModel.toDown();
            MainFrame.getMainFrame().requestFocusInWindow();
            repaint();
            checkConsequence(p);
        });
        leftButton.addActionListener(e -> {
            int p = gameModel.toLeft();
            MainFrame.getMainFrame().requestFocusInWindow();
            repaint();
            checkConsequence(p);
        });
        rightButton.addActionListener(e -> {
            int p = gameModel.toRight();
            MainFrame.getMainFrame().requestFocusInWindow();
            repaint();
            checkConsequence(p);
        });

        backButton.addActionListener(e -> {
            if (MainFrame.getMainFrame().getNowUser().isNoGuest()) {
                try {
                    MainFrame.getMainFrame().getNowUser().setSettings(MainFrame.getMainFrame().getNowSettings());
                    MainFrame.getMainFrame().getNowUser().save();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "已自动保存", "Reminder", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "游客模式无法自动保存", "Reminder", JOptionPane.PLAIN_MESSAGE);
            }
            MainFrame.getMainFrame().switchTo("Prepare1");
        });

        saveButton.addActionListener(e -> {
            if (MainFrame.getMainFrame().getNowUser().isNoGuest()) {
                try {
                    MainFrame.getMainFrame().getNowUser().setSettings(MainFrame.getMainFrame().getNowSettings());
                    MainFrame.getMainFrame().getNowUser().save();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "已手动保存", "Reminder", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "游客模式无法保存", "Reminder", JOptionPane.PLAIN_MESSAGE);
            }
            repaint();
        });

        initialButton.addActionListener(e -> {
            gameModel.initialize();
            repaint();
            MainFrame.getMainFrame().requestFocusInWindow();
        });

        returnButton.addActionListener(e -> {
            gameModel.loadByStack();
            repaint();
            MainFrame.getMainFrame().requestFocusInWindow();
        });

        reviewButton.addActionListener(e -> {
            if (size == 4 && !gameModel.isWin()) {
                long board = 0;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        board |= (long) this.board[i][j].getVal() << 4 * (i * 4 + j);
                    }
                }

                int move;

                for (move = 0; move < 4; move++) {
                    System.out.println("checked " + move);
                    if (executeMove(move, board) != board) break;
                }

                move = findBestMove(board);

                switch (move) {
                    case 0 -> upButton.doClick();
                    case 1 -> downButton.doClick();
                    case 2 -> leftButton.doClick();
                    case 3 -> rightButton.doClick();
                }
            }

        });

        add(upButton);
        add(downButton);
        add(leftButton);
        add(rightButton);

        add(backButton);
        add(saveButton);
        add(initialButton);
        add(returnButton);
        add(reviewButton);

        add(stepLabel);
        add(pointLabel);

        // Hint: 以下部分是为了键盘操作
        MainFrame.getMainFrame().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> leftButton.doClick();
                    case KeyEvent.VK_RIGHT -> rightButton.doClick();
                    case KeyEvent.VK_DOWN -> downButton.doClick();
                    case KeyEvent.VK_UP -> upButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        MainFrame.getMainFrame().setFocusable(true);
        MainFrame.getMainFrame().requestFocusInWindow();
    }

    private void checkConsequence(int i) {
        switch (i) {
            case 1:
                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "You Win!", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "You Lose!", "Pity!", JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

    public void setSize(int size) {
        this.size = size;
        this.GRID_SIZE = this.BOARD_SIZE / this.size;
        this.board = new Block[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                board[i][j] = new Block(MainFrame.getMainFrame(), 0, this.GRID_SIZE);
                board[i][j].setLocation(169 + j * GRID_SIZE, 280 + i * GRID_SIZE);
                this.add(board[i][j]);
            }
        }
        gameModel.setSize(size);
        gameModel.initialize();
    }

    private static JButton getTextjButton(int x, int y, int w, int h, String text) {
        JButton jButton = new JButton(text);
        jButton.setBounds(x, y, w, h);
        jButton.setFont(Fonts.NORMAL_THICK.font);
        return jButton;
    }

    public void startTimer() {
        // 重置计时器
        elapsedSeconds = gameModel.getElapsedSeconds();
        timerLabel.setText("Time: " + elapsedSeconds);
        // 开始计时
        timer.start();
    }

    public void stopTimer() {
        // 停止计时
        timer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = MainFrame.getMainFrame().getNowSettings().getBackground().getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void repaint() {
        this.pointLabel.setText("point: " + gameModel.getPoint());
        this.stepLabel.setText("step: " + gameModel.getStep());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j].setVal(gameModel.getBoardAt(i, j));
            }
        }
        super.repaint();

    }

    private static void initializeIconButton(int x, int y, JButton button, ImageIcon imageIcon) {
        button.setBounds(x, y, 77, 77);
        button.setRolloverIcon(imageIcon);
        button.setPressedIcon(imageIcon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }
}
