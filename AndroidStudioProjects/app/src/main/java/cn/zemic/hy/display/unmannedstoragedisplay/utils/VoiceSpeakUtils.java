package cn.zemic.hy.display.unmannedstoragedisplay.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 语音播放工具类
 *
 * @author 0.0
 */
public class VoiceSpeakUtils {
    private ExecutorService mExecutorService;
    private Context context;

    public VoiceSpeakUtils(Context context) {
        this.context = context;
        mExecutorService = ThreadPoolExecutorUtils.getInstance();
    }

    public void speak(final List<String> voiceLists) {
        if (mExecutorService != null) {
            StartThread startThread = new StartThread(voiceLists);
            mExecutorService.execute(startThread);
        }
    }

    private class StartThread extends Thread {
        private final List<String> voices;

        StartThread(final List<String> voices) {
            this.voices = voices;
        }

        @Override
        public synchronized void run() {
            super.run();
            final CountDownLatch latch = new CountDownLatch(1);
            MediaPlayer player = new MediaPlayer();
            if (voices != null && voices.size() > 0) {
                final int[] counter = {0};
                AssetFileDescriptor assetFileDescriptor = null;
                try {
                    assetFileDescriptor = context.getAssets().openFd(String.format("sounds/%s.wav", voices.get(counter[0])));
                    if (assetFileDescriptor == null) {
                        return;
                    }
                    player.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(),
                            assetFileDescriptor.getLength());
                    player.prepareAsync();
                    player.setOnPreparedListener(MediaPlayer::start);
                    player.setOnCompletionListener(mp -> {
                        mp.reset();
                        counter[0]++;
                        if (counter[0] < voices.size()) {
                            try {
                                AssetFileDescriptor fd = context.getAssets().openFd(String.format("sounds/%s.wav", voices.get(counter[0])));
                                if (fd == null) {
                                    return;
                                }
                                mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                                mp.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                                latch.countDown();
                            }
                        } else {
                            mp.release();
                            latch.countDown();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    latch.countDown();
                } finally {
                    if (assetFileDescriptor != null) {
                        try {
                            assetFileDescriptor.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            try {
                latch.await();
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
