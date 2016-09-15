package com.piggeh.palmettoscholars.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piggeh.palmettoscholars.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseResourceHolder extends RecyclerView.ViewHolder {
    View mView;
    //Context mContext;

    public FirebaseResourceHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setTitle(String title) {
        TextView field = (TextView) mView.findViewById(R.id.resourceTitle);
        field.setText(title);
    }

    public void setUrl(String url) {
        TextView field = (TextView) mView.findViewById(R.id.resourceSubtitle);
        field.setText(url);
    }

    public void setSubheader(String subheader){
        TextView field = (TextView) mView.findViewById(R.id.subheaderText);
        field.setText(subheader);
    }

    public View getDivider(){
        return mView.findViewById(R.id.divider);
    }

    public FrameLayout getSubheaderView(){
        return (FrameLayout) mView.findViewById(R.id.frameLayout_resource_subheader);
    }

    public RelativeLayout getItemView(){
        return (RelativeLayout) mView.findViewById(R.id.relativeLayout_resource_item);
    }
}