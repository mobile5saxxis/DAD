package codecanyon.grocery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.models.Price;

public class PriceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Price> prices;
    private Context context;

    public PriceAdapter(Context context, List<Price> prices) {
        this.prices = prices;
        this.context = context;
    }

    @Override
    public int getCount() {
        return prices.size();
    }

    @Override
    public Price getItem(int position) {
        return prices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return prices.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Price price = getItem(position);
        PriceViewHolder priceVH;
        View rowView = convertView;

        if (rowView == null) {

            priceVH = new PriceViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_spinner, null, false);

            priceVH.tv_price = rowView.findViewById(R.id.tv_price);
            rowView.setTag(priceVH);
        } else {
            priceVH = (PriceViewHolder) rowView.getTag();
        }

        if (price != null) {
            priceVH.tv_price.setText(String.valueOf(price.getQuantity()));
        }

        return rowView;
    }

    private class PriceViewHolder {
        TextView tv_price;
    }
}
