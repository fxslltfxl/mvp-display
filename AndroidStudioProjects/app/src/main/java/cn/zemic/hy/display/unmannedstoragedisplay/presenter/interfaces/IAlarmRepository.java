package cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces;

import microsoft.aspnet.signalr.client.hubs.HubProxy;

/**
 * @author fxs
 */
public interface IAlarmRepository extends IBaseRepository {


    void getWareHouseNo(String deviceId,IBaseRepository.OnGetWareHouseNoFinish onGetWareHouseNoFinish);

    /**
     * fetch WarningAndUserInfo
     *
     * @param wareHouseNo                 search condition
     * @param deviceId                 search condition
     * @param onGetWarningDataFinish get data finish callback
     */
    void connectService(String deviceId, String wareHouseNo, IBaseRepository.OnGetWarningDataFinish onGetWarningDataFinish);

    HubProxy getHubProxy();
}
