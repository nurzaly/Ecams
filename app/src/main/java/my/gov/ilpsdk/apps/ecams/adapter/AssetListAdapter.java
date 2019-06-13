package my.gov.ilpsdk.apps.ecams.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import my.gov.ilpsdk.apps.ecams.R;
import my.gov.ilpsdk.apps.ecams.data.GlobalVariable;
import my.gov.ilpsdk.apps.ecams.model.Assets;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.ViewHolder> implements Filterable  {

    public static final int INBOX_MODE = 0;
    public static final int OUTBOX_MODE = 1;
    public static final int TRASH_MODE = 2;

    private List<Assets> original_items = new ArrayList<>();
    private List<Assets> filtered_items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    private Context ctx;
    private GlobalVariable global;
    private int mode;

    // for item click listener
    private OnItemClickListener mOnItemClickListener;
    private boolean clicked = false;

    public interface OnItemClickListener {
        void onItemClick(View view, Assets obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView content1;
        public TextView content2;
        public TextView content3;
        public TextView content4;
        public TextView tv_icon;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            content1 = (TextView) v.findViewById(R.id.content1);
            content2 = (TextView) v.findViewById(R.id.content2);
            content3 = (TextView) v.findViewById(R.id.content3);
            content4 = (TextView) v.findViewById(R.id.content4);
            tv_icon = (TextView) v.findViewById(R.id.tv_icon);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);

            content3.setVisibility(View.GONE);
        }

    }

    public Filter getFilter() {
        return mFilter;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AssetListAdapter(int mode, Activity activity, List<Assets> items) {
        this.mode = mode;
        global = (GlobalVariable) activity.getApplication();
        this.ctx = activity;
        original_items = items;
        filtered_items = items;
    }

    @Override
    public AssetListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_assets, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Assets c = filtered_items.get(position);
        holder.tv_icon.setText(c.getJenis_aset());
        holder.content1.setVisibility(View.GONE);
        holder.title.setText(c.getNo_siri_pendaftaran().toUpperCase());


        //holder.content1.setText(c.getBarcode());
        //holder.text2.setText(c.getLokasi_bahagian());
        holder.content2.setText(c.getLokasi().getTitle());
        holder.content4.setText(c.getJenis().toUpperCase());

        try {
            if(c.getVerification_id() > 0){
                holder.lyt_parent.setBackgroundColor(Color.LTGRAY);
            }
        } catch (Exception e) {
            holder.lyt_parent.setBackgroundColor(Color.WHITE);
        }

//        Picasso.with(ctx).load(c.getSender().getPhoto())
//                .resize(100, 100)
//                .transform(new CircleTransform())
//                .into(holder.image);

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);

        // view detail message conversation
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked && mOnItemClickListener != null) {
                    clicked = true;
                    mOnItemClickListener.onItemClick(view, c, position);
                }
            }
        });
        holder.content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, c, position);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, c, position);
            }
        });
        clicked = false;
    }


    public void remove(int position) {
        filtered_items = trashGlobalData(position);
        notifyDataSetChanged();
    }

    public Assets getItem(int position){
        return filtered_items.get(position);
    }

    public void setItems(List<Assets> items){
        filtered_items = items;
        notifyDataSetChanged();
    }

    public void add(int position, Assets assets){
        filtered_items.add(position, assets);
        notifyDataSetChanged();
        updateGlobalData(filtered_items);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(filtered_items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(filtered_items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        updateGlobalData(filtered_items);
        return true;
    }

    /**
     * Here is the key method to apply the animation
     */
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filtered_items.size();
    }

    @Override
    public long getItemId(int position) {
        return filtered_items.get(position).getId();
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Assets> list = original_items;
            final List<Assets> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getNo_siri_pendaftaran() + " " +list.get(i).getLokasi().getTitle() + " " + list.get(i).getJenis();
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_items = (List<Assets>) results.values;
            notifyDataSetChanged();
        }

    }

    private void updateGlobalData(List<Assets> items){
        switch (mode){
            case INBOX_MODE :
                global.setAssets(items);
                break;
//            case OUTBOX_MODE :
//                global.setOutbox(items);
//                break;
//            case TRASH_MODE :
//                break;
        }
    }

    private List<Assets> trashGlobalData(int position){
        switch (mode){
            case INBOX_MODE :
                global.updatedAssets(position);
                return global.getAsset();
            case OUTBOX_MODE :
//                global.trashOutbox(position);
//                return global.getOutbox();
            case TRASH_MODE :
                break;
        }

        return new ArrayList<>();
    }
}