package cn.zemic.hy.display.unmannedstoragedisplay.view.activity.alarmactivity;

import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserOutVM;

/**
 * @author fxs
 */
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
     * show greet
     *
     * @param user user info
     */
    void showGreetForUser(UserInViewModel user,String warehouseNo,boolean isOpenDoor);

    /**
     *
     * @param wareHouse
     * @param user
     */
    void showUserOut(String wareHouse, UserOutVM user);


    void hideWarningList(String wareHouse, boolean isCheck);
    void showMaintainInfo(String wareHouse, boolean isMaintain);

    void showTemperatureAndHumidity(String wareHouseNo, float temperature, float humidity);
}
