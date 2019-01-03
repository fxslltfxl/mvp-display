package cn.zemic.hy.display.unmannedstoragedisplay.presenter.imp.alarm;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.EntranceGuardState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.ShelfState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserOutVM;
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

    private String mDeviceId;

    private String mWareHouseNo;

    private List<OperateWarningInformViewModel> mAlarmInfoBeans = new ArrayList<>();


    public AlarmPresenter(IAlarmView iAlarmView, IAlarmRepository interaction) {
        this.iAlarmView = iAlarmView;
        this.interaction = interaction;
    }

    @Override
    public void getWareHouseNoAndWarningInfo(String deviceId) {
        mDeviceId = deviceId;
        iAlarmView.showLoading();
        //获取仓库编号 start
        interaction.getWareHouseNo(deviceId, new IBaseRepository.OnGetWareHouseNoFinish() {
            @Override
            public void onSuccess(String wareHouseNo) {
                mWareHouseNo = wareHouseNo;
                //连接服务器，获取告警信息 start
                interaction.connectServiceForGetWarningAndUserInfo(deviceId, wareHouseNo, new IBaseRepository.OnGetWarningDataFinish() {
                    @Override
                    public void onSuccess(List<OperateWarningInformViewModel> alarmInfoBeans, boolean isFirst) {
                        iAlarmView.hideLoading();
                        iAlarmView.showWarning(alarmInfoBeans, isFirst);
                        mAlarmInfoBeans = alarmInfoBeans;
                    }

                    @Override
                    public void onShow(String error) {
                        iAlarmView.hideLoading();
                        iAlarmView.showError(error);
                    }

                    @Override
                    public void onUserChange(UserInViewModel user, String warehouseNo,boolean isOpenDoor) {
                        iAlarmView.hideLoading();
                        iAlarmView.showGreetForUser(user,warehouseNo,isOpenDoor);
                    }

                    @Override
                    public void onUserOut(String wareHouse, UserOutVM user) {
                        iAlarmView.hideLoading();
                        iAlarmView.showUserOut(wareHouse,user);
                    }

                    @Override
                    public void onRunningCheck(String wareHouse, boolean isCheck) {
                        iAlarmView.hideLoading();
                        iAlarmView.hideWarningList(wareHouse,isCheck);
                    }

                    @Override
                    public void onRunningMaintain(String wareHouse, boolean isMaintain) {
                        iAlarmView.hideLoading();
                        iAlarmView.showMaintainInfo(wareHouse,isMaintain);
                    }

                    @Override
                    public void onFetchTemperatureAndHumidityFinish(String wareHouseNo, float temperature, float humidity) {
                        iAlarmView.hideLoading();
                        iAlarmView.showTemperatureAndHumidity(wareHouseNo,temperature,humidity);
                    }

                    @Override
                    public void onReceiveDoorState(String warehouseNo, EntranceGuardState entranceGuardState) {
                        iAlarmView.hideLoading();
                        iAlarmView.showDoorState(warehouseNo,entranceGuardState);
                    }

                    @Override
                    public void onReceiveShelfState(String shelfWarehouseNo, ShelfState shelfState) {
                        iAlarmView.hideLoading();
                        iAlarmView.showShelfState(shelfWarehouseNo,shelfState);
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
        if (null!=interaction&&null!=interaction.getHubProxy()){
            interaction.getHubProxy().invoke("Connect",mDeviceId);
        }
    }
}
