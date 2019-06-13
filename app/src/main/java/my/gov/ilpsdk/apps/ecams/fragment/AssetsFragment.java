package my.gov.ilpsdk.apps.ecams.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import my.gov.ilpsdk.apps.ecams.AssetsDetailsActivity;
import my.gov.ilpsdk.apps.ecams.data.Constant;
import my.gov.ilpsdk.apps.ecams.data.GlobalVariable;
import my.gov.ilpsdk.apps.ecams.data.Tools;
import my.gov.ilpsdk.apps.ecams.R;
import my.gov.ilpsdk.apps.ecams.adapter.AssetListAdapter;
import my.gov.ilpsdk.apps.ecams.model.Assets;
import my.gov.ilpsdk.apps.ecams.widget.DividerItemDecoration;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class AssetsFragment extends Fragment {

    public RecyclerView recyclerView;
    public AssetListAdapter mAdapter;
    private GlobalVariable global;
    private View view;
    private SearchView searchView;
    private LinearLayout lyt_not_found;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Integer update_asset_id = 0;
    private String bahagian = null;
    private SharedPreferences myconfig;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assets, null);
        global = (GlobalVariable) getActivity().getApplication();

        setHasOptionsMenu(true);


        Constant.FRAGMENT_ID = 0;

        Bundle bundle = this.getArguments();

        if(bundle != null){
            url = bundle.getString(Constant.KEY_URL,"fragment-url");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        lyt_not_found = (LinearLayout) view.findViewById(R.id.lyt_not_found);

        //((MainActivity)getActivity()).setTitle("Unset Assets");
        myconfig = getActivity().getSharedPreferences(Constant.MYCONFIG, MODE_PRIVATE);
        bahagian = myconfig.getString(Constant.KEY_BAHAGIAN,"bahagian");
        //bahagian = "tkr";
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAssets();
            }
        });

        getAssets();
        return view;
    }

    @Override
    public void onResume() {
        try {
            if(global.getAsset().size() > 0){
                mAdapter.setItems(global.getAsset());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Constant.assets = null;

        check_query_search();

        //getAssets();
        super.onResume();

    }
    private void check_query_search(){
        try {
            if(searchView.getQuery().length() > 0){
                Log.d(TAG, "onResume: " + searchView.getQuery());
                mAdapter.getFilter().filter(searchView.getQuery());
            }
        }catch (Exception e){

        }
    }

    private void getAssets() {
        show_progress_bar();
        AndroidNetworking.get(url)
                //.addQueryParameter(Constant.BAHAGIAN,bahagian)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Assets.class, new ParsedRequestListener<List<Assets>>() {
                    @Override
                    public void onResponse(List<Assets> response) {
                        global.setAssets(response);
                        Toast.makeText(getContext(),response.size() + " item found",Toast.LENGTH_SHORT).show();
                        displayAssets();
                        close_progress_bar();
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getMessage());
                        close_progress_bar();
                    }
                });
    }

    private void displayAssets() {
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        //studentlist = Constant.getFriendsData(this);
        mAdapter = new AssetListAdapter(AssetListAdapter.INBOX_MODE, getActivity(), global.getAsset());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AssetListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Assets obj, int position) {
                Constant.assets = obj;
                switch (view.getId()) {
                    case R.id.lyt_parent:
                        AssetsDetailsActivity.navigate((AppCompatActivity) getActivity(), obj);
                        break;
//                    case R.id.content1:
//                        new RunBarcodeScanner().launchActivity(FullScannerFragmentActivity.class, getActivity(), getContext(), 1);
//                        break;
                }

            }
        });

        //itemTouchHelper.attachToRecyclerView(recyclerView);

        if (mAdapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
        /*mAdapter.setOnItemClickListener(new AdapterTeacherList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Student obj, int position) {
                // ActivityUserDetails.navigate((ActivityMain) this, v.findViewById(R.id.image), obj);
                switch (v.getId()) {
                    case R.id.image:
                        ActivityUserDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.image), obj);
                        break;
                }
            }
        });*/

        // for system bar in lollipop
        try {
            Tools.systemBarLolipop(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        check_query_search();
    }

    // handle swipe to delete, and dragable
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
            final Assets assets = mAdapter.getItem(position);
            mAdapter.remove(position);
            Snackbar.make(view, "Moved to Trash", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapter.add(position, assets);
                }
            }).show();
        }
    });

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_assets, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search assets...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    mAdapter.getFilter().filter(s);
                } catch (Exception e) {
                }
                return true;
            }
        });
        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTouchHelper.attachToRecyclerView(null);
                setItemsVisibility(menu, searchItem, false);
            }
        });

        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                itemTouchHelper.attachToRecyclerView(recyclerView);
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
        searchView.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_history) {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_content, new AssetsUpdatedBarcodeFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    private void show_progress_bar() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void close_progress_bar() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
