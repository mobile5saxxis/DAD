package apsara.saxxis.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.R;
import apsara.saxxis.reterofit.APIUrls;
import apsara.saxxis.util.ConnectivityReceiver;
import apsara.saxxis.util.JSONParser;
import apsara.saxxis.util.NameValuePair;
import apsara.saxxis.util.SessionManagement;

/**
 * Created by Rajesh Dabhi on 28/6/2017.
 */

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private static String TAG = EditProfileFragment.class.getSimpleName();

    private EditText et_phone, et_name, et_email, et_house;
    private Button btn_update;
    private TextView tv_phone, tv_name, tv_email, tv_house, tv_socity, btn_socity;
    private ImageView iv_profile;
    //private Spinner sp_socity;

    private String getsocity = "";
    private String filePath = "";
    private static final int GALLERY_REQUEST_CODE1 = 201;
    private Bitmap bitmap;
    private Uri imageuri;

    private SessionManagement sessionManagement;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(true);

        sessionManagement = new SessionManagement(getActivity());

        et_phone = view.findViewById(R.id.et_pro_phone);
        et_name = view.findViewById(R.id.et_pro_name);
        tv_phone = view.findViewById(R.id.tv_pro_phone);
        tv_name = view.findViewById(R.id.tv_pro_name);
        tv_email = view.findViewById(R.id.tv_pro_email);
        et_email = view.findViewById(R.id.et_pro_email);
        iv_profile = view.findViewById(R.id.iv_pro_img);
        /*et_house = (EditText) view.findViewById(R.id.et_pro_home);
        tv_house = (TextView) view.findViewById(R.id.tv_pro_home);
        tv_socity = (TextView) view.findViewById(R.id.tv_pro_socity);*/
        btn_update = view.findViewById(R.id.btn_pro_edit);
        //btn_socity = (TextView) view.findViewById(R.id.btn_pro_socity);

        String getemail = sessionManagement.getUserDetails().get(APIUrls.KEY_EMAIL);
        String getimage = sessionManagement.getUserDetails().get(APIUrls.KEY_IMAGE);
        String getname = sessionManagement.getUserDetails().get(APIUrls.KEY_NAME);
        String getphone = sessionManagement.getUserDetails().get(APIUrls.KEY_MOBILE);
        String getpin = sessionManagement.getUserDetails().get(APIUrls.KEY_PINCODE);
        String gethouse = sessionManagement.getUserDetails().get(APIUrls.KEY_HOUSE);
        getsocity = sessionManagement.getUserDetails().get(APIUrls.KEY_SOCITY_ID);
        String getsocity_name = sessionManagement.getUserDetails().get(APIUrls.KEY_SOCITY_NAME);

        et_name.setText(getname);
        et_phone.setText(getphone);

        /*if (!TextUtils.isEmpty(getsocity_name)) {
            btn_socity.setText(getsocity_name);
        }*/

        if (!TextUtils.isEmpty(getimage)) {

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(this)
                    .load(APIUrls.IMG_PROFILE_URL + getimage)
                    .apply(requestOptions)
                    .into(iv_profile);
        }

        if (!TextUtils.isEmpty(getemail)) {
            et_email.setText(getemail);
        }

        /*if (!TextUtils.isEmpty(gethouse)){
            et_house.setText(gethouse);
        }*/

        btn_update.setOnClickListener(this);
        //btn_socity.setOnClickListener(this);
        iv_profile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_pro_edit) {
            attemptEditProfile();
        } /*else if (id == R.id.btn_pro_socity) {

            String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {

                Bundle args = new Bundle();
                Fragment fm = new SoCityFragment();
                args.putString("pincode", getpincode);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }else{
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }

        } */ else if (id == R.id.iv_pro_img) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE1);
        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        /*tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));*/

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));
        /*tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));*/

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getemail = et_email.getText().toString();
        /*String gethouse = et_house.getText().toString();
        String getsocity = sessionManagement.getUserDetails().get(APIAPIUrls.KEY_SOCITY_ID);*/

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.invalid_phone_number));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_email;
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(APIUrls.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    new editProfile(user_id, getname, getphone).execute();
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if ((requestCode == GALLERY_REQUEST_CODE1)) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    //filePath = imgDecodableString;

                    Bitmap b = BitmapFactory.decodeFile(imgDecodableString);
                    Bitmap out = Bitmap.createScaledBitmap(b, 1200, 1024, false);

                    //getting iv_category from gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);


                    File file = new File(imgDecodableString);
                    filePath = file.getAbsolutePath();
                    FileOutputStream fOut;
                    try {
                        fOut = new FileOutputStream(file);
                        out.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                        fOut.flush();
                        fOut.close();
                        //b.recycle();
                        //out.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (requestCode == GALLERY_REQUEST_CODE1) {

                        // Set the Image in ImageView after decoding the String
                        iv_profile.setImageBitmap(bitmap);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // asynctask for upload data with iv_category or not iv_category using HttpOk
    public class editProfile extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        JSONParser jsonParser;
        ArrayList<NameValuePair> nameValuePairs;
        boolean response;
        String error_string, success_msg;

        String getphone;
        String getname;
        String getpin;
        String gethouse;
        String getsocity;
        String getimage;

        public editProfile(String user_id, String name, String phone) {

            nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new NameValuePair("user_id", user_id));
            nameValuePairs.add(new NameValuePair("user_fullname", name));
            nameValuePairs.add(new NameValuePair("user_mobile", phone));
            /*nameValuePairs.add(new NameValuePair("pincode", pincode));
            nameValuePairs.add(new NameValuePair("socity_id", socity_id));
            nameValuePairs.add(new NameValuePair("house_no", house));*/

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.uploading_profile_data), true);
            jsonParser = new JSONParser(getActivity());
        }

        @Override
        protected Void doInBackground(Void... params) {

            String json_responce = null;
            try {
                if (filePath == "") {
                    json_responce = jsonParser.execPostScriptJSON(APIUrls.EDIT_PROFILE_URL, nameValuePairs);
                } else {
                    json_responce = jsonParser.execMultiPartPostScriptJSON(APIUrls.EDIT_PROFILE_URL, nameValuePairs, "iv_category/png", filePath, "image");
                }
                Log.e(TAG, json_responce + "," + filePath);

                JSONObject jObj = new JSONObject(json_responce);
                if (jObj.getBoolean("responce")) {
                    response = true;
                    //success_msg = jObj.getString("data");

                    JSONObject obj = jObj.getJSONObject("data");

                    getphone = obj.getString("user_phone");
                    getname = obj.getString("user_fullname");
                    getpin = obj.getString("pincode");
                    gethouse = obj.getString("house_no");
                    getsocity = obj.getString("socity_id");
                    getimage = obj.getString("user_image");

                } else {
                    response = false;
                    error_string = jObj.getString("error");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.hide();
                progressDialog.dismiss();
                progressDialog = null;
            }

            if (response) {

                sessionManagement.updateData(getname, getphone, getpin, getsocity, getimage, gethouse);

                ((MainActivity) getActivity()).updateHeader();

                Toast.makeText(getActivity(), getResources().getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "" + error_string, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (progressDialog != null) {
                progressDialog.hide();
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_change_password:
                Fragment fm = new ChangePasswordFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                        .addToBackStack(null).commit();
                return false;
        }
        return false;
    }

}
