package cn.zemic.hy.display.unmannedstoragedisplay.view.activity.alarmactivity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.gongwen.marqueen.SimpleMF;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.zemic.hy.display.unmannedstoragedisplay.R;
import cn.zemic.hy.display.unmannedstoragedisplay.adapter.BaseRecycleViewAdapter;
import cn.zemic.hy.display.unmannedstoragedisplay.databinding.ActivityMainBinding;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.imp.alarm.AlarmPresenter;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.imp.alarm.AlarmRepository;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IAlarmPresenter;
import cn.zemic.hy.display.unmannedstoragedisplay.utils.DateUtils;
import cn.zemic.hy.display.unmannedstoragedisplay.utils.ThreadPoolExecutorUtils;
import cn.zemic.hy.display.unmannedstoragedisplay.utils.UniquenessIdFactory;
import cn.zemic.hy.display.unmannedstoragedisplay.utils.VoiceSpeakUtils;
import cn.zemic.hy.display.unmannedstoragedisplay.view.activity.BaseActivity;
import cn.zemic.hy.display.unmannedstoragedisplay.view.widget.CustomToast;

/**
 * @author fxs
 */
public class AlarmMainActivity extends BaseActivity implements IAlarmView, BaseRecycleViewAdapter.OnRecycleViewItemClickListener {


    private BaseRecycleViewAdapter adapter;
    private ActivityMainBinding binding;
    private IAlarmPresenter mAlarmPresenter;
    private List<String> voices;
    private StopAnimateHandler stopAnimateHandler;
    private int second;
    private int minute;
    private int hour;
    private List<String> dataForWave;
    private VoiceSpeakUtils voiceSpeakUtils;

    @Override
    public void onStart() {
        super.onStart();
        binding.mvGreetUser.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mvGreetUser.stopFlipping();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        Date date = new Date();
        minute = date.getMinutes();
        hour = date.getHours();
        binding = DataBindingUtil.setContentView(AlarmMainActivity.this, R.layout.activity_main);
        dataForWave = new ArrayList<>();
        voiceSpeakUtils = new VoiceSpeakUtils(this);
        //play voice
        voices = Collections.synchronizedList(new ArrayList<>());
        stopAnimateHandler = new StopAnimateHandler();
        voices.add("welcome");
        voiceSpeakUtils.speak(voices);
        //设置日期
        binding.tvYearMonth.setText(DateUtils.getSysDate());
        binding.tvWeek.setText(DateUtils.getWeek());
        ThreadPoolExecutorUtils.getInstance().execute(this::run);
        //获取仓库号与告警信息
        mAlarmPresenter = new AlarmPresenter(this, new AlarmRepository());
        mAlarmPresenter.getWareHouseNoAndWarningInfo(UniquenessIdFactory.getId(this));
    }

    @Override
    public void showError(String error) {
        CustomToast.showLong(this, error);
    }

