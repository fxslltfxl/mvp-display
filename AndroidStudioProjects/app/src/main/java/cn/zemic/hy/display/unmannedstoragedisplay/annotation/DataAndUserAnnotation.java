package cn.zemic.hy.display.unmannedstoragedisplay.annotation;

import android.support.annotation.IntDef;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author fxs
 */
@Deprecated
public class DataAndUserAnnotation {

    private static final int USER_CHANGED = 2;
    private static final int DATA_CHANGED = 1;
    private static final int DATA_ERROR = 0;

    public DataAndUserAnnotation() throws MalformedURLException {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @IntDef({USER_CHANGED, DATA_ERROR, DATA_CHANGED})
    public @interface DataOrUserAnnotation {
    }

    private URL url = new URL("https://www.baidu.com");

    public void getInputStream() throws IOException {
        InputStream inputStream = url.openStream();
    }
}
