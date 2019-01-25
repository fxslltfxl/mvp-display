package cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces;

import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.EntranceGuardState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.ShelfState;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserOutVM;

/**
 * @author fxs
 */
public interface IBaseRepository {

    interface OnGetWareHouseNoFinish{
        void onSuccess(String wareHouseNo);
        void onFail(String error);
    }

    interface OnGetWarningDataFinish {


        /**
         * successful callback
         *
         * @param alarmInfoBeans data source
         * @param isFirst        first upload data ?(if true set adapter otherwise refresh)
         */
        void onSuccess(List<OperateWarningInformViewModel> alarmInfoBeans, boolean isFirst);

        /**
         * show message call back
         *
         * @param message message
         */
        void onShow(String message);

        /**
         * when user switchover(用户切换时)
         *
         * @param user        user view model
         * @param warehouseNo 仓库号
         */
        void onUserChange(UserInViewModel user, String warehouseNo,boolean isOpenDoor);

        void onUserOut(String wareHouse, UserOutVM user);

        void onRunningCheck(String wareHouse, boolean isCheck);
        void onRunningMaintain(String wareHouse, boolean isMaintain);

        void onFetchTemperatureAndHumidityFinish(String wareHouseNo,float temperature, float humidity);

        void onReceiveDoorState(String warehouseNo, EntranceGuardState entranceGuardState);

        void onReceiveShelfState(String shelfWarehouseNo, ShelfState shelfState);
    }
}