    @Override
    public void showWarning(List<OperateWarningInformViewModel> alarmInfo, boolean isFirst) {
        if (isFirst) {
            binding.clTable.setVisibility(View.GONE);
            binding.rvWarning.setVisibility(View.VISIBLE);
            adapter = new BaseRecycleViewAdapter<>(this, alarmInfo, R.layout.item_recycleview_alarm, BR.VM, getItemHeight());
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
            adapter.setRecycleViewItemClickListener(this);
            binding.rvWarning.setLayoutManager(layoutManager);
            binding.rvWarning.setHasFixedSize(true);
            binding.rvWarning.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    private int getItemHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        float density = dm.density;
        // 屏幕密度（每寸像素：120/160/240/320）
//        float densityDPI = dm.densityDpi;
//        float xdpi = dm.xdpi;
//        float ydpi = dm.ydpi;
//        // 屏幕宽（dip，如：320dip）
//        int screenWidthDip = dm.widthPixels;
//        // 屏幕高（dip，如：533dip）
//        int screenHeightDip = dm.heightPixels;
//        // 屏幕宽（px，如：480px）
//        int screenWidth = (int) (dm.widthPixels * density + 0.5f);
        // 屏幕高（px，如：800px）
        int screenHeight = (int) (dm.heightPixels * density + 0.5f);
        int marginTopAndBottom = (int) (70 * density + 0.5f);
        return (screenHeight - marginTopAndBottom) / 10;
    }

    @Override
    public void showLoading() {
        binding.avLoadingView.setIndicator("BallSpinFadeLoaderIndicator");
        binding.avLoadingView.smoothToShow();
    }

    @Override
    public void hideLoading() {
        binding.avLoadingView.smoothToHide();
    }

    @Override
    public void showDetailInfo(OperateWarningInformViewModel alarmInfoBean) {

    }

    @Override
    public void showGreetForUser(UserInViewModel user, String warehouseNo, boolean isOpenDoor) {
        voices.clear();
        dataForWave.clear();
        binding.mvGreetUser.stopFlipping();
        SimpleMF<String> marqueeFactory = new SimpleMF<>(this);
        if (isOpenDoor) {
            voices.add("welcome");
            voiceSpeakUtils.speak(voices);
            dataForWave.add(String.format("%s,欢迎进入%s仓库", user.getUserName(), warehouseNo));
            marqueeFactory.setData(dataForWave);
        } else {
            if (TextUtils.isEmpty(user.getUserName())) {
                dataForWave.add(String.format("%s该用户未注册系统，请联系管理员注册", user.getUserNo()));
                marqueeFactory.setData(dataForWave);
            } else {
                dataForWave.add(String.format("%s没有进入仓库%s的权限", user.getUserName(), warehouseNo));
                marqueeFactory.setData(dataForWave);
            }
        }
        binding.mvGreetUser.setMarqueeFactory(marqueeFactory);
        binding.mvGreetUser.startFlipping();
        ThreadPoolExecutorUtils.getInstance().execute(() -> {
            try {
                Thread.sleep(4300);
                Message message = Message.obtain();
                message.obj = AlarmMainActivity.this;
                message.what = 100;
                stopAnimateHandler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(ViewDataBinding dataBinding, int position) {
        mAlarmPresenter.loadDetailWarningInfo(position);
    }

    @Override
    public void onLongItemClick(ViewDataBinding dataBinding, int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void run() {
        while (true) {
            updateTimer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void updateTimer() {
        ++second;
        int sixty = 60;
        if (second >= sixty) {
            ++minute;
            second -= 60;
            if (minute >= sixty) {
                ++hour;
                minute -= 60;
                if (hour >= 24) {
                    hour -= 24;
                    runOnUiThread(() -> {
                        binding.tvYearMonth.setText(DateUtils.getSysDate());
                        binding.tvWeek.setText(DateUtils.getWeek());
                    });
                }
            }
        }
        runOnUiThread(() -> {
            String secondStr = second < 10 ? String.format(Locale.CHINA, "0%d", second) : String.valueOf(AlarmMainActivity.this.second);
            String minuteStr = minute < 10 ? String.format(Locale.CHINA, "0%d", minute) : String.valueOf(AlarmMainActivity.this.minute);
            String hoursStr = hour < 10 ? String.format(Locale.CHINA, "0%d", hour) : String.valueOf(AlarmMainActivity.this.hour);
            String ticks = String.format("%s:%s:%s", hoursStr, minuteStr, secondStr);
            AlarmMainActivity.this.binding.tvTime.setText(ticks);
        });
    }

    public static class StopAnimateHandler extends Handler {

        private AlarmMainActivity context;

        @Override
        public void handleMessage(Message msg) {
            WeakReference<AlarmMainActivity> weakReference = new WeakReference<>((AlarmMainActivity) msg.obj);
            context = weakReference.get();
            if (null == context) {
                return;
            }
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    final List<String> data = new ArrayList<>();
                    SimpleMF<String> marqueeFactory = new SimpleMF<>(context);
                    marqueeFactory.setData(data);
                    context.binding.mvGreetUser.setMarqueeFactory(marqueeFactory);
                    context.binding.mvGreetUser.startFlipping();
                    context.binding.mvGreetUser.stopFlipping();
                    break;
                default:
                    break;
            }
        }
    }
}
