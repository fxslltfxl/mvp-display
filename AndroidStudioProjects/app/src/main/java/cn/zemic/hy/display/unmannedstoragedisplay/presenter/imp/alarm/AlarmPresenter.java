package cn.zemic.hy.display.unmannedstoragedisplay.presenter.imp.alarm;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.imp.BasePresenter;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IAlarmPresenter;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IAlarmRepository;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IBaseRepository;
import cn.zemic.hy.display.unmannedstoragedisplay.view.activity.alarmactivity.IAlarmView;


/**
 * @author fxs
 */
public class AlarmPresenter extends BasePresenter implements IAlarmPresenter {

    private IAlarmView iAlarmView;

    private IAlarmRepository interaction;

    private List<OperateWarningInformViewModel> mAlarmInfoBeans = new ArrayList<>();


    public AlarmPresenter(IAlarmView iAlarmView, IAlarmRepository interaction) {
        this.iAlarmView = iAlarmView;
        this.interaction = interaction;
    }

    @Override
    public void getWareHouseNoAndWarningInfo(String deviceId) {
        iAlarmView.showLoading();
        //获取仓库编号 start
        interaction.getWareHouseNo(deviceId, new IBaseRepository.OnGetWareHouseNoFinish() {
            @Override
            public void onSuccess(String wareHouseNo) {
                //连接服务器，获取告警信息 start
                interaction.connectServiceForGetWarningAndUserInfo(deviceId, wareHouseNo, new IBaseRepository.OnGetWarningDataFinish() {
                    @Override
                    public void onSuccess(List<OperateWarningInformViewModel> alarmInfoBeans, boolean isFirst) {
                        iAlarmView.hideLoading();
                        iAlarmView.showWarning(alarmInfoBeans, isFirst);
                        mAlarmInfoBeans = alarmInfoBeans;
                    }

                    @Override
                    public void onFail(String error) {
                        iAlarmView.hideLoading();
                        iAlarmView.showError(error);
                    }

                    @Override
                    public void onUserChange(UserInViewModel user, String warehouseNo,boolean isOpenDoor) {
                        iAlarmView.hideLoading();
                        iAlarmView.showGreetForUser(user,warehouseNo,isOpenDoor);
                    }
                });
            }

            //连接服务器，获取告警信息 end
            @Override
            public void onFail(String error) {
                iAlarmView.hideLoading();
                iAlarmView.showError(error);
            }
        });
        //获取仓库编号 end
    }

    @Override
    public void loadDetailWarningInfo(int position) {
//        if (iAlarmView != null && mAlarmInfoBeans != null) {
//            iAlarmView.showDetailInfo(mAlarmInfoBeans.get(position));
//        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        super.onDestroy(owner);
        if (iAlarmView != null) {
            iAlarmView = null;
        }
    }
}
