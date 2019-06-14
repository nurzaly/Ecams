package my.gov.ilpsdk.apps.ecams;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import my.gov.ilpsdk.apps.ecams.data.Constant;
import my.gov.ilpsdk.apps.ecams.data.GlobalVariable;
import my.gov.ilpsdk.apps.ecams.model.Assets;
import my.gov.ilpsdk.apps.ecams.model.Locations;
import my.gov.ilpsdk.apps.ecams.model.Staff;
import my.gov.ilpsdk.apps.ecams.services.GetSharePref;

import static android.content.ContentValues.TAG;


public class AssetsDetailsActivity extends AppCompatActivity{

    @BindView(R.id.desc1)
    TextView desc1;

    @BindView(R.id.desc2)
    TextView desc2;

    @BindView(R.id.desc3)
    TextView desc3;

    @BindView(R.id.desc4)
    TextView desc4;

    @BindView(R.id.keterangan_assets)
    TextView keterangan_assets;

    @BindView(R.id.tv_lokasi_sebenar_info)
    TextView tv_lokasi_sebenar_info;

    @BindView(R.id.tv_keadaan_harta_modal_info)
    TextView tv_keadaan_harta_modal_info;

    @BindView(R.id.tv_catatan_info)
    TextView tv_catatan_info;

    @BindView(R.id.tv_tarikh_pemeriksaan)
    TextView tv_tarikh_pemeriksaan;

    @BindView(R.id.tv_pemeriksa)
    TextView tv_pemeriksa;

    @BindView(R.id.ly_pemeriksaan_data)
    LinearLayout ly_pemeriksaan_data;

    @BindView(R.id.pb_pemeriksaan_data)
    ProgressBar pb_pemeriksaan_data;

    @BindView(R.id.fab)
    FloatingActionButton fab;

//    @BindView(R.id.fab_barcode)
//    com.github.clans.fab.FloatingActionButton  fab_barcode;
//
//    @BindView(R.id.fab_location)
//    com.github.clans.fab.FloatingActionButton fab_location;
//
//    @BindView(R.id.fab_pic)
//    com.github.clans.fab.FloatingActionButton fab_pic;

    private static final String TAG_RESET_BARCODE = "reset_barcode";
    private static final String TAG_UPDATE_LOCATION = "update_location";
    private static final String TAG_UPDATE_PIC = "update_pic";
    private static final String TAG_UPDATE_PEGAWAI_PEMERIKSA = "update_pegawai_pemeriksa";
    private static final String TAG_LOKASI_SEBENAR = "lokasi_sebenar";
    public static final String EXTRA_OBJCT = "my.gov.ilpsdk.apps.amos";

    private Locations locations_selected;
    private Staff staff_selected;
    private static final String page_from = null;
    private GetSharePref myconfig;
    private Dialog dialog;
    private JSONObject pemeriksaan_data = new JSONObject();
    private TextView tv_lokasi_sebenar, tv_status, tv_catatan;
    private Verification veri_data;
    private Toast toast;
    private Status status;
    private String status_code;

    private List<FloatingActionMenu> menus = new ArrayList<>();


    public static void navigate(AppCompatActivity activity, Assets obj) {
        Intent intent = new Intent(activity, AssetsDetailsActivity.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, null);
    }

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Assets assets;
    private View parent_view;


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_details);
        //parent_view = findViewById(android.R.id.content);\


        // bind the view using butterknife
        ButterKnife.bind(this);


        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);
        ly_pemeriksaan_data.setVisibility(View.GONE);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Maklumat Item");

        myconfig = new GetSharePref(getApplicationContext());

        assets = (Assets) getIntent().getSerializableExtra(EXTRA_OBJCT);
        veri_data = new Verification();
        status = new Status();
        veri_data.asset_id = assets.getId();
        veri_data.jenis = assets.getJenis();
        veri_data.location = assets.getKod_lokasi();
        veri_data.pemeriksa = myconfig.getString(Constant.KEY_EMAIL,"email");


        ImageView ivImage = (ImageView) findViewById(R.id.avatar);
        desc1.setText(assets.getBarcode());
        desc2.setText(assets.getNo_siri_pendaftaran());
        desc3.setText(assets.getLokasi().getTitle());
        desc4.setText(assets.getPegawai());
        keterangan_assets.setText((assets.getSub_kategori() + " - " + assets.getJenis() + GlobalVariable.get_no_casis(assets.getNo_casis())).toUpperCase());

        Picasso.with(this).load(R.drawable.amos).transform(new CropCircleTransformation()).into(ivImage);


        fab.setVisibility(View.GONE);
        if(myconfig.getString(Constant.KEY_AMOS,"staf").equals("admin")){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(clickListener);
        }

        //((ImageView) findViewById(R.id.image)).setImageResource(friend.getPhoto());
