package codecanyon.grocery.activities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codecanyon.grocery.adapter.ExpandableListAdapter;
import codecanyon.grocery.DADApp;
import codecanyon.grocery.R;
import codecanyon.grocery.fcm.FirebaseRegister;
import codecanyon.grocery.fragments.CartFragment;
import codecanyon.grocery.fragments.CategoryFragment;
import codecanyon.grocery.fragments.EditProfileFragment;
import codecanyon.grocery.fragments.HomeFragment;
import codecanyon.grocery.fragments.MyOrderFragment;
import codecanyon.grocery.fragments.OffersFragment;
import codecanyon.grocery.fragments.SearchFragment;
import codecanyon.grocery.fragments.SupportInfoFragment;
import codecanyon.grocery.models.Category;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.BottomNavigationViewHelper;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.DatabaseHandler;
import codecanyon.grocery.util.SessionManagement;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView totalBudgetCount, tv_number;
    private ImageButton tv_name, tv_register;
    private ImageView iv_profile;
    private boolean doubleBackToExit;
    private DatabaseHandler dbcart;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SessionManagement sessionManagement;
    private Menu nav_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        BottomNavigationView navigation = findViewById(R.id.btm_navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dbcart = new DatabaseHandler();

        checkConnection();
        sessionManagement = new SessionManagement(MainActivity.this);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        iv_profile = header.findViewById(R.id.iv_header_img);
        tv_name = header.findViewById(R.id.tv_header_name);
        tv_register = header.findViewById(R.id.tv_header_Register);
        tv_number = header.findViewById(R.id.tv_header_moblie);

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(4);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.menu_cart_layout, bottomNavigationMenuView, false);
        totalBudgetCount = badge.findViewById(R.id.actionbar_notifcation_textview);

        totalBudgetCount.setText(String.valueOf(dbcart.getCartCount()));
        itemView.addView(badge);

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    Fragment fm = new EditProfileFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                            .addToBackStack(null).commit();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        updateHeader();
        sideMenu();

        if (savedInstanceState == null) {
            Fragment fm = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, fm, HomeFragment.class.getSimpleName())
                    .addToBackStack(HomeFragment.class.getSimpleName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {

                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    Fragment fr = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);
                    if (fm_name.contentEquals(HomeFragment.class.getSimpleName())) {


                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        toggle.setDrawerIndicatorEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();

                    } else if (fm_name.contentEquals("MyOrderFragment") ||
                            fm_name.contentEquals("ThanksFragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment fm = new HomeFragment();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                                        .addToBackStack(null).commit();
                            }
                        });
                    } else {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                onBackPressed();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        if (sessionManagement.getUserDetails().get(APIUrls.KEY_ID) != null && !sessionManagement.getUserDetails().get(APIUrls.KEY_ID).equalsIgnoreCase("")) {
            FirebaseRegister fireReg = new FirebaseRegister(this);
            fireReg.RegisterUser(sessionManagement.getUserDetails().get(APIUrls.KEY_ID));
        }
    }

    public void updateHeader() {
        if (sessionManagement.isLoggedIn()) {
            String getname = sessionManagement.getUserDetails().get(APIUrls.KEY_NAME);
            String getimage = sessionManagement.getUserDetails().get(APIUrls.KEY_IMAGE);
            String email = sessionManagement.getUserDetails().get(APIUrls.KEY_EMAIL);

            Glide.with(this)
                    .load(APIUrls.IMG_PROFILE_URL + getimage)
                    .apply(RequestOptions.placeholderOf(R.drawable.logo))
                    .into(iv_profile);

            tv_number.setText(email);
        }
    }

    public void sideMenu() {

        if (sessionManagement.isLoggedIn()) {
            tv_number.setVisibility(View.VISIBLE);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
            nav_menu.findItem(R.id.nav_user).setVisible(true);
            tv_name.setVisibility(View.GONE);
            tv_register.setVisibility(View.GONE);
        } else {
            tv_number.setVisibility(View.GONE);
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });

            tv_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            });
            nav_menu.findItem(R.id.nav_logout).setVisible(false);
            nav_menu.findItem(R.id.nav_user).setVisible(false);
        }
    }

    public void setFinish() {
        finish();
    }

    public void setCartCounter(String totalitem) {
        totalBudgetCount.setText(totalitem);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_cart);
        final MenuItem search_nav = menu.findItem(R.id.tv_search_bar);
        MenuItem c_password = menu.findItem(R.id.action_change_password);
        MenuItem search = menu.findItem(R.id.action_search);
        item.setVisible(false);
        c_password.setVisible(false);
        search.setVisible(false);
        /*View count = item.getActionView();
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });
        totalBudgetCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);

        totalBudgetCount.setText("" + dbcart.getCartCount());*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fm = null;
        Bundle args = new Bundle();
        FragmentManager fM = getSupportFragmentManager();

        switch (id) {
            case R.id.nav_home:
                Fragment fm_home = new HomeFragment();
                fM.beginTransaction()
                        .replace(R.id.frame_layout, fm_home, HomeFragment.class.getSimpleName())
                        .addToBackStack(HomeFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;

            case R.id.nav_myorders:
                Fragment myOrderFragment = new MyOrderFragment();
                fM.beginTransaction()
                        .replace(R.id.frame_layout, myOrderFragment, "MyOrderFragment")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;

            case R.id.nav_myprofile:
                fm = new EditProfileFragment();
                break;

            case R.id.nav_support:
                fm = new SupportInfoFragment();
                args.putString("url", APIUrls.GET_SUPPORT_URL);
                args.putString("tv_subcat_title", getResources().getString(R.string.nav_support));
                fm.setArguments(args);
                break;

            case R.id.nav_aboutus:
                fm = new SupportInfoFragment();
                args.putString("url", APIUrls.GET_ABOUT_URL);
                args.putString("tv_subcat_title", getResources().getString(R.string.nav_about));
                fm.setArguments(args);
                break;

            case R.id.nav_policy:
                fm = new SupportInfoFragment();
                args.putString("url", APIUrls.GET_TERMS_URL);
                args.putString("tv_subcat_title", getResources().getString(R.string.nav_terms));
                fm.setArguments(args);
                break;

            case R.id.nav_review:
                reviewOnApp();
                break;

            case R.id.nav_logout:
                sessionManagement.logoutSession();
                finish();
                break;

            case R.id.nav_category:
                fm = new CategoryFragment();
                args.putString("url", APIUrls.GET_CATEGORY_URL);
                args.putString("tv_subcat_title", getResources().getString(R.string.nav_category));
                fm.setArguments(args);
                break;
            case R.id.offers:
                break;
        }

        if (fm != null) {
            fM.beginTransaction().replace(R.id.frame_layout, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + "http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back tv_subcat_add,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        DADApp.getInstance().setConnectivityListener(this);

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(APIUrls.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(APIUrls.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fM = getSupportFragmentManager();

            if (fM.getBackStackEntryCount() > 1) {
                fM.popBackStackImmediate();
            } else if (!doubleBackToExit) {
                doubleBackToExit = true;
                Toast.makeText(this, R.string.press_again_warn, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExit = false;
                    }
                }, 2000);
            } else {
                finish();
            }
        }
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;

        if (!isConnected) {
            message = "" + getResources().getString(R.string.no_internet);
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.coordinatorlayout), message, Snackbar.LENGTH_LONG)
                /*.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })*/;

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(APIUrls.PREFS_NAME, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            //txtRegId.setText("Firebase Reg Id: " + regId);
        } else {
            //txtRegId.setText("Firebase Reg Id is not received yet!");
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
            int id = item.getItemId();
            FragmentManager fM = getSupportFragmentManager();
            FragmentTransaction fT = fM.beginTransaction();

            switch (id) {
                default:
                case R.id.home:

                    if (fM.findFragmentByTag(HomeFragment.class.getSimpleName()) == null) {
                        Fragment fm_home = new HomeFragment();
                        fT.replace(R.id.frame_layout, fm_home, HomeFragment.class.getSimpleName())
                                .addToBackStack(HomeFragment.class.getSimpleName())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    } else {
                        Fragment homeFragment = fM.findFragmentByTag(HomeFragment.class.getSimpleName());

                        if (homeFragment != null && homeFragment instanceof HomeFragment) {
                            fT.replace(R.id.frame_layout, homeFragment, HomeFragment.class.getSimpleName())
                                    .addToBackStack(HomeFragment.class.getSimpleName())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();
                        }
                    }

                    break;
                case R.id.offers:

                    if (fM.findFragmentByTag(OffersFragment.class.getSimpleName()) == null) {
                        Fragment offersFragment = new OffersFragment();
                        fT.replace(R.id.frame_layout, offersFragment, OffersFragment.class.getSimpleName())
                                .addToBackStack(OffersFragment.class.getSimpleName())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    } else {
                        Fragment offerFragment = fM.findFragmentByTag(OffersFragment.class.getSimpleName());

                        if (offerFragment != null && offerFragment instanceof OffersFragment) {
                            fT.replace(R.id.frame_layout, offerFragment, OffersFragment.class.getSimpleName())
                                    .addToBackStack(OffersFragment.class.getSimpleName())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();
                        }
                    }

                    break;
                case R.id.categories:

                    if (fM.findFragmentByTag(CategoryFragment.class.getSimpleName()) == null) {
                        Fragment fragmentCategory = new CategoryFragment();
                        fT.replace(R.id.frame_layout, fragmentCategory, CategoryFragment.class.getSimpleName())
                                .addToBackStack(CategoryFragment.class.getSimpleName())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    } else {
                        Fragment fragmentCategory = fM.findFragmentByTag(CategoryFragment.class.getSimpleName());

                        if (fragmentCategory != null && fragmentCategory instanceof CategoryFragment) {
                            fT.replace(R.id.frame_layout, fragmentCategory, CategoryFragment.class.getSimpleName())
                                    .addToBackStack(CategoryFragment.class.getSimpleName())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();
                        }
                    }

                    break;
                case R.id.search:

                    if (fM.findFragmentByTag(SearchFragment.class.getSimpleName()) == null) {
                        Fragment searchFragment = new SearchFragment();
                        fT.replace(R.id.frame_layout, searchFragment, SearchFragment.class.getSimpleName())
                                .addToBackStack(SearchFragment.class.getSimpleName())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    } else {
                        Fragment searchFragment = fM.findFragmentByTag(SearchFragment.class.getSimpleName());

                        if (searchFragment != null && searchFragment instanceof SearchFragment) {
                            fT.replace(R.id.frame_layout, searchFragment, SearchFragment.class.getSimpleName())
                                    .addToBackStack(SearchFragment.class.getSimpleName())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();
                        }
                    }

                    break;
                case R.id.cart:

                    if (fM.findFragmentByTag(CartFragment.class.getSimpleName()) == null) {
                        Fragment cartFragment = new CartFragment();
                        fT.replace(R.id.frame_layout, cartFragment, CartFragment.class.getSimpleName())
                                .addToBackStack(CartFragment.class.getSimpleName())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    } else {
                        Fragment cartFragment = fM.findFragmentByTag(CartFragment.class.getSimpleName());

                        if (cartFragment != null && cartFragment instanceof CartFragment) {
                            fT.replace(R.id.frame_layout, cartFragment, CartFragment.class.getSimpleName())
                                    .addToBackStack(CartFragment.class.getSimpleName())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();
                        }
                    }

                    break;
            }

            return false;
        }
    };
}