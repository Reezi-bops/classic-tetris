import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private Clip clip;

    public void play(String filePath, boolean loop, float volume) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Set volume
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Audio error: " + e.getMessage());
        }
    }

    public void fadeOut(int ms) {
        if (clip != null && clip.isRunning()) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            new Thread(() -> {
                float range = volume.getMaximum() - volume.getMinimum();
                float step = range / 50; // 50 steps fade
                for (int i = 0; i < 50; i++) {
                    volume.setValue(volume.getMaximum() - i * step);
                    try {
                        Thread.sleep(ms / 50);
                    } catch (InterruptedException ignored) {}
                }
                clip.stop();
            }).start();
        }
    }

    public void playOnce(String filePath, float volume) {
        new Thread(() -> {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filePath));
                Clip oneShot = AudioSystem.getClip();
                oneShot.open(stream);

                // Set volume
                if (oneShot.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) oneShot.getControl(FloatControl.Type.MASTER_GAIN);
                    float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                    gainControl.setValue(dB);
                }

                oneShot.start();
            } catch (Exception e) {
                System.err.println("Sound effect error: " + e.getMessage());
            }
        }).start();
    }
}
