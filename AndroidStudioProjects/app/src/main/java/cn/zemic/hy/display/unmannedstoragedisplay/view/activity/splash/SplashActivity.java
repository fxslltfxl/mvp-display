package cn.zemic.hy.display.unmannedstoragedisplay.view.activity.splash;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import java.lang.ref.WeakReference;

import cn.zemic.hy.display.unmannedstoragedisplay.R;
import cn.zemic.hy.display.unmannedstoragedisplay.view.activity.BaseActivity;
import cn.zemic.hy.display.unmannedstoragedisplay.view.activity.alarmactivity.AlarmMainActivity;

/**
 * @author fxs
 */
public class SplashActivity extends BaseActivity {

    public static final int MSG_FINISH = 500;

    public JumpHandler mJumpHandler;


    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        mJumpHandler=new JumpHandler(this);
        mJumpHandler.sendEmptyMessageDelayed(MSG_FINISH, 3000);
    }
    private static class JumpHandler extends Handler {
        private final WeakReference<SplashActivity> mSplashActivityWeakReference;

        JumpHandler(SplashActivity activity) {
            this.mSplashActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity splashActivity = mSplashActivityWeakReference.get();
            if (splashActivity == null) {
                return;
            }
            switch (msg.what) {
                case MSG_FINISH:
                    Intent intent = new Intent(splashActivity, AlarmMainActivity.class);
                    splashActivity.startActivity(intent);
                    splashActivity.finish();
                    splashActivity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 取消返回键监听
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
            default:
                break;
        }
        return true;
    }

}
