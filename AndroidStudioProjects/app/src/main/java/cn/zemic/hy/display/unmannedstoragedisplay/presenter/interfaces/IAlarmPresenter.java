package cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces;

/**
 * @author fxs
 */
public interface IAlarmPresenter extends IBasePresenter {
    /**
     * 获取仓库编号与告警信息
     *
     * @param deviceId 设备唯一id
     */
    void getWareHouseNoAndWarningInfo(String deviceId);

    /**
     *
     * 加载告警详情
     * @param position listView位置
     */
    void loadDetailWarningInfo(int position);
}
