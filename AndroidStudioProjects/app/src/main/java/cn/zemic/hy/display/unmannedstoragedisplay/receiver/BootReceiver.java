package cn.zemic.hy.display.unmannedstoragedisplay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.zemic.hy.display.unmannedstoragedisplay.view.activity.alarmactivity.AlarmMainActivity;

/**
 * @author fxs
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent launchIntent = new Intent(context, AlarmMainActivity.class);
        //launch activity by new task
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchIntent);
    }
}
