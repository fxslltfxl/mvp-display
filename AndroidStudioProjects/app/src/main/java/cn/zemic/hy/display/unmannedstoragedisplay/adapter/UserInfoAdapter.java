package cn.zemic.hy.display.unmannedstoragedisplay.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;

import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.R;
import cn.zemic.hy.display.unmannedstoragedisplay.databinding.OrderCellBinding;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.UserDisplayVM;

public class UserInfoAdapter extends BaseRecycleViewAdapter<UserDisplayVM, OrderCellBinding> {
    public UserInfoAdapter(Context mContext, List mList, int resID, int variableId) {
        super(mContext, mList, resID, variableId);
    }

    public UserInfoAdapter(Context mContext, List mList, int resID, int variableId, int itemHeight) {
        super(mContext, mList, resID, variableId, itemHeight);
    }

    @Override
    public int getViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public @LayoutRes
    int getResID(int viewType) {
        switch (viewType) {
            case 0:
                return R.layout.order_cell_gray;
            case 1:
                return R.layout.order_cell;
            default:
                return R.layout.order_cell;
        }
    }
}
