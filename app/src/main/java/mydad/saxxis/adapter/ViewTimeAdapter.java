package mydad.saxxis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mydad.saxxis.R;

/**
 * Created by Rajesh Dabhi on 4/7/2017.
 */

public class ViewTimeAdapter extends RecyclerView.Adapter<ViewTimeAdapter.MyViewHolder> {

    private List<String> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_socity_name);
        }
    }

    public ViewTimeAdapter(List<String> modelList) {
        this.modelList = modelList;
    }

    @Override
    public ViewTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_socity, parent, false);

        context = parent.getContext();

        return new ViewTimeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewTimeAdapter.MyViewHolder holder, int position) {
        //Society mList = modelList.get(position);

        holder.title.setText(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
