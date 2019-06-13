package my.gov.ilpsdk.apps.ecams;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import my.gov.ilpsdk.apps.ecams.adapter.LocationsListAdapter;
import my.gov.ilpsdk.apps.ecams.data.Constant;
import my.gov.ilpsdk.apps.ecams.data.Tools;
import my.gov.ilpsdk.apps.ecams.model.Locations;
import my.gov.ilpsdk.apps.ecams.model.Staff;
import my.gov.ilpsdk.apps.ecams.widget.DividerItemDecoration;
import my.gov.ilpsdk.apps.ecams.R;

import static android.content.ContentValues.TAG;

public class LocationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    public LocationsListAdapter mAdapter;
    private Dialog dialog_pegawai_aset;
    TextView tv_maklumat_lokasi, tv_pegawai_pemeriksa_1, tv_pegawai_pemeriksa_2;
    Post_data post_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Senarai Lokasi");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_locations();
            }
        });

        get_locations();


    }

    public void show_dialog_kemaskini_pegawai(Locations locations){
        dialog_pegawai_aset = new Dialog(this);
        dialog_pegawai_aset.setContentView(R.layout.cus_dialog_pegawai_aset);
        dialog_pegawai_aset.setTitle("Kemaskini Maklumat Pegawai Pemeriksa");

        post_data = new Post_data();
        post_data.block_code = locations.getBlock_code();
        post_data.short_name = locations.getShort_name();
                //ButterKnife.bind(dialog);

//        // set the custom dialog components - text, image and button
//        TextView text = (TextView) dialog.findViewById(R.id.text);
//        text.setText("Android custom dialog example!");
//        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.ic_launcher);
//
        Button btn_save = (Button) dialog_pegawai_aset.findViewById(R.id.btn_custom_dialog_pemeriksa_aset);
        Button btn_cancel = (Button) dialog_pegawai_aset.findViewById(R.id.btn_custom_dialog_pemeriksa_aset_cancel);

        tv_maklumat_lokasi = (TextView) dialog_pegawai_aset.findViewById(R.id.tv_maklumat_lokasi);
        tv_pegawai_pemeriksa_1 = (TextView) dialog_pegawai_aset.findViewById(R.id.tv_pegawai_pemeriksa_1);
        tv_pegawai_pemeriksa_2 = (TextView) dialog_pegawai_aset.findViewById(R.id.tv_pegawai_pemeriksa_2);




        btn_save.setOnClickListener(clickListener);
        btn_cancel.setOnClickListener(clickListener);

        tv_maklumat_lokasi.setText(locations.getBlock_name());
        tv_pegawai_pemeriksa_1.setOnClickListener(clickListener);
        tv_pegawai_pemeriksa_2.setOnClickListener(clickListener);

        dialog_pegawai_aset.show();
    }
    private void show_progress_bar() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }


    private void get_locations() {
        show_progress_bar();
        AndroidNetworking.get(Constant.URL_GET_LOCATION+"?type=block")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Locations.class, new ParsedRequestListener<List<Locations>>() {
                    @Override
                    public void onResponse(List<Locations> response) {
                        displayLocationsList(response);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onError: " + anError.getMessage());
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }

    private void displayLocationsList(List<Locations> locations) {
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        //studentlist = Constant.getFriendsData(this);
        mAdapter = new LocationsListAdapter(this,locations);
        recyclerView.setAdapter(mAdapter);


        Toast.makeText(this,  mAdapter.getItemCount() + " item found", Toast.LENGTH_SHORT).show();

//        if (mAdapter.getItemCount() == 0) {
//            lyt_not_found.setVisibility(View.VISIBLE);
//        } else {
//            lyt_not_found.setVisibility(View.GONE);
//        }
        mAdapter.setOnItemClickListener(new LocationsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Locations locations, int position) {
                // ActivityUserDetails.navigate((ActivityMain) this, v.findViewById(R.id.image), obj);
                switch (v.getId()) {
                    case R.id.lyt_parent:
                        AssetListActivity.navigate(LocationsActivity.this, Constant.URL_ASSETS_BY_LOCATION+"?location="+locations.getShort_name(), "Senarai Aset");
                        break;
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        // for system bar in lollipop
        try {
            Tools.systemBarLolipop(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final Locations locations = mAdapter.getItem(position);
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "onSwiped: " + locations.getLevel());
            show_dialog_kemaskini_pegawai(locations);
//            if(direction == 16){
//
//                MaterialDialog dialog = new MaterialDialog.Builder(LocationsActivity.this)
//                        .title("Jana Laporan")
//                        .items(R.array.jana_laporan)
//                        .itemsCallback(new MaterialDialog.ListCallback() {
//                            @Override
//                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                if(which == 0){
//                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.DOMAIN_AMOS + "kewpa10?bangunan=" + locations.getBlock_code()));
//                                    startActivity(browserIntent);
//                                }
//                                else if(which == 1){
//                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.DOMAIN_AMOS + "kewpa11?bangunan=" + locations.getBlock_code()));
//                                    startActivity(browserIntent);
//                                }
//                                else{
//                                    dialog.dismiss();
//                                }
//                            }
//                        })
//                        .show();
//            }
//            else{
//                show_dialog_kemaskini_pegawai(locations);
//            }
            //mAdapter.remove(position);
            /*Snackbar.make(findViewById(android.R.id.content), "Moved to Trash", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mAdapter.add(position, assets);
                }
            }).show();*/
        }
    });

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_pegawai_pemeriksa_1:

                    try {
                        //list_staf(tv_pegawai_pemeriksa_1,1);
                        provideSimpleDialogStaff(Constant.staff,tv_pegawai_pemeriksa_1, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        list_staf(tv_pegawai_pemeriksa_1,1);
                    }
                    break;
                case R.id.tv_pegawai_pemeriksa_2:
                    try {
                        provideSimpleDialogStaff(Constant.staff,tv_pegawai_pemeriksa_2, 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                        list_staf(tv_pegawai_pemeriksa_2, 2);
                    }
                    break;
                case R.id.btn_custom_dialog_pemeriksa_aset:
                    if(post_data.nama_pemeriksa_1 == null || post_data.nama_pemeriksa_2 == null){
                        new MaterialDialog.Builder(LocationsActivity.this)
                                .content("Sila pilih nama pegawai")
                                .positiveText("OK")
                                .show();
                    }
                    else{
                        save_pegawai_pemeriksa();
                    }

                    break;
                case R.id.btn_custom_dialog_pemeriksa_aset_cancel:
                    dialog_pegawai_aset.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public void save_pegawai_pemeriksa(){

        final ProgressDialog progressDialog = new ProgressDialog(LocationsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Simpan Pegawai Pemeriksa...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_SAVE_PEGAWAI_PEMERIKSA)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .addBodyParameter(post_data)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        dialog_pegawai_aset.dismiss();
                        new MaterialDialog.Builder(LocationsActivity.this)
                                .content("Berjaya disimpan")
                                .positiveText("OK")
                                .show();

                        Log.d("test", "onResponse: " + response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onError: " + anError.getMessage() + anError.getErrorDetail() + anError.getErrorBody());
                    }
                });
    }

    private void list_staf(final TextView tv_obj, final int n) {

        final ProgressDialog progressDialog = new ProgressDialog(LocationsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.get(Constant.URL_STAF_LIST)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Staff.class, new ParsedRequestListener<ArrayList<Staff>>() {
                    @Override
                    public void onResponse(ArrayList<Staff> response) {
                        progressDialog.dismiss();
                        Constant.staff = response;
                        provideSimpleDialogStaff(response,tv_obj, n);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onError: " + anError.getMessage() + anError.getErrorDetail() + anError.getErrorBody());
                    }
                });
    }

    void provideSimpleDialogStaff(final ArrayList<Staff> staff, final TextView tv_obj, final int n){

        SimpleSearchDialogCompat dialog = new SimpleSearchDialogCompat(LocationsActivity.this, "STAF ILP SANDAKAN",
                "Taip nama staf yang dicari...", null, staff,
                new SearchResultListener<Staff>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat dialog, Staff item, int position) {
                        tv_obj.setText(item.getName());
                        if(n == 1){
                            post_data.nama_pemeriksa_1 = item.getEmail();
                        }
                        else {
                            post_data.nama_pemeriksa_2 = item.getEmail();
                        }
                        dialog.dismiss();
                    }

                });
        dialog.show();
        dialog.getSearchBox().setTypeface(Typeface.SERIF);
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

    private class Post_data{
        String nama_pemeriksa_1 = null;
        String nama_pemeriksa_2 = null;
        String block_code = null;
        String short_name = null;
    }




}
