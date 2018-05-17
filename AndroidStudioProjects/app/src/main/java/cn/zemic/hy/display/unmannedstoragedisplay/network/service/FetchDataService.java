package cn.zemic.hy.display.unmannedstoragedisplay.network.service;

import cn.zemic.hy.display.unmannedstoragedisplay.model.bean.FilterLedForMobileUiCommand;
import cn.zemic.hy.display.unmannedstoragedisplay.network.RetrofitClient;
import cn.zemic.hy.display.unmannedstoragedisplay.network.iservices.IFetchDataService;
import retrofit2.Call;

/**
 * @author fxs
 */
public class FetchDataService {
    private static IFetchDataService mIFetchDataService = RetrofitClient.getInstance().create(IFetchDataService.class);

    public static Call<String> getWareHouseNoService(FilterLedForMobileUiCommand deviceId) {
        return mIFetchDataService.getWareHouseNoService(deviceId);
    }
}
