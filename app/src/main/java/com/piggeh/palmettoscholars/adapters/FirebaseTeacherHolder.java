package com.piggeh.palmettoscholars.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piggeh.palmettoscholars.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseTeacherHolder extends RecyclerView.ViewHolder {
    View mView;
    //Context mContext;

    public FirebaseTeacherHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.teacherName);
        field.setText(name);
    }

    public void setSubject(String text) {
        TextView field = (TextView) mView.findViewById(R.id.teacherCategory);
        field.setText(text);
    }

    public View getDivider(){
        return mView.findViewById(R.id.divider);
    }

    public RelativeLayout getRootview(){
        return (RelativeLayout) mView.findViewById(R.id.relativeLayout_recycler_teacher);
    }

    public TextView getHiddenDataView(){
        return (TextView) mView.findViewById(R.id.textView_hiddenData);
    }

    public CircleImageView getAvatarView(){
        return (CircleImageView) mView.findViewById(R.id.teacherAvatar);
    }
}