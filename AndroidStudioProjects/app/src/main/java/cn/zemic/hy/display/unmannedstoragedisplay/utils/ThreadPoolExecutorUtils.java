package cn.zemic.hy.display.unmannedstoragedisplay.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author google
 */
public class ThreadPoolExecutorUtils {

    private static class Handler {
        private static ExecutorService mExecutorService = new ThreadPoolExecutor(
                5,
                200,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                new ThreadFactoryBuilder().setNameFormat("voice-speak-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static ExecutorService getInstance() {
        return Handler.mExecutorService;
    }
}
