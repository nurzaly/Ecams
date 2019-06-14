package my.gov.ilpsdk.apps.ecams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import my.gov.ilpsdk.apps.ecams.data.Constant;
import my.gov.ilpsdk.apps.ecams.data.Tools;
import my.gov.ilpsdk.apps.ecams.model.Assets;
import my.gov.ilpsdk.apps.ecams.model.Locations;
import my.gov.ilpsdk.apps.ecams.services.GetSharePref;

import static android.content.ContentValues.TAG;

public class Home2Activity extends AppCompatActivity {

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.staf_name)
    TextView staf_name;

    @BindView(R.id.bahagian)
    TextView bahagian;

    @BindView(R.id.count_hartamodal)
    TextView count_hartamodal;

    @BindView(R.id.count_inventori)
    TextView count_inventori;

    @BindView(R.id.progress_bar_hartamodal)
    ProgressBar progress_bar_hartamodal;

    @BindView(R.id.progress_bar_inventori)
    ProgressBar progress_bar_inventori;

    @BindView(R.id.card_hartamodal)
    CardView card_hartamodal;

    @BindView(R.id.card_inventori)
    CardView card_inventori;

    @BindView(R.id.card_pengimbas)
    CardView card_pengimbas;

    @BindView((R.id.card_search))
    CardView card_search;

    private GetSharePref myconfig;
    private String email;
    private String bahagians;
    private String nama;
    private Toast toast;
    private List<Locations> locations;
    private String user_level;
    private CardView card_pegawai_pemeriksa;
    private  CardView card_kewpa11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        myconfig = new GetSharePref(getApplicationContext());
        user_level = myconfig.getString(Constant.KEY_AMOS, "staf");
        email = myconfig.getString(Constant.KEY_EMAIL, null);
        bahagians = myconfig.getString(Constant.KEY_BAHAGIAN,"bahagian");
        nama = myconfig.getString(Constant.KEY_NAMA,"nama");
        initToolbar();

        if(user_level.equals("staf")){
            setContentView(R.layout.activity_home_staf);
        }
        else{
            setContentView(R.layout.activity_home2);

            card_pegawai_pemeriksa = (CardView)findViewById(R.id.card_pegawai_pemeriksa);
            card_kewpa11 = (CardView)findViewById(R.id.card_kewpa11);

            card_pegawai_pemeriksa.setOnClickListener(clickListener);
            card_kewpa11.setOnClickListener(clickListener);
        }

        // TODO: 14/6/2019  

        ButterKnife.bind(this);
        card_hartamodal.setOnClickListener(clickListener);
        card_inventori.setOnClickListener(clickListener);
        card_pengimbas.setOnClickListener(clickListener);
        card_search.setOnClickListener(clickListener);

        set_profile();
        count_user_aset();
    }

    private void scanner() {
        Intent intent = new Intent(Home2Activity.this, ScannerActivity.class);
        startActivity(intent);
    }

    public void showInputDialog() {
        new MaterialDialog.Builder(this)
                .title("Carian Aset")
                .content("Sila taip no siri, jenis aset, lokasi aset")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 16)
                .positiveText("Cari")
                .input(
                        "No Siri Aset, Jenis Aset, Kod Lokasi Aset",
                        "",
                        false,
                        (dialog, input) -> searchAssets(input.toString()))
                .show();
    }

