package cn.zemic.hy.display.unmannedstoragedisplay.view.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.zemic.hy.display.unmannedstoragedisplay.R;

/**
 * @author fxs
 */
public class CustomToast {
    /**
     * display toast==LENGTH_SHORT
     *
     * @param msg message that you want show
     */
    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * display toast==LENGTH_LONG
     *
     * @param msg message that you want show
     */
    public static void showLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }


    private static void show(Context context, String massage, int showLengthOfTime) {
        //using layoutInflater inflater custom layout made by yourself
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null, false);
        //fetch TextView
        TextView title = view.findViewById(R.id.tv_toast);
        //set context message you want
        title.setText(massage);
        Toast toast = new Toast(context);
        //set toast location
        toast.setGravity(Gravity.BOTTOM, toast.getXOffset() / 2, toast.getYOffset() / 2);
        //set time length
        toast.setDuration(showLengthOfTime);
        toast.setView(view);
        toast.show();
    }
}
