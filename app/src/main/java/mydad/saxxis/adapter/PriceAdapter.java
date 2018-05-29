package mydad.saxxis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mydad.saxxis.R;
import mydad.saxxis.models.Stock;

public class PriceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Stock> stocks;
    private Context context;

    public PriceAdapter(Context context, List<Stock> stocks) {
        this.stocks = stocks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Stock getItem(int position) {
        return stocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stocks.get(position).getStockId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Stock stock = getItem(position);
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

        if (stock != null) {
            priceVH.tv_price.setText(String.valueOf(stock.getQuantity()));
        }

        return rowView;
    }

    private class PriceViewHolder {
        TextView tv_price;
    }
}
