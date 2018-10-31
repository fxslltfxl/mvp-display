package cn.zemic.hy.display.unmannedstoragedisplay.adapter;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * @author fxs
 * @date 2018/3/26
 */

public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    /**
     * item布局 绑定的ViewDataBinding
     */
    private T mViewDataBinding;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.mViewDataBinding = DataBindingUtil.bind(itemView);
    }

    public final T getBinding() {
        return mViewDataBinding;
    }
}

