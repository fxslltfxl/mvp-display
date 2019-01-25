package cn.zemic.hy.display.unmannedstoragedisplay.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author fxs
 */
@SuppressWarnings("unused")
public class LogUtils {
    public static String saveLog2File(String tag, String log) {
        StringBuilder sb = new StringBuilder();

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        sb.append(time).append("\n").append(tag).append("\n").append("内容为:").append(log).append("\n").append("\n");


        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String fileTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
            String fileName = fileTime + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = String.format("%s/customlogger/", Environment.getExternalStorageDirectory().getPath());
                File dir = new File(path);
                if (!dir.exists()) {
                    boolean mkdirs = dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                byte[] bytes = sb.toString().getBytes("UTF-8");
                fos.write(bytes);
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
