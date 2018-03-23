package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

import Model.Product_model;
import util.DatabaseHandler;

/**
 * Created by srikarn on 23-03-2018.
 */

public class Categori_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder>
        implements Filterable {

    private List<Product_model> modelList;
    private List<Product_model> mFilteredList;
    private Context context;
    private DatabaseHandler dbcart;


    @NonNull
    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Product_adapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
