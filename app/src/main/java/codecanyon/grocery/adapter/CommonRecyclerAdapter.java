package codecanyon.grocery.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.listeners.OnLoadMoreListener;

/**
 * Created by FAMILY on 12-02-2018.
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_DELEGATE = 0;
    private static final int ITEM_VIEW_TYPE_PROGRESS_FOOTER = 1;
    private List<T> dataSet;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView recyclerView;
    private boolean loading;

    public CommonRecyclerAdapter() {
        dataSet = new ArrayList<>();
    }

    public CommonRecyclerAdapter(RecyclerView recyclerView, @NonNull OnLoadMoreListener onLoadMoreListener) {
        dataSet = new ArrayList<>();
        this.onLoadMoreListener = onLoadMoreListener;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == ITEM_VIEW_TYPE_PROGRESS_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            viewHolder = new ProgressBarViewHolder(view);
        } else {
            viewHolder = onCreateBasicItemViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType != ITEM_VIEW_TYPE_PROGRESS_FOOTER) {
            onBindBasicItemView(holder, position);
            if (onLoadMoreListener != null) {
                final int pos = position;

                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!loading && pos >= getItemCount() - 1) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }, 3000);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? ITEM_VIEW_TYPE_DELEGATE : ITEM_VIEW_TYPE_PROGRESS_FOOTER;
    }


    public final void addItems(List<T> items) {
        int positionStart = dataSet.size() + 1;
        dataSet.addAll(items);
        notifyItemRangeInserted(positionStart, items.size());
    }

    public final void resetItems() {
        loading = false;
        dataSet.clear();
        notifyDataSetChanged();
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.size() > 0 && dataSet.size() > index && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            return null;
        }
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void addItem(T item, int position) {
        if (!dataSet.contains(item)) {
            dataSet.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void updateItem(int position) {
        notifyItemChanged(position);
    }

    public void updateItem(int position, T item) {
        dataSet.set(position, item);
        notifyItemChanged(position);
    }

    public void clearItemsforSearch() {
        dataSet.clear();
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public void removeItem(int indexOfItem) {
        if (indexOfItem != -1) {
            dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    public abstract RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(RecyclerView.ViewHolder holder, int position);

    public static class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressBarViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progress_bar);
        }
    }
}