package cn.zemic.hy.display.unmannedstoragedisplay.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * @author fxs
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentView());
        //initialize
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    /**
     * fetch ContentView
     *
     * @return layout
     */
    @LayoutRes
    protected abstract int getContentView();

    /**
     * initialize and do something follow up
     */
    protected abstract void init();
}
