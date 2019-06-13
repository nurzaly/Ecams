package my.gov.ilpsdk.apps.ecams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import my.gov.ilpsdk.apps.ecams.data.Constant;
import my.gov.ilpsdk.apps.ecams.fragment.AssetsFragment;


public class AssetListActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    public static final String EXTRA_OBJCT = "my.gov.ilpsdk.apps.amos";
    public static final String EXTRA_JENIS_ASET = "jenisaset";
    private Fragment mContent;
    private String url;
    private String title;

    private SharedPreferences myconfig;

    public static void navigate(AppCompatActivity activity, String url, String title) {
        Intent intent = new Intent(activity, AssetListActivity.class);
        intent.putExtra(EXTRA_OBJCT, url);
        intent.putExtra(EXTRA_JENIS_ASET, title);
        //intent.putExtra(EXTRA_OBJCT, jenis_aset);
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        myconfig = getSharedPreferences(Constant.MYCONFIG, MODE_PRIVATE);

        url = getIntent().getStringExtra(EXTRA_OBJCT);
        title = getIntent().getStringExtra(EXTRA_JENIS_ASET);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        displayContentView(0);
    }

    private void displayContentView(int id) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_URL,url);

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
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void asset_list_fragment(){

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