//    private void startScan() {
//        /**
//         * Build a new MaterialBarcodeScanner
//         */
//        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
//                .withActivity(HomeActivity.this)
//                .withEnableAutoFocus(true)
//                .withBleepEnabled(true)
//                .withBackfacingCamera()
//                .withText("AMOSILPSDK : Scanning...")
//                .withCenterTracker()
//                .withBarcodeFormats(Barcode.CODE_128)
//                .withOnly2DScanning()
//                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
//                    @Override
//                    public void onResult(Barcode barcode) {
//                        findAssets(barcode.rawValue);
//                    }
//                })
//                .build();
//        materialBarcodeScanner.startScan();
//    }

    public void findAssets(String barcode){
        final ProgressDialog progressDialog = new ProgressDialog(Home2Activity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Carian item...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_FIND_BARCODE)
                .addBodyParameter("barcode",barcode)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Assets.class, new ParsedRequestListener<List<Assets>>() {
                    @Override
                    public void onResponse(List<Assets> response) {
                        progressDialog.dismiss();
                        if(response.size() == 1){
                            Constant.assets = response.get(0);
                            AssetsDetailsActivity.navigate((AppCompatActivity) Home2Activity.this, response.get(0));
                        }
                        else if(response.size() > 1){
                            Toast.makeText(getApplicationContext(),"More than one assets found",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Barcode not found",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        progressDialog.dismiss();
                        Log.d(TAG, "onError: " + anError.getMessage());
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }

    private void page_pegawai_aset(){
        Intent intent = new Intent(this,LocationsActivity.class);
        startActivity(intent);
    }

    private void count_user_aset() {
        if (Constant.HARTAMODAL == -1) {
            final ProgressDialog progressDialog = new ProgressDialog(Home2Activity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            AndroidNetworking.post(Constant.URL_COUNT_USER_ASSETS)
                    .addBodyParameter("nama",nama)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                count_hartamodal.setText(response.getString("harta-modal"));
                                count_hartamodal.setVisibility(View.VISIBLE);
                                progress_bar_hartamodal.setVisibility(View.GONE);

                                count_inventori.setText(response.getString("inventori"));
                                count_inventori.setVisibility(View.VISIBLE);
                                progress_bar_inventori.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Log.d("zaly", "onError: " + anError.getMessage());
                            Log.d("zaly", "onError: " + anError.getErrorBody());
                            Log.d("zaly", "onError: " + anError.getErrorDetail());
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {

        }
    }

    private void set_profile(){
        try{
            if(myconfig.getBoolean(Constant.KEY_LOGIN,false) == true){
                String avatar_url = myconfig.getString(Constant.KEY_AVATAR, "avatar");
                staf_name.setText(myconfig.getString(Constant.KEY_NAMA, "Nama"));
                //bahagian.setText(myconfig.getString(Constant.KEY_NAMA_BAHAGIAN, "Nama Bahagian"));
                Picasso.with(getApplicationContext()).load(avatar_url).transform(new CropCircleTransformation()).into(avatar);
            }
            else{
                Log.d(TAG, "set_profile: online");
                get_profile();
            }
        }
        catch (Exception e){
            get_profile();
        }
    }

    private void get_profile() {

        final ProgressDialog progressDialog = new ProgressDialog(Home2Activity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.get(Constant.URL_STAF_SINGLE)
                .addQueryParameter(Constant.KEY_EMAIL, email)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String avatar_url = response.getString("avatar");
                            String staf_names = response.getString("name");
                            String nama_bahagian = response.getString("nama_bahagian");
                            Picasso.with(getApplicationContext()).load(avatar_url).transform(new CropCircleTransformation()).into(avatar);
                            staf_name.setText(staf_names);
                            //bahagian.setText(nama_bahagian);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d("zaly", "onError: " + anError.getMessage());
                        Log.d("zaly", "onError: " + anError.getErrorBody());
                        Log.d("zaly", "onError: " + anError.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void set_profile_offline(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        SharedPreferences.Editor editor = myconfig.edit();
        editor.remove(Constant.KEY_EMAIL);
        editor.remove(Constant.KEY_LOGIN);
        editor.remove(Constant.KEY_BAHAGIAN);
        //editor.remove(Const.KEY_INSTALL);
        boolean success = editor.commit();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void get_locations(String block) {
        final ProgressDialog progressDialog = new ProgressDialog(Home2Activity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.get(Constant.URL_GET_LOCATION+"?type=block")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Locations.class, new ParsedRequestListener<List<Locations>>() {
                    @Override
                    public void onResponse(List<Locations> response) {
                        locations = response;
                        String[] lokasi_array = new String[response.size()];
                        for(int i = 0; i < response.size(); i++){
                            lokasi_array[i] =  response.get(i).getBlock_code() + "-" + response.get(i).getBlock_name();
                        }
                        show_dialog_location(lokasi_array, block);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onError: " + anError.getMessage());
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }

    private void view_kewpa(String kewpa, int which){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.DOMAIN_AMOS + kewpa +"?bangunan=" + locations.get(which).getShort_name()));
        startActivity(browserIntent);
    }

    public void show_dialog_location(String[] lokasi, String kewpa) {

        new MaterialDialog.Builder(this)
                .title("Senarai Lokasi")
                .items(lokasi)
                .itemsCallback((dialog, view, which, text) -> view_kewpa(kewpa,which))
                .positiveText(android.R.string.cancel)
                .show();
    }

    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void searchAssets(String search_key){
        AssetListActivity.navigate(Home2Activity.this, Constant.URL_SEARCH_ASSETS+"?search_key="+search_key, "Senarai Aset");
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.deep_purple_500), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.card_hartamodal:
                    AssetListActivity.navigate(Home2Activity.this, Constant.URL_ASSETS_JENIS+"?jenis_aset=H&nama="+nama, "Senarai Harta Modal");
                    break;
                case R.id.card_inventori:
                    AssetListActivity.navigate(Home2Activity.this, Constant.URL_ASSETS_JENIS+"?jenis_aset=I&nama="+nama, "Senarai Inventori");
                    break;
                case R.id.card_pegawai_pemeriksa:
                    page_pegawai_aset();
                    break;
                case R.id.card_pengimbas:
                    //new RunBarcodeScanner().launchActivity(FullScannerFragmentActivityZxing.class, HomeActivity.this, HomeActivity.this, 2);
                    //startScan();
                    scanner();
                    break;
                case R.id.card_search:
                    showInputDialog();
                    break;
                case R.id.card_kewpa11:
                    get_locations("kewpa11");
                    break;
            }
        }
    };
}