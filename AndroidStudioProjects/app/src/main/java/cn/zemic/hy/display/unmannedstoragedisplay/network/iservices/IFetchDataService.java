package cn.zemic.hy.display.unmannedstoragedisplay.network.iservices;

import cn.zemic.hy.display.unmannedstoragedisplay.model.bean.FilterLedForMobileUiCommand;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author fxs
 */
public interface IFetchDataService {
    /**
     * 获取仓库编号
     *
     * @param deviceId 唯一id
     * @return 仓库编号
     */
    @POST("api/mobile/led")
    Call<String> getWareHouseNoService(@Body FilterLedForMobileUiCommand deviceId);
}
