package Setting;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Music implements Serializable {
    private static Clip backClip;
    private static Clip clip1;
    public static void setupAudio(){
        try {
            AudioInputStream audioIn1 = AudioSystem.getAudioInputStream(new File("music\\backgroundMusic\\C418-Haggstrom.wav"));
            backClip = AudioSystem.getClip();
            backClip.open(audioIn1);
            backClip.loop(Clip.LOOP_CONTINUOUSLY); // 循环播放背景音乐

            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(new File("music\\soundEffect\\orb.wav"));
            clip1 = AudioSystem.getClip();
            clip1.open(audioIn2);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public static void playBackgroundMusic() {
        backClip.start();
    }

    public static void playSound1() {
        clip1.stop();    // 停止任何正在播放的内容
        clip1.setFramePosition(0);
        clip1.start();
    }

    public static void setVolumeBack(float volume) {
        volume/=100.0f;
        FloatControl gainControlBack = (FloatControl) backClip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume == 0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
        gainControlBack.setValue(dB);
    }
    public static void setVolume1(float volume) {
        volume/=100.0f;
        FloatControl gainControl1 = (FloatControl) clip1.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume == 0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
        gainControl1.setValue(dB);
    }
}
