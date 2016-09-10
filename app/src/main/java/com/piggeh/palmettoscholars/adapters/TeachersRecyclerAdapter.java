package com.piggeh.palmettoscholars.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.classes.TeacherConstants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by peter on 9/8/2016.
 */
public class TeachersRecyclerAdapter extends RecyclerView.Adapter<TeachersRecyclerAdapter.ViewHolder> {
    private static final String TAG = "TeachersRecyclerAdapter";

    private ArrayList<Bundle> mSetTeacherData;
    private ArrayList<Drawable> mSetAvatars;
    private Context mContext;
    private static RecyclerItemClickListener mItemListener;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RelativeLayout rootView;
        private final TextView nameTextView;
        private final TextView categoryTextView;
        private final CircleImageView avatarView;
        private final TextView hiddenDataView;

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
            rootView = (RelativeLayout) v.findViewById(R.id.relativeLayout_recycler_teacher);
            nameTextView = (TextView) v.findViewById(R.id.teacherName);
            categoryTextView = (TextView) v.findViewById(R.id.teacherCategory);
            avatarView = (CircleImageView) v.findViewById(R.id.teacherAvatar);
            hiddenDataView = (TextView) v.findViewById(R.id.textView_hiddenData);
            rootView.setOnClickListener(this);
        }

        public RelativeLayout getRootView(){ return rootView; }
        public TextView getNameTextView() {
            return nameTextView;
        }
        public TextView getCategoryTextView(){ return categoryTextView; }
        public CircleImageView getAvatarView() {
            return avatarView;
        }
        public TextView getHiddenData(){ return hiddenDataView; }

        @Override
        public void onClick(View v)
        {
            mItemListener.onRecyclerItemClick(v, this.getAdapterPosition(), (String)getHiddenData().getText());
            Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param setTeacherData String[] containing the data to populate views to be used by RecyclerView.
     */
    public TeachersRecyclerAdapter(ArrayList<Bundle> setTeacherData, ArrayList<Drawable> setAvatars, Context context, RecyclerItemClickListener itemListener) {
        mSetTeacherData = setTeacherData;
        mSetAvatars = setAvatars;

        mContext = context;
        mItemListener = itemListener;
    }

    /*public Bundle getTeacherData(int position){ return mSetTeacherData.get(position); }*/
    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_teacher, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");

        //title
        switch (mSetTeacherData.get(position).getInt(TeacherConstants.KEY_PREFIX)){
            case TeacherConstants.PREFIX_MS:
                viewHolder.getNameTextView().setText(String.format(
                        mContext.getString(R.string.teachers_prefix_ms),
                        mSetTeacherData.get(position).getString(TeacherConstants.KEY_NAME)
                ));
                break;
            case TeacherConstants.PREFIX_MRS:
                viewHolder.getNameTextView().setText(String.format(
                        mContext.getString(R.string.teachers_prefix_mrs),
                        mSetTeacherData.get(position).getString(TeacherConstants.KEY_NAME)
                ));
                break;
            case TeacherConstants.PREFIX_MR:
                viewHolder.getNameTextView().setText(String.format(
                        mContext.getString(R.string.teachers_prefix_mr),
                        mSetTeacherData.get(position).getString(TeacherConstants.KEY_NAME)
                ));
                break;
            case TeacherConstants.PREFIX_DR:
                viewHolder.getNameTextView().setText(String.format(
                        mContext.getString(R.string.teachers_prefix_dr),
                        mSetTeacherData.get(position).getString(TeacherConstants.KEY_NAME)
                ));
                break;
        }
        //subtitle
        switch (mSetTeacherData.get(position).getInt(TeacherConstants.KEY_CATEGORY)){
            case TeacherConstants.CATEGORY_ENGLISH:
                viewHolder.getCategoryTextView().setText(mContext.getString(R.string.teachers_category_english));
                break;
            case TeacherConstants.CATEGORY_FINE_ARTS:
                viewHolder.getCategoryTextView().setText(mContext.getString(R.string.teachers_category_fine_arts));
                break;
            case TeacherConstants.CATEGORY_FOREIGN_LANGUAGES:
                viewHolder.getCategoryTextView().setText(mContext.getString(R.string.teachers_category_foreign_languages));
                break;
            case TeacherConstants.CATEGORY_MATH:
                viewHolder.getCategoryTextView().setText(mContext.getString(R.string.teachers_category_math));
                break;
            case TeacherConstants.CATEGORY_SCIENCE:
                viewHolder.getCategoryTextView().setText(mContext.getString(R.string.teachers_category_science));
                break;
            case TeacherConstants.CATEGORY_SOCIAL_SCIENCE:
                viewHolder.getCategoryTextView().setText(mContext.getString(R.string.teachers_category_social_science));
                break;
        }

        //hidden data
        viewHolder.getHiddenData().setText(mSetTeacherData.get(position).getString(TeacherConstants.KEY_NAME));

        //TODO: Get actual pictures of the teachers
        //viewHolder.getAvatarView().setImageDrawable(mSetAvatars.get(position));
        mSetTeacherData.get(position).getInt(TeacherConstants.KEY_AVATAR);
        viewHolder.getAvatarView().setImageResource(mSetTeacherData.get(position).getInt(TeacherConstants.KEY_AVATAR));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mSetTeacherData.size();
    }

    /*private static void returnElementClicked(int position){
        elementPositionClicked = position;
    }*/

    /*public int getPositionClicked(){
        return elementPositionClicked;
    }*/

    public interface RecyclerItemClickListener {
        public void onRecyclerItemClick(View v, int position, String teacherName);
    }
}