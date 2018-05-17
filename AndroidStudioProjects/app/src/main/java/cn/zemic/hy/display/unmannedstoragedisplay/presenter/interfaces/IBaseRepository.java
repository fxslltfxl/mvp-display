package cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces;

import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.OperateWarningInformViewModel;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserInViewModel;

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
         * fail call back
         *
         * @param error message
         */
        void onFail(String error);

        /**
         * when user switchover(用户切换时)
         *
         * @param user        user view model
         * @param warehouseNo 仓库号
         */
        void onUserChange(UserInViewModel user, String warehouseNo,boolean isOpenDoor);
    }
}
