package codecanyon.grocery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.models.SoCity;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class SocityAdapter extends RecyclerView.Adapter<SocityAdapter.MyViewHolder>
        implements Filterable {

    private List<SoCity> modelList;
    private List<SoCity> mFilteredList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_socity_name);
        }
    }

    public SocityAdapter(List<SoCity> modelList) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public SocityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_socity, parent, false);

        context = parent.getContext();

        return new SocityAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SocityAdapter.MyViewHolder holder, int position) {
        SoCity mList = modelList.get(position);

        holder.title.setText(mList.getSocity_name());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = modelList;
                } else {

                    ArrayList<SoCity> filteredList = new ArrayList<>();

                    for (SoCity soCity : modelList) {

                        if (soCity.getSocity_name().toLowerCase().contains(charString)) {

                            filteredList.add(soCity);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<SoCity>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }


}
