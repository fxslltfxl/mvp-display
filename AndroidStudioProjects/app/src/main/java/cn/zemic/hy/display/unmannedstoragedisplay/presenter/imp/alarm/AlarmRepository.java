package cn.zemic.hy.display.unmannedstoragedisplay.presenter.imp.alarm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cn.zemic.hy.display.unmannedstoragedisplay.model.bean.FilterLedForMobileUiCommand;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.EntranceGuardState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.MachineBindDoorControlViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.ShelfState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserOutVM;
import cn.zemic.hy.display.unmannedstoragedisplay.network.URLFactory;
import cn.zemic.hy.display.unmannedstoragedisplay.network.service.FetchDataService;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IAlarmRepository;
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IBaseRepository;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author fxs
 */
public class AlarmRepository implements IAlarmRepository {
    private static final int DATA_ERROR = 0;
    private static final int DATA_CHANGED = 1;
    private static final int USER_CHANGED = 2;
    private static final int DATA_SEND_MSG = 3;
    private static final int USER_OUT = 4;
    private static final int CHECK = 5;
    private static final int MAINTAIN = 6;
    private static final int TEMPERATURE_HUMIDITY = 7;
    private static final int ENTRANCE_GUARD_STATE = 8;
    private static final int SHELF_STATE = 9;

    private static IBaseRepository.OnGetWarningDataFinish mOnGetWarningDataFinish;
    private static String mConnectionId;
    private MyMessagesHandler mHandler;
    private HubProxy mHubProxy;

