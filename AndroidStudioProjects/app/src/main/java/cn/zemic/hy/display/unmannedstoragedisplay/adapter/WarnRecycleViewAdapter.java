package cn.zemic.hy.display.unmannedstoragedisplay.adapter;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.zemic.hy.display.unmannedstoragedisplay.R;


/**
 * @author fxs
 */
public class WarnRecycleViewAdapter<T, K extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<K>> {

    private static final int TYPE_ONE = 1;
    private static final int TYPE_SECOND = 2;
    /**
     * 数据源
     */
    private List<T> mList;
    /**
     * item 布局
     */
    @LayoutRes
    private int resID;
    /**
     * 布局内VariableId
     */
    private int variableId;
    private Context mContext;
    private int mItemHeight;
    /**
     * 自定义item单击事件
     */
    private OnRecycleViewItemClickListener listener;

    public WarnRecycleViewAdapter(Context mContext, List<T> mList, @LayoutRes int resID, int variableId, int itemHeight) {
        this.mList = mList;
        this.resID = resID;
        this.variableId = variableId;
        this.mContext = mContext;
        this.mItemHeight = itemHeight;
    }

    @Override
    public BaseViewHolder<K> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_ONE) {
            itemView = LayoutInflater.from(mContext).inflate(resID, parent, false);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recycleview_alarm_gary, parent, false);
        }
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.height = mItemHeight;
        itemView.setLayoutParams(layoutParams);
        return new BaseViewHolder<>(itemView);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder<K> holder, int position) {
        holder.getBinding().setVariable(variableId, mList.get(position));
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
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return TYPE_ONE;
        } else {
            return TYPE_SECOND;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void addItem(T t) {
        addItem(t, -1);
    }

    /**
     * 插入Item，并且更新界面
     *
     * @param t        data
     * @param position 位置
     */
    public void addItem(T t, int position) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        if (position == -1) {
            position = mList.size();
        }
        mList.add(position, t);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void addItems(List<T> ts) {
        addItems(ts, -1);
    }

    /**
     * 添加Items
     *
     * @param ts       List<T>
     * @param position position
     */
    public void addItems(List<T> ts, int position) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        if (position == -1) {
            position = mList.size();
        }
        mList.addAll(position, ts);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    /**
     * remove Item
     *
     * @param t data
     */
    public void removeItem(T t) {
        if (mList == null) {
            notifyDataSetChanged();
            return;
        }
        int position = mList.indexOf(t);
        if (position != -1) {
            mList.remove(t);
            notifyItemRemoved(position);
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (mList == null) {
            notifyDataSetChanged();
            return;
        }
        if (position >= 0 && position < mList.size()) {
            //移除数据
            mList.remove(position);
            //移除视图
            notifyItemRemoved(position);
        }
        // 更新position
        notifyDataSetChanged();
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
