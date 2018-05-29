package mydad.saxxis.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import mydad.saxxis.R;
import mydad.saxxis.fragments.AddDeliveryAddressFragment;
import mydad.saxxis.models.AddressDeleteRequest;
import mydad.saxxis.models.DeliveryAddress;
import mydad.saxxis.models.RequestResponse;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.ConnectivityReceiver;
import mydad.saxxis.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh on 2017-08-01.
 */

public class DeliveryAddressAdapter extends RecyclerSwipeAdapter<DeliveryAddressAdapter.MyViewHolder> {

    private static String TAG = DeliveryAddressAdapter.class.getSimpleName();

    private List<DeliveryAddress> modelList;

    private Context context;

    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    private boolean ischecked = false;
    private String location_id = "";
    private String getsocity, gethouse, getphone, getpin, getname, getcharge;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_address, tv_name, tv_phone, tv_charges;
        public RadioButton rb_select;

        SwipeLayout swipeLayout;
        Button buttonDelete, btn_edit;

        public MyViewHolder(View view) {
            super(view);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.delete);
            btn_edit = itemView.findViewById(R.id.edit);

            tv_address = view.findViewById(R.id.tv_adres_address);
            tv_name = view.findViewById(R.id.tv_adres_username);
            tv_phone = view.findViewById(R.id.tv_adres_phone);
            tv_charges = view.findViewById(R.id.tv_adres_charge);
            rb_select = view.findViewById(R.id.rb_adres);

            rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RadioButton cb = (RadioButton) view;
                    int clickedPos = getAdapterPosition();

                    location_id = modelList.get(clickedPos).getLocation_id();
                    gethouse = modelList.get(clickedPos).getHouse_no();
                    getname = modelList.get(clickedPos).getReceiver_name();
                    getphone = modelList.get(clickedPos).getReceiver_mobile();
                    getsocity = modelList.get(clickedPos).getSocity_name();
                    getpin = modelList.get(clickedPos).getPincode();
                    getcharge = modelList.get(clickedPos).getDelivery_charge();

                    if (modelList.size() > 1) {
                        if (cb.isChecked()) {
                            if (lastChecked != null) {
                                lastChecked.setChecked(false);
                                modelList.get(lastCheckedPos).setIscheckd(false);
                            }

                            lastChecked = cb;
                            lastCheckedPos = clickedPos;
                        } else {
                            lastChecked = null;
                        }
                    }
                    modelList.get(clickedPos).setIscheckd(cb.isChecked());

                    if (cb.isChecked()) {
                        ischecked = true;

                        Intent updates = new Intent("Grocery_delivery_charge");
                        updates.putExtra("type", "update");
                        updates.putExtra("charge", getcharge);
                        context.sendBroadcast(updates);
                    } else {
                        ischecked = false;

                        Intent updates = new Intent("Grocery_delivery_charge");
                        updates.putExtra("type", "update");
                        updates.putExtra("charge", "00");
                        context.sendBroadcast(updates);
                    }

                }
            });
        }
    }

    public DeliveryAddressAdapter(List<DeliveryAddress> modelList) {
        this.modelList = modelList;
    }

    @Override
    public DeliveryAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_time, parent, false);

        context = parent.getContext();

        return new DeliveryAddressAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DeliveryAddressAdapter.MyViewHolder holder, final int position) {
        final DeliveryAddress mList = modelList.get(position);

        holder.tv_address.setText(mList.getSocity_name());
        holder.tv_phone.setText(mList.getReceiver_mobile());
        holder.tv_name.setText(mList.getReceiver_name());
        holder.tv_charges.setText(mList.getDelivery_charge());

        holder.rb_select.setChecked(mList.getIscheckd());
        holder.rb_select.setTag(new Integer(position));

        //for default check in first item
        if (position == 0 /*&& mList.getIscheckd() && holder.rb_select.isChecked()*/) {
            holder.rb_select.setChecked(true);
            modelList.get(position).setIscheckd(true);

            lastChecked = holder.rb_select;
            lastCheckedPos = 0;

            location_id = modelList.get(0).getLocation_id();

            gethouse = modelList.get(0).getHouse_no();
            getname = modelList.get(0).getReceiver_name();
            getphone = modelList.get(0).getReceiver_mobile();
            getsocity = modelList.get(0).getSocity_name();
            getpin = modelList.get(0).getPincode();
            getcharge = modelList.get(0).getDelivery_charge();

            ischecked = true;

            Intent updates = new Intent("Grocery_delivery_charge");
            updates.putExtra("type", "update");
            updates.putExtra("charge", getcharge);
            context.sendBroadcast(updates);
        }

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        holder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                /*modelList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, modelList.size());
                mItemManger.closeAllItems();*/

                if (ConnectivityReceiver.isConnected()) {
                    makeDeleteAddressRequest(mList.getLocation_id(), position);
                }

            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement = new SessionManagement(context);
                sessionManagement.updateSocity("", "");

                Bundle args = new Bundle();
                Fragment fm = new AddDeliveryAddressFragment();
                args.putString("location_id", mList.getLocation_id());
                args.putString("name", mList.getReceiver_name());
                args.putString("mobile", mList.getReceiver_mobile());
                args.putString("pincode", mList.getPincode());
                args.putString("socity_id", mList.getSocity_id());
                args.putString("socity_name", mList.getSocity_name());
                args.putString("house", mList.getHouse_no());
                fm.setArguments(args);
               /* FragmentManager fragmentManager = ((Activity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit(); */ // uncomment this this is causing error so i commented it.
            }
        });

        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public String getlocation_id() {
        return location_id;
    }

    public String getaddress() {
        String address = context.getResources().getString(R.string.reciver_name) + getname + "\n" + context.getResources().getString(R.string.reciver_mobile) + getphone +
                "\n" + context.getResources().getString(R.string.pincode) + getpin +
                "\n" + context.getResources().getString(R.string.house_no) + gethouse +
                "\n" + context.getResources().getString(R.string.socity) + getsocity;

        return address;
    }

    public boolean ischeckd() {
        return ischecked;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeDeleteAddressRequest(String location_id, final int position) {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.deleteAddress(location_id).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RequestResponse rr = response.body();

                    if (rr.isResponce()) {

                        Toast.makeText(context, rr.getMessage(), Toast.LENGTH_SHORT).show();

                        modelList.remove(position);
                        notifyDataSetChanged();
                        mItemManger.closeAllItems();

                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                Toast.makeText(context, R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