    @Override
    public void getWareHouseNo(String deviceId, OnGetWareHouseNoFinish onGetWareHouseNoFinish) {
        FilterLedForMobileUiCommand filter = new FilterLedForMobileUiCommand(deviceId);
        FetchDataService.getWareHouseNoService(filter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (!response.isSuccessful()) {
                    onGetWareHouseNoFinish.onFail(String.format(Locale.CHINA, "获取仓库号响应异常，错误码：%d", response.code()));
                    return;
                }
                String wareHouseNo = response.body();
                onGetWareHouseNoFinish.onSuccess(wareHouseNo);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                onGetWareHouseNoFinish.onFail(t.getMessage() == null || "".equals(t.getMessage()) ? "获取仓库号失败" : t.getMessage());
            }
        });
    }

    @Override
    public void connectServiceForGetWarningAndUserInfo(String deviceId, String wareHouseNo, IBaseRepository.OnGetWarningDataFinish onGetWarningDataFinish) {
        mOnGetWarningDataFinish = onGetWarningDataFinish;
        mHandler = new MyMessagesHandler();
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        String serverURI = String.format(Locale.CHINA, "%ssignalr", URLFactory.serverURI);
//        String serverURI = "http://192.168.0.2:25535/signalr";
        HubConnection connection = new HubConnection(serverURI) {
            @Override
            protected void onConnected() {
                super.onConnected();
            }

            @Override
            public void onError(Throwable error, boolean mustCleanCurrentConnection) {
                super.onError(error, mustCleanCurrentConnection);
                Message message = Message.obtain();
                message.what = DATA_ERROR;
                message.obj = "".equals(error.getMessage()) ? "连接推送服务器出现异常，请联系管理员" : error.getMessage();
                mHandler.sendMessage(message);
            }

            @Override
            protected void onClosed() {
                super.onClosed();
            }
        };
        //hub agency
        mHubProxy = connection.createHubProxy("electronicSignsHub");
        //hubProxy.on可以理解为讯息or事件监听器
        mHubProxy.on("sendUserIn", this::runWithUserInfo, String.class, Boolean.class, UserInViewModel.class, MachineBindDoorControlViewModel.class);
        mHubProxy.on("sendCheck", this::runWithIsCheck, String.class, Boolean.class);
        mHubProxy.on("sendMaintain", this::runWithMaintain, String.class, Boolean.class);
        mHubProxy.on("sendTemAndHum", this::runWithTemperature, String.class, Float.class, Float.class);
        mHubProxy.on("sendUserLeave", this::runWithUserOut, String.class, UserOutVM.class);
        //接收告警信息
        mHubProxy.on("sendWarnInform", this::runWithWarnInfo, String.class, OperateWarningInformViewModel.class);
        //接受日志信息
        mHubProxy.on("sendConnectMessage", this::runWithConnectMsg, String.class, String.class, Boolean.class);
        mHubProxy.on("sendAttendanceMachine", this::entranceGuardState, String.class, EntranceGuardState.class);
        mHubProxy.on("sendLedShelfAndunit", this::shelfState, String.class, ShelfState.class);
        final SignalRFuture<Void> con = connection.start(new ServerSentEventsTransport(connection.getLogger()));
        try {
            con.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        con.onError(this::onError);
        if (con.isDone()) {
            //调用服务器连接方法
            mHubProxy.invoke("Connect", wareHouseNo, deviceId);
        }
    }

    @Override
    public HubProxy getHubProxy() {
        if (null == mHubProxy) {
            throw new NullPointerException("mHubProxy is null");
        }
        return mHubProxy;
    }

    private void runWithUserInfo(String warehouseNo, Boolean isOpenDoor, UserInViewModel userIn, MachineBindDoorControlViewModel doorControl) {
        Message message = Message.obtain();
        message.what = USER_CHANGED;
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOpenDoor", isOpenDoor);
        bundle.putString("warehouseNo", warehouseNo);
        bundle.putParcelable("userIn", userIn);
        bundle.putParcelable("doorControl", doorControl);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }


    private void runWithUserOut(String warehouseNo, UserOutVM user) {
        Message message = Message.obtain();
        message.what = USER_OUT;
        Bundle bundle = new Bundle();
        bundle.putString("warehouseNo", warehouseNo);
        bundle.putParcelable("userOut", user);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void runWithIsCheck(String warehouseNo, Boolean isCheck) {
        Message message = Message.obtain();
        message.what = CHECK;
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCheck", isCheck);
        bundle.putString("warehouseNo", warehouseNo);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void runWithMaintain(String warehouseNo, Boolean isMaintain) {
        Message message = Message.obtain();
        message.what = MAINTAIN;
        Bundle bundle = new Bundle();
        bundle.putBoolean("isMaintain", isMaintain);
        bundle.putString("warehouseNo", warehouseNo);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void runWithTemperature(String wareHouseNo, float temperature, float humidity) {
        Message message = Message.obtain();
        message.what = TEMPERATURE_HUMIDITY;
        Bundle bundle = new Bundle();
        bundle.putString("wareHouseNo", wareHouseNo);
        bundle.putFloat("temperature", temperature);
        bundle.putFloat("humidity", humidity);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void runWithWarnInfo(String warehouseNo, OperateWarningInformViewModel warningInform) {
        Message message = Message.obtain();
        message.what = DATA_CHANGED;
        Bundle bundle = new Bundle();
        bundle.putString("warehouseNo", warehouseNo);
        bundle.putParcelable("warningInform", warningInform);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void runWithConnectMsg(String connectionMsg, String connectionId, Boolean isConnect) {
        Message message = Message.obtain();
        message.what = DATA_SEND_MSG;
        Bundle bundle = new Bundle();
        if (isConnect) {
            mConnectionId = connectionId;
        }
        bundle.putString("connectionId", connectionId);
        bundle.putBoolean("isConnect", isConnect);
        message.setData(bundle);
        message.obj = connectionMsg;
        mHandler.sendMessage(message);
    }

    private void entranceGuardState(String warehouseNo, EntranceGuardState entranceGuardState) {
        Message message = Message.obtain();
        message.what = ENTRANCE_GUARD_STATE;
        Bundle bundle = new Bundle();
        bundle.putString("warehouseNo", warehouseNo);
        bundle.putParcelable("entranceGuardState", entranceGuardState);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void shelfState(String warehouseNo, ShelfState shelfState) {
        Message message = Message.obtain();
        message.what = SHELF_STATE;
        Bundle bundle = new Bundle();
        bundle.putString("warehouseNo", warehouseNo);
        bundle.putParcelable("shelfState", shelfState);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void onError(Throwable error) {
        Message message = Message.obtain();
        message.what = DATA_ERROR;
        message.obj = "".equals(error.getMessage()) ? error.getMessage() : "连接中断，请联系管理员";
        mHandler.sendMessage(message);
    }

    private static class MyMessagesHandler extends Handler {
        private final int LIST_MAX_SIZE = 10;
        private List<OperateWarningInformViewModel> mOperateWarningInformViewModels = new ArrayList<>();

        @Override
        public void handleMessage(Message msg) {
            if (mOnGetWarningDataFinish == null) {
                return;
            }
            super.handleMessage(msg);
            switch (msg.what) {
                case TEMPERATURE_HUMIDITY:
                    Bundle bundletempAndHum = msg.getData();
                    float temperature = bundletempAndHum.getFloat("temperature");
                    float humidity = bundletempAndHum.getFloat("humidity");
                    String wareHouseForTemp = bundletempAndHum.getString("wareHouseNo");
                    mOnGetWarningDataFinish.onFetchTemperatureAndHumidityFinish(wareHouseForTemp, temperature, humidity);
                    break;
                case MAINTAIN:
                    Bundle bundleMaintain = msg.getData();
                    String warehouseNoForMaintain = bundleMaintain.getString("warehouseNo");
                    Boolean isMaintain = bundleMaintain.getBoolean("isMaintain");
                    mOnGetWarningDataFinish.onRunningMaintain(warehouseNoForMaintain, isMaintain);
                    break;
                case CHECK:
                    Bundle bundleCheck = msg.getData();
                    String warehouseNo = bundleCheck.getString("warehouseNo");
                    Boolean isCheck = bundleCheck.getBoolean("isCheck");
                    mOnGetWarningDataFinish.onRunningCheck(warehouseNo, isCheck);
                    break;

                case DATA_SEND_MSG:
                    Bundle bundleMsg = msg.getData();
                    String connectionId = bundleMsg.getString("connectionId");
                    Boolean isConnect = bundleMsg.getBoolean("isConnect");
                    Log.e("DATA_SEND_MSG", msg.obj.toString() + connectionId);
                    if (isConnect) {
                        mOnGetWarningDataFinish.onShow(msg.obj.toString());
                    } else {
                        if (mConnectionId.equals(connectionId)) {
                            mOnGetWarningDataFinish.onShow(msg.obj.toString());
                        }
                    }
                    break;
                case DATA_ERROR:
                    Log.e("DATA_ERROR", msg.obj.toString());
                    mOnGetWarningDataFinish.onShow(msg.obj.toString());
                    break;
                case DATA_CHANGED:
                    Bundle bundle = msg.getData();
                    OperateWarningInformViewModel wainingInfo = bundle.getParcelable("warningInform");
                    if (wainingInfo != null) {
                        if (mOperateWarningInformViewModels.size() < LIST_MAX_SIZE) {
                            mOperateWarningInformViewModels.add(0, wainingInfo);
                        } else {
                            mOperateWarningInformViewModels.remove(mOperateWarningInformViewModels.size() - 1);
                            mOperateWarningInformViewModels.add(0, wainingInfo);
                        }
                    }
                    //update view
                    if (mOperateWarningInformViewModels.size() <= 1) {
                        mOnGetWarningDataFinish.onSuccess(mOperateWarningInformViewModels, true);
                    } else {
                        mOnGetWarningDataFinish.onSuccess(mOperateWarningInformViewModels, false);
                    }
                    break;
                case USER_CHANGED:
//                    //清空告警列表
//                    mOperateWarningInformViewModels.clear();
//                    mOnGetWarningDataFinish.onSuccess(mOperateWarningInformViewModels, false);
                    Bundle userBundle = msg.getData();
                    boolean isOpenDoor = userBundle.getBoolean("isOpenDoor");
                    UserInViewModel userIn = userBundle.getParcelable("userIn");
                    String userWarehouseNo = userBundle.getString("warehouseNo");
                    mOnGetWarningDataFinish.onUserChange(userIn, userWarehouseNo, isOpenDoor);
                    break;
                case USER_OUT:
                    Bundle userOutBundle = msg.getData();
                    UserOutVM userOut = userOutBundle.getParcelable("userOut");
                    String userOutWarehouseNo = userOutBundle.getString("warehouseNo");
                    if (null == userOut) {
                        break;
                    }
                    mOnGetWarningDataFinish.onUserOut(userOutWarehouseNo, userOut);
                    break;
                case ENTRANCE_GUARD_STATE:
                    Bundle doorState = msg.getData();
                    String doorWarehouseNo = doorState.getString("warehouseNo");
                    EntranceGuardState entranceGuardState = doorState.getParcelable("entranceGuardState");
                    mOnGetWarningDataFinish.onReceiveDoorState(doorWarehouseNo, entranceGuardState);
                    break;
                case SHELF_STATE:
                    Bundle shelfStateBundle = msg.getData();
                    String shelfWarehouseNo = shelfStateBundle.getString("warehouseNo");
                    ShelfState shelfState = shelfStateBundle.getParcelable("shelfState");
                    mOnGetWarningDataFinish.onReceiveShelfState(shelfWarehouseNo, shelfState);
                    break;
                default:
                    break;
            }
        }
    }
}

