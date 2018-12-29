package cn.zemic.hy.display.unmannedstoragedisplay.adapter;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import cn.zemic.hy.display.unmannedstoragedisplay.R;
import cn.zemic.hy.display.unmannedstoragedisplay.databinding.ShelfCellBinding;
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.ShelfState;


/**
 * @author fxs
 */
public class ShelfRecycleViewAdapter extends RecyclerView.Adapter<BaseViewHolder<ShelfCellBinding>> {
    /**
     * 数据源
     */
    private List<ShelfState> mList;

    private Context mContext;
    private int mItemHeight;
    /**
     * 自定义item单击事件
     */
    private OnRecycleViewItemClickListener listener;

    public ShelfRecycleViewAdapter(Context mContext, List<ShelfState> mList, int itemHeight) {
        this.mList = mList;
        this.mContext = mContext;
        this.mItemHeight = itemHeight;
    }

    @Override
    public BaseViewHolder<ShelfCellBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.shelf_cell, parent, false);

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.height = mItemHeight;
        itemView.setLayoutParams(layoutParams);
        return new BaseViewHolder<>(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ShelfCellBinding> holder, int position) {


        //TODO  韩旭  你在这写 bug


        holder.getBinding().shelfName.setText(String.format(Locale.CHINA,"货架%d", position + 1));

        //Item 点击事件
        if (listener != null) {
            //点击事件
            holder.itemView.setOnClickListener(view -> listener.onItemClick(holder.getBinding(), position));
            //长击事件
            holder.itemView.setOnLongClickListener(v -> {
                listener.onLongItemClick(holder.getBinding(), position);
                return false;
            });
        }
        //刷新页面
        holder.getBinding().executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setRecycleViewItemClickListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnRecycleViewItemClickListener {
        /**
         * onclick event
         *
         * @param dataBinding dataBinding
         * @param position    位置
         */
        void onItemClick(ViewDataBinding dataBinding, int position);

        /**
         * long onclick event
         *
         * @param dataBinding dataBinding
         * @param position    位置
         */
        void onLongItemClick(ViewDataBinding dataBinding, int position);
    }


}