//        ((Button) findViewById(R.id.bt_view_photos)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(parent_view, "View Photos Clicked ", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        ((Button) findViewById(R.id.bt_view_gallery)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(parent_view, "View Gallery Clicked ", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        show_pemeriksaan_aset();
        get_maklumat_pemeriksaan();

    }

    public void get_maklumat_pemeriksaan() {
        AndroidNetworking.get(Constant.URL_GET_PEMERIKSAAN_DATA)
                .setTag(this)
                .addQueryParameter("id", String.valueOf(assets.getId()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {

                                if (!response.getJSONObject("data").equals(null)) {

                                    ly_pemeriksaan_data.setVisibility(View.VISIBLE);
                                    tv_tarikh_pemeriksaan.setText(": " + response.getJSONObject("data").getString("updated_at"));
                                    tv_lokasi_sebenar_info.setText(": " + response.getJSONObject("data").getString("kod_lokasi_sebenar"));
                                    //tv_lokasi_sebenar.setText(response.getJSONObject("data").getString("kod_lokasi_sebenar"));

                                    tv_keadaan_harta_modal_info.setText(":" + set_status(response.getJSONObject("data").getString("status")));
                                    //tv_status.setText(set_status(response.getJSONObject("data").getString("status")));

                                    tv_pemeriksa.setText(": " + response.getJSONObject("data").getString("pemeriksa"));

                                    tv_catatan_info.setText(": " + response.getJSONObject("data").getString("catatan"));
                                    //tv_catatan.setText(response.getJSONObject("data").getString("catatan"));
                                }
                                else{
                                    ly_pemeriksaan_data.setVisibility(View.GONE);
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan pemeriksaan data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pb_pemeriksaan_data.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Ralat pada server", Toast.LENGTH_SHORT).show();
                        pb_pemeriksaan_data.setVisibility(View.GONE);
                        Log.d(TAG, "onError: " + anError.getMessage());
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }

    private String set_status(String status_code){
        StringBuilder str = new StringBuilder();
        this.status_code = status_code;

        for(int i=0;i<getResources().getStringArray(R.array.keadaan_harta_modal).length;i++){
            if(status_code.contains(i+"")){
                if(str.length() != 0){
                    str.append(", ");
                }
                str.append(getResources().getStringArray(R.array.keadaan_harta_modal)[i]);
            }
        }
        return str.toString();
    }
    private Integer[] set_current_status(){

        try {
            String[] str =  this.status_code.split("(?!^)");
            Integer[] intArray = new Integer[this.status_code.length()];
            for(int i=0;i<this.status_code.length();i++){
                intArray[i] = Integer.parseInt(str[i]);
            }
            return intArray;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Integer[]{};
    }

    public void show_pemeriksaan_aset() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.cus_dialog_pemeriksaan_aset);
        dialog.setTitle("Maklumat Pemeriksaan Aset");
        //ButterKnife.bind(dialog);

//        // set the custom dialog components - text, image and button
//        TextView text = (TextView) dialog.findViewById(R.id.text);
//        text.setText("Android custom dialog example!");
//        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.ic_launcher);
//
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_custom_dialog_simpan);

        tv_lokasi_sebenar = (TextView) dialog.findViewById(R.id.tv_lokasi_sebenar);
        tv_status = (TextView) dialog.findViewById(R.id.tv_status);
        tv_catatan = (TextView) dialog.findViewById(R.id.tv_catatan);

        if(ly_pemeriksaan_data.getVisibility() == 0){
            tv_lokasi_sebenar.setText(tv_lokasi_sebenar_info.getText().toString().replace(":","").replace("null",""));
            tv_status.setText(tv_keadaan_harta_modal_info.getText().toString().replace(":","").replace("null",""));
            tv_catatan.setText(tv_catatan_info.getText().toString().replace(":","").replace("null",""));
        }

        dialogButton.setOnClickListener(clickListener);
        tv_lokasi_sebenar.setOnClickListener(clickListener);
        tv_status.setOnClickListener(clickListener);
        tv_catatan.setOnClickListener(clickListener);

        dialog.show();
    }



    private void provideSimpleDialogLocation(final ArrayList<Locations> locations) {
        SimpleSearchDialogCompat dialog = new SimpleSearchDialogCompat(AssetsDetailsActivity.this, "Carian lokasi...",
                "Taip nama lokasi yang dicari...", null, locations,
                new SearchResultListener<Locations>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat dialog, Locations item, int position) {
                        locations_selected = item;

                        set_lokasi_sebenar();
                        dialog.dismiss();

                    }

                });
        dialog.show();
        dialog.getSearchBox().setTypeface(Typeface.SERIF);
    }


    private void list_locations() {
        final ProgressDialog progressDialog = new ProgressDialog(AssetsDetailsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.get(Constant.URL_GET_LOCATION)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Locations.class, new ParsedRequestListener<ArrayList<Locations>>() {
                    @Override
                    public void onResponse(ArrayList<Locations> response) {
                        progressDialog.dismiss();
                        Constant.locations = response;
                        provideSimpleDialogLocation(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onError: " + anError.getMessage());
                    }
                });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        assets = Constant.assets;
        desc1.setText(assets.getBarcode());
        super.onResume();
    }

    private void set_lokasi_sebenar() {
        tv_lokasi_sebenar.setText(locations_selected.getBlock_name());
        veri_data.location = locations_selected.getLocation_code();
    }

    private void updateLocation(int id, final String kod_lokasi) {
        final ProgressDialog progressDialog = new ProgressDialog(AssetsDetailsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_UPDATE_LOCATION)
                .addBodyParameter("id", String.valueOf(id))
                .addBodyParameter("kod_lokasi", kod_lokasi)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            if (response.getString("status").equals("success")) {
                                Toast.makeText(getApplicationContext(), response.getString("status"), Toast.LENGTH_LONG).show();
                                desc3.setText(locations_selected.getBlock_name() + " - " + locations_selected.getLocation_name());
                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("status"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updatePic(int id, final String pic_email) {
        final ProgressDialog progressDialog = new ProgressDialog(AssetsDetailsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_UPDATE_PIC)
                .addBodyParameter("id", String.valueOf(id))
                .addBodyParameter("pic_email", pic_email)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            if (response.getString("status").equals("success")) {
                                Toast.makeText(getApplicationContext(), response.getString("status"), Toast.LENGTH_LONG).show();
                                desc4.setText(staff_selected.getName());
                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("status"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void resetBarcode() {
        final ProgressDialog progressDialog = new ProgressDialog(AssetsDetailsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_RESET_BARCODE)
                .addBodyParameter("id", String.valueOf(assets.getId()))
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            if (response.getString("status").equals("success")) {
                                Toast.makeText(getApplicationContext(), "Reset Success", Toast.LENGTH_LONG).show();
                                assets.setBarcode("NULL");
                                desc1.setText("NULL");
                            } else {
                                Toast.makeText(getApplicationContext(), "Reset Failed", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Reset Failed with error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void save_pemeriksaan_data() {
        final ProgressDialog progressDialog = new ProgressDialog(AssetsDetailsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_SAVE_PEMERIKSAAN_DATA)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .addBodyParameter(veri_data)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

//                        try {
//                            new MaterialDialog.Builder(AssetsDetailsActivity.this)
//                                    .title("Makluman")
//                                    .content(response.getString("message"))
//                                    .positiveText("OK")
//                                    .show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        get_maklumat_pemeriksaan();
                        Constant.assets.setVerification_id(99999);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        Log.d("test", "onError: " + anError.getErrorBody());
                        Log.d("test", "onError: " + anError.getErrorDetail());
                        Log.d("test", "onError: " + anError.getMessage());

                    }
                });
    }

    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab: {
                    show_pemeriksaan_aset();
                    break;
                }

                case R.id.btn_custom_dialog_simpan:
                    new MaterialDialog.Builder(AssetsDetailsActivity.this)
                            .title("Pengesahan")
                            .content("Ada pasti ingin menyimpan maklumat ini?")
                            .positiveText("Pasti")
                            .negativeText("Tidak")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    save_pemeriksaan_data();
                                }
                            })
                            .show();
                    dialog.dismiss();
                    break;

                case R.id.tv_lokasi_sebenar:
                    try {
                        provideSimpleDialogLocation(Constant.locations);
                    } catch (Exception e) {
                        e.printStackTrace();
                        list_locations();
                    }

                    break;

                case R.id.tv_status:
                    new MaterialDialog.Builder(AssetsDetailsActivity.this)
                            .title("Status")
                            .items(R.array.keadaan_harta_modal)
                            .itemsCallbackMultiChoice(
                                    set_current_status(),
                                    (dialog, which, text) -> {
                                        StringBuilder str = new StringBuilder();
                                        StringBuilder str2 = new StringBuilder();
                                        for (int i = 0; i < which.length; i++) {
                                            if (i > 0) {
                                                str.append(", ");
                                            }
                                            str.append(text[i]);
                                            str2.append(which[i]);
                                        }
                                        //showToast(str2.toString());
                                        tv_status.setText(str.toString());
                                        status_code = str2.toString();
                                        //Log.d(TAG, "onClick: " + str2.toString());
                                        return true; // allow selection
                                    })
                            .onPositive((dialog, which) -> {
                                //showToast(which.toString());

                                veri_data.status = status_code;
                                dialog.dismiss();
                            })
                            .onNegative((dialog, which) -> dialog.dismiss())
                            .alwaysCallMultiChoiceCallback()
                            .positiveText(R.string.choose)
                            .autoDismiss(false)
                            .negativeText(R.string.cancel)
                            .show();
                    break;

                case R.id.tv_catatan:
                    new MaterialDialog.Builder(AssetsDetailsActivity.this)
                            .title("Catatan")
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(null, null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    if (input.length() > 0) {
                                        //set_var_pemeriksaan("catatan", input + "");
                                        veri_data.catatan = input + "";
                                        tv_catatan.setText(input);
                                    }

                                }
                            }).show();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Status{
        StringBuilder data = null;
    }


    private class Verification {
        public String location = assets.getKod_lokasi();
        public String status = null;
        public String catatan;
        public String jenis;
        public String pemeriksa;
        public int asset_id = assets.getId();
    }
}
