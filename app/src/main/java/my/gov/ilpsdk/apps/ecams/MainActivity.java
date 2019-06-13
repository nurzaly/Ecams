package my.gov.ilpsdk.apps.ecams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import my.gov.ilpsdk.apps.ecams.data.Constant;
import my.gov.ilpsdk.apps.ecams.data.GlobalVariable;
import my.gov.ilpsdk.apps.ecams.fragment.AssetsFragment;
import my.gov.ilpsdk.apps.ecams.model.Assets;
import my.gov.ilpsdk.apps.ecams.model.Locations;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    public ActionBar actionBar;
    private NavigationView navigationView;
    private GlobalVariable global;
    private SharedPreferences myconfig;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        global = (GlobalVariable) getApplication();

        initToolbar();
        //initDrawerMenu();

        //setTitleBar("Unset Assets");

        myconfig = getSharedPreferences(Constant.MYCONFIG, MODE_PRIVATE);
        //logout();
        if (!get_versions().equals("2.0")) {
            logout();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);
    }

    @Override
    protected void onResume() {
        //asset_list_activity();
        //asset_list_fragment();
        check_login();
        //page_assets_details();
        //page_pegawai_aset();
        //Log.d(TAG, "onResume: " + "test");
        //pull_data();

        super.onResume();
    }

    public void page_assets_details(){
        Assets obj;
        Locations locations;
        locations = new Locations(1,"location_code","level","short_name","block_name","location_name");
        obj = new Assets(1,"no siri pendaftaran","barcode","kategori","sub_kategori","jenis","jenis_aset","no_casis","kod_lokasi",locations,"pegawai",2);
        AssetsDetailsActivity.navigate((AppCompatActivity) this, obj);
    }

    private String get_versions() {
        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    private void asset_list_activity() {
        Intent intent = new Intent(this, AssetListActivity.class);
        startActivity(intent);
    }

    private void check_login() {

        boolean login = myconfig.getBoolean(Constant.KEY_LOGIN, false);

        if (login == false) {
            login();
        } else {
            //displayContentView(Constant.FRAGMENT_ID);
            home();
            //scanner();
            //page_assets_details();

        }
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void home() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void scanner() {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
        finish();
    }


    private void page_pegawai_aset() {
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
        finish();
    }




    private void logout() {
        SharedPreferences.Editor editor = myconfig.edit();
        editor.remove(Constant.KEY_EMAIL);
        editor.remove(Constant.KEY_LOGIN);
        editor.remove(Constant.KEY_BAHAGIAN);
        //editor.remove(Const.KEY_INSTALL);
        boolean success = editor.commit();
        login();
    }

    private void displayContentView(int id) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_BAHAGIAN, myconfig.getString(Constant.KEY_BAHAGIAN, null));
        //fragment = new AssetsFragment();

        switch (id) {
            case 0:
                fragment = new AssetsFragment();
                //fragment = new FullScannerFragment();
                //launchActivity(FullScannerFragmentActivity.class);
                break;
        }
        fragment.setArguments(bundle);
        mContent = fragment;
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);
    }

    private void updateDrawerCounter() {
        //setMenuAdvCounter(R.id.nav_inbox, global.getInbox().size());
        //setMenuStdCounter(R.id.nav_trash, global.getTrash().size());
    }

    private void setMenuStdCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                updateDrawerCounter();
                hideKeyboard();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawer.closeDrawers();
                actionBar.setTitle(menuItem.getTitle());
                displayContentView(menuItem.getItemId());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

        }

        return super.onOptionsItemSelected(item);
    }


}
