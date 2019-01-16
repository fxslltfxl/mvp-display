package cn.zemic.hy.display.unmannedstoragedisplay.view.activity.alarmactivity;

import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.EntranceGuardState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.ShelfState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserOutVM;

/**
 * @author fxs
 */
@SuppressWarnings("unused")
public interface IAlarmView {

    /**
     * show  error info
     *
     * @param string error message
     */
    void showError(String string);


    /**
     * show warning info for user whose into warehouse
     *
     * @param alarmInfo alarmInfo list
     * @param isFirst   judge whether or not first data ，if first set adapter ，if not refresh
     */
    void showWarning(List<OperateWarningInformViewModel> alarmInfo, boolean isFirst);

    /**
     * show loading view
     */
    void showLoading();

    /**
     * hide loading view
     */
    void hideLoading();


    /**
     * show detail
     *
     * @param alarmInfoBean data source
     */
    void showDetailInfo(final OperateWarningInformViewModel alarmInfoBean);


    /**
     * when user in
     * @param user user
     * @param warehouseNo warehouseNo
     * @param isOpenDoor  isOpenDoor
     */
    void showGreetForUser(UserInViewModel user,String warehouseNo,boolean isOpenDoor);

    /**
     * when user out
     * @param wareHouse the no of warehouse
     * @param user UserOutVM
     */
    void showUserOut(String wareHouse, UserOutVM user);

    /**
     * hideWarningList
     * @param wareHouse the no of warehouse
     * @param isCheck check is running?
     */
    void hideWarningList(String wareHouse, boolean isCheck);

    /**
     * showMaintainInfo
     * @param wareHouse the no of warehouse
     * @param isMaintain maintain is running?
     */
    void showMaintainInfo(String wareHouse, boolean isMaintain);

    /**
     * showTemperatureAndHumidity
     * @param wareHouseNo the no of warehouse
     * @param temperature temperature
     * @param humidity humidity
     */
    void showTemperatureAndHumidity(String wareHouseNo, float temperature, float humidity);


    /**
     * showShelfState
     * @param shelfWarehouseNo shelfWarehouseNo
     * @param shelfState shelfState
     */
    void showShelfState(String shelfWarehouseNo, ShelfState shelfState);

    /**
     * show Base Station State
     */
    void showBaseStationState();

    /**
     * showDoorState
     * @param warehouseNo warehouseNo
     * @param entranceGuardState entranceGuardState
     */
    void showDoorState(String warehouseNo, EntranceGuardState entranceGuardState);
}
