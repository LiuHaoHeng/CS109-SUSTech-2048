package Main;

import Interface.GameBoard;
import Interface.GameModel;
import Interface.MainFrame;
import Setting.Settings;

import java.io.File;
import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class User implements Serializable {

    public static User currentUser;
    private String uid;
    private String password;
    private int key;
    private int[] best5;

    private boolean isNoGuest = true;

    private transient GameBoard gameBoard;
    private GameModel gameModel;


    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.gameModel = gameBoard.getGameModel();
    }

    private Settings settings;

    static Random random = new Random();


    public User(String uid, String password, boolean isNoGuest) {
        this.uid = uid;
        this.isNoGuest = isNoGuest;
        this.settings = new Settings();
        this.gameBoard = null;
        if (isNoGuest) {
            this.best5 = new int[]{0, 0, 0, 0, 0};
            this.key = randomKey(password);
            this.password = encode(password);

            //保存当前用户信息
            try {
                this.save();
            } catch (IOException ignore) {
                //todo:
            }

            //更新用户列表
            File info = new File("account\\accounts.ser");
            try (FileInputStream fileInputStream = new FileInputStream(info);
                 ObjectInputStream ois = new ObjectInputStream(fileInputStream)) {
                ArrayList<String> users = (ArrayList<String>) ois.readObject();
                users.add(this.uid);
                ois.close();

                ObjectOutputStream oos = null;
                FileOutputStream fileOutputStream = new FileOutputStream(info);
                oos = new ObjectOutputStream(fileOutputStream);
                oos.writeObject(users);
                oos.close();
            } catch (IOException | ClassNotFoundException e) {
                try {
                    ArrayList<String> users = new ArrayList<>();
                    users.add(this.uid);
                    ObjectOutputStream oos;
                    FileOutputStream fileOutputStream;
                    fileOutputStream = new FileOutputStream(info);
                    oos = new ObjectOutputStream(fileOutputStream);
                    oos.writeObject(users);
                    oos.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
//        System.out.println(this.password);
//        System.out.println(this.decode(this.password,this.key));
    }

    private int randomKey(String password) {
        int k;
        while (true) {
            k = random.nextInt(-password.length(), password.length());
            if (Math.abs(k) >= password.length() / 3)
                return k;
        }
    }

    private String encode(String plain) {
        StringBuilder cypher = new StringBuilder();
        int p = this.key;
        for (int i = 0; i < plain.length(); i++) {
            cypher.append((char) (plain.charAt(i) + p));
            p = p - 1 == 0 ? key : p - 1;
        }
        return cypher.reverse().toString();
    }

    private String decode(String cypher, int key) {
        StringBuilder plain = new StringBuilder();
        int p = key;
        for (int i = cypher.length() - 1; i >= 0; i--) {
            plain.append((char) (cypher.charAt(i) - p));
            p = p - 1 == 0 ? key : p - 1;
        }
        return plain.toString();
    }

    public void save() throws IOException {
        //System.out.println(System.getProperty("user.dir"));
        //新建用户目录
        File accountPath = new File("account\\" + uid);
        accountPath.mkdirs();

        //把用户信息存下
        File info = new File("account\\" + uid + "\\info.ser");
        FileOutputStream fileOutputStream = new FileOutputStream(info);
        ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
        oos.writeObject(this);
        oos.close();
    }

    //todo: 用用户list来判断账户情况
    public static int checkAccount(String uid, String password) {
        if (uid.isEmpty()) return -1;
        File info = new File("account\\" + uid + "\\info.ser");
        if (!info.exists()) {
            return 0;//账号不存在
        } else {
            try (FileInputStream fileInputStream = new FileInputStream(info);
                 ObjectInputStream ois = new ObjectInputStream(fileInputStream)) {
                User user = (User) ois.readObject();
                if (user.decode(user.password, user.key).equals(password)) {
                    currentUser = user;
                    return 3;//密码正确
                } else {
                    return 2;//密码错误
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return 1;//文件损毁
            }
        }
    }

    //todo: load变得更现代化
    public static void load(String uid, String password) throws IOException, ClassNotFoundException {
        File info = new File("account\\" + uid + "\\info.ser");
        if (!info.exists()) {
            throw new FileNotFoundException("User data not found for UID: " + uid);
        }
        try (FileInputStream fileInputStream = new FileInputStream(info);
             ObjectInputStream ois = new ObjectInputStream(fileInputStream)) {
            User user = (User) ois.readObject();
            if (user.decode(user.password, user.key).equals(password)) {
                System.out.println(("OK"));
            } else {
                System.out.println("no");
            }
            currentUser = user;
        } catch (IOException e) {
            System.out.println("文件损毁");
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }


    public boolean isNoGuest() {
        return isNoGuest;
    }


    public String getUid() {
        return uid;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            in.defaultReadObject();
        } catch (Exception ignored) {

        }
        if (gameBoard == null) {
            gameBoard = new GameBoard();
            if (gameModel != null) {
                gameBoard.setSize(gameModel.getSize());
                gameBoard.setGameModel(gameModel);
                System.out.println(gameModel);
            }
            MainFrame.getMainFrame().setNowGameBoard(gameBoard);
        }
        // Debugging: Print out the values of each member variable to check for nulls
        System.out.println("User ID: " + uid);
        System.out.println("Password: " + (password != null ? "exists" : "null"));
        System.out.println("Key: " + key);
        System.out.println("Best5: " + (best5 != null ? "exists" : "null"));
        System.out.println("isNoGuest: " + isNoGuest);
        System.out.println("GameBoard: " + (gameBoard != null ? "exists" : "null"));
        System.out.println("Settings: " + (settings != null ? "exists" : "null"));
        System.out.println("GameModel: " + ((gameModel != null) ? "exists" : "null"));
        if (gameModel != null) {
            System.out.println("time: " + gameModel.getElapsedSeconds());
            for (int i = 0; i < gameModel.getSize(); i++) {
                System.out.printf("%d ", gameModel.getBoardAt(0, i));
            }
            System.out.println();
        }
    }
}
