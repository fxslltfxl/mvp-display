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
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.ShelfLedState;


/**
 * @author fxs
 */
public class ShelfRecycleViewAdapter extends RecyclerView.Adapter<BaseViewHolder<ShelfCellBinding>> {
    /**
     * 数据源
     */
    private List<ShelfLedState> mList;

    private Context mContext;
    private int mItemHeight;
    /**
     * 自定义item单击事件
     */
    private OnRecycleViewItemClickListener listener;

    public ShelfRecycleViewAdapter(Context mContext, List<ShelfLedState> mList, int itemHeight) {
        this.mList = mList;
        this.mContext = mContext;
        this.mItemHeight = itemHeight;
    }

    @Override
    @NonNull
    public BaseViewHolder<ShelfCellBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.shelf_cell, parent, false);

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.height = mItemHeight;
        itemView.setLayoutParams(layoutParams);
        return new BaseViewHolder<>(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ShelfCellBinding> holder, int position) {
        holder.getBinding().shelfName.setText(String.format(Locale.CHINA, "货架%s", mList.get(position).getShelfNo()));
        String shelfState = mList.get(position).getShelfState();

        switch (shelfState) {
            case "0":
                holder.getBinding().correct.setImageResource(R.drawable.correct_normal);
                holder.getBinding().warn.setImageResource(R.drawable.warn_normal);
                holder.getBinding().error.setImageResource(R.drawable.error);
                break;
            case "1":
                holder.getBinding().correct.setImageResource(R.drawable.correct);
                holder.getBinding().warn.setImageResource(R.drawable.warn_normal);
                holder.getBinding().error.setImageResource(R.drawable.error_normal);
                break;
            case "2":
                holder.getBinding().correct.setImageResource(R.drawable.correct_normal);
                holder.getBinding().warn.setImageResource(R.drawable.warn);
                holder.getBinding().error.setImageResource(R.drawable.error_normal);
                break;
            default:
                break;
        }

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

    public void setDataChange(List<ShelfLedState> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @SuppressWarnings("unused")
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
