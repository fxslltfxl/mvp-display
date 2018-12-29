package cn.zemic.hy.display.unmannedstoragedisplay.view.activity.alarmactivity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.zemic.hy.display.unmannedstoragedisplay.R;
import cn.zemic.hy.display.unmannedstoragedisplay.adapter.BaseRecycleViewAdapter;
import cn.zemic.hy.display.unmannedstoragedisplay.adapter.ShelfRecycleViewAdapter;
import cn.zemic.hy.display.unmannedstoragedisplay.adapter.WarnRecycleViewAdapter;
import cn.zemic.hy.display.unmannedstoragedisplay.databinding.ActivityMainNewBinding;
import cn.zemic.hy.display.unmannedstoragedisplay.databinding.OrderCellBinding;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.ShelfState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserDisplayVM;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserOutVM;
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
public class AlarmMainActivity extends BaseActivity implements IAlarmView, WarnRecycleViewAdapter.OnRecycleViewItemClickListener {

    private WarnRecycleViewAdapter adapter;
    private ActivityMainNewBinding binding;
    private IAlarmPresenter mAlarmPresenter;
    private List<String> voices;
    private int second;
    private int minute;
    private int hour;
    private VoiceSpeakUtils voiceSpeakUtils;
    private List<UserDisplayVM> userDisplays;
    private List<ShelfState> shelfStates;
    BaseRecycleViewAdapter<UserDisplayVM, OrderCellBinding> userAdapter;
    ShelfRecycleViewAdapter shelfAdapter;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_new;
    }

    @Override
    protected void init() {
        Date date = new Date();
        minute = date.getMinutes();
        hour = date.getHours();
        binding = DataBindingUtil.setContentView(AlarmMainActivity.this, R.layout.activity_main_new);
        userDisplays = new ArrayList<>();
        shelfStates = new ArrayList<>();
        voiceSpeakUtils = new VoiceSpeakUtils(this);
        //play voice
        voices = Collections.synchronizedList(new ArrayList<>());
        voices.add("welcome");
        voiceSpeakUtils.speak(voices);
        //设置日期
        binding.tvYearMonth.setText(DateUtils.getSysDate());
        binding.tvWeek.setText(DateUtils.getWeek());
        ThreadPoolExecutorUtils.getInstance().execute(this::run);
        //获取仓库号与告警信息
        mAlarmPresenter = new AlarmPresenter(this, new AlarmRepository());
        mAlarmPresenter.getWareHouseNoAndWarningInfo(UniquenessIdFactory.getId(this));


        //
        userDisplays.add(new UserDisplayVM("1", "1", ""));
        userAdapter = new BaseRecycleViewAdapter<>(this, userDisplays, R.layout.order_cell, BR.VM);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvUser.setLayoutManager(layoutManager);
        binding.rvUser.hasFixedSize();
        binding.rvUser.setAdapter(userAdapter);


        for (int i = 0; i < 8; i++) {
            shelfStates.add(new ShelfState(1));
        }
        shelfAdapter = new ShelfRecycleViewAdapter(this, shelfStates, getItemHeight());
        RecyclerView.LayoutManager grid = new GridLayoutManager(this, 4);
        binding.rvState.setLayoutManager(grid);
        binding.rvState.hasFixedSize();
        binding.rvState.setAdapter(shelfAdapter);


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
//        // 屏幕宽（dip，如：320px）
//        int screenWidthDip = dm.widthPixels;
//        // 屏幕高（dip，如：533dip）
//        int screenHeightDip = dm.heightPixels;
//        // 屏幕宽（px，如：480px）
//        int screenWidth = (int) (dm.widthPixels * density + 0.5f);
        // 屏幕高（px，如：800px）
        int screenHeight = (dm.heightPixels);
//        int marginTopAndBottom = (int) (85 * density + 0.5f);
        return screenHeight >> 2;
    }

    @Override
    public void showError(String error) {
        CustomToast.showLong(this, error);
    }

    @Override
    public void showWarning(List<OperateWarningInformViewModel> alarmInfo, boolean isFirst) {
        if (isFirst) {
            binding.rvWarning.setVisibility(View.VISIBLE);
            adapter = new WarnRecycleViewAdapter<>(this, alarmInfo, R.layout.item_recycleview_alarm, BR.VM, getItemHeight());
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
            adapter.setRecycleViewItemClickListener(this);
            binding.rvWarning.setLayoutManager(layoutManager);
            binding.rvWarning.setHasFixedSize(true);
            binding.rvWarning.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

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
        if (isOpenDoor) {
            voices.add("welcome");
            voiceSpeakUtils.speak(voices);
            userDisplays.add(new UserDisplayVM(user.getUserNo(), user.getUserName(), ""));
            userAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showUserOut(String wareHouse, UserOutVM user) {
        UserDisplayVM userDisplayVM = new UserDisplayVM().userOut2UserDisplay(user);
        userDisplays.remove(userDisplayVM);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideWarningList(String wareHouse, boolean isCheck) {
        if (isCheck) {
            binding.tvCheck.setText("仓  库  盘  点  中");
            binding.tvCheck.setVisibility(View.VISIBLE);
            binding.rvWarning.setVisibility(View.GONE);
            binding.llFormTitle.setVisibility(View.GONE);
        } else {
            binding.tvCheck.setVisibility(View.GONE);
            binding.rvWarning.setVisibility(View.VISIBLE);
            binding.llFormTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMaintainInfo(String wareHouse, boolean isMaintain) {
        if (isMaintain) {
            binding.tvCheck.setText("仓   库   维   修   中");
            binding.tvCheck.setVisibility(View.VISIBLE);
            binding.rvWarning.setVisibility(View.GONE);
            binding.llFormTitle.setVisibility(View.GONE);
        } else {
            binding.tvCheck.setVisibility(View.GONE);
            binding.rvWarning.setVisibility(View.VISIBLE);
            binding.llFormTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showTemperatureAndHumidity(String wareHouseNo, float temperature, float humidity) {
        String temp = String.valueOf(temperature);
        if (temp.length() > 5) {
            temp = temp.substring(0, 5);
        }
        String hum = String.valueOf(humidity);
        if (hum.length() > 5) {
            hum = hum.substring(0, 5);
        }
        binding.tvTemperatureHumidity.setText(String.format(Locale.CHINA, "温度：%s ℃     湿度：%s %%RH", temp, hum));

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
}
