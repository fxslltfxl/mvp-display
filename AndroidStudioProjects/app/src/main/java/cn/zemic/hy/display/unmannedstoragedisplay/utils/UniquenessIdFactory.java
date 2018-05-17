package cn.zemic.hy.display.unmannedstoragedisplay.utils;

import android.content.Context;
import android.provider.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * @author fxs
 */
public class UniquenessIdFactory {

    private static String sID = null;

    private static final String INSTALLATION = "INSTALLATION";


    public static String getId(Context context) {

        String androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        if ("".equals(androidId) || "9774d56d682e549c".equals(androidId) || null == androidId) {
            return id(context);
        } else {
            return androidId;
        }
    }

    public synchronized static String id(Context context) {

        if (null == sID) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {

                if (!installation.exists()) {
                    writeInstallationFile(installation);
                }
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return sID;

    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();

        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}

