package com.piggeh.palmettoscholars.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.activities.MainActivity;
import com.piggeh.palmettoscholars.classes.TeacherConstants;

import java.util.ArrayList;

/**
 * Created by peter on 9/12/2016.
 */
public class ResourcesRecyclerAdapter extends RecyclerView.Adapter<ResourcesRecyclerAdapter.ViewHolder> {
    private static final String TAG = "ResourceRecyclerAdapter";

    //constants
    public static final String KEY_TYPE = "type";
    public static final int TYPE_SUBHEADER = 0;
    public static final int TYPE_RESOURCE = 1;
    public static final String KEY_TITLE = "title";
    public static final String KEY_SUBTITLE = "subtitle";
    public static final String KEY_URL = "url";


    private ArrayList<Bundle> mDataSet;
    private Context mContext;
    private Activity mActivity;
    private static RecyclerItemClickListener mItemListener;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        private final LinearLayout itemRootView;
        private final FrameLayout subheaderView;
        private final TextView subheaderText;
        private final RelativeLayout resourceItemView;
        private final TextView titleTextView;
        private final TextView subtitleTextView;
        //private final TextView hiddenDataView;
        private final View divider;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getLayoutPosition() + " clicked.");
                    //returnElementClicked(getLayoutPosition());

                }
            });*/
            //v.setOnClickListener(this);
            itemRootView = (LinearLayout) v.findViewById(R.id.linearLayout_resources_root);
            subheaderView = (FrameLayout) v.findViewById(R.id.frameLayout_resource_subheader);
            subheaderText = (TextView) v.findViewById(R.id.subheaderText);
            resourceItemView = (RelativeLayout) v.findViewById(R.id.relativeLayout_resource_item);
            titleTextView = (TextView) v.findViewById(R.id.resourceTitle);
            subtitleTextView = (TextView) v.findViewById(R.id.resourceSubtitle);
            //hiddenDataView = (TextView) v.findViewById(R.id.textView_hiddenData);
            divider = v.findViewById(R.id.divider);
            //resourceItemView.setOnClickListener(this);
        }

        public LinearLayout getItemRootView(){ return itemRootView; }
        public FrameLayout getSubheaderView(){ return subheaderView;}
        public TextView getSubheaderText(){ return subheaderText; }
        public RelativeLayout getResourceItemView(){ return resourceItemView; }
        public TextView getTitleTextView() {
            return titleTextView;
        }
        public TextView getSubtitleTextView(){ return subtitleTextView; }
        //public TextView getHiddenData(){ return hiddenDataView; }
        public View getDivider(){ return divider; }

        /*@Override
        public void onClick(View v)
        {
            //mItemListener.onRecyclerItemClick(v, this.getAdapterPosition(), (String)getHiddenData().getText());
            Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
        }*/
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ResourcesRecyclerAdapter(/*Context context*/Activity activity, ArrayList<Bundle> dataSet, RecyclerItemClickListener itemListener) {
        mDataSet = dataSet;

        //mContext = context;
        mActivity = activity;
        mItemListener = itemListener;
    }

    /*public Bundle getTeacherData(int position){ return mDataSet.get(position); }*/
    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_resources_composite, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");
        if (mDataSet.get(position).getInt(KEY_TYPE, TYPE_SUBHEADER) == TYPE_SUBHEADER){
            viewHolder.getSubheaderView().setVisibility(View.VISIBLE);
            viewHolder.getResourceItemView().setVisibility(View.GONE);

            viewHolder.getSubheaderText().setText(mDataSet.get(position)
            .getString(KEY_TITLE));
        } else{
            viewHolder.getSubheaderView().setVisibility(View.GONE);
            viewHolder.getResourceItemView().setVisibility(View.VISIBLE);

            viewHolder.getTitleTextView().setText(mDataSet.get(position)
            .getString(KEY_TITLE));
            viewHolder.getSubtitleTextView().setText(mDataSet.get(position)
                    .getString(KEY_SUBTITLE));
            viewHolder.getResourceItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.openWebUrl(mActivity, mDataSet.get(position)
                    .getString(KEY_URL));
                }
            });
        }


        //hidden data
        //viewHolder.getHiddenData().setText(mDataSet.get(position).getString(TeacherConstants.KEY_NAME));

        //show divider if necessary
        if (position < mDataSet.size() - 1){
            if (mDataSet.get(position + 1).getInt(KEY_TYPE, TYPE_SUBHEADER) == TYPE_SUBHEADER){ //next item is a subheader
                viewHolder.getDivider().setVisibility(View.VISIBLE);
            }
        }
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /*private static void returnElementClicked(int position){
        elementPositionClicked = position;
    }*/

    /*public int getPositionClicked(){
        return elementPositionClicked;
    }*/

    public interface RecyclerItemClickListener {
        public void onRecyclerItemClick(View v, int position, String url);
    }
}