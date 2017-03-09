package com.piggeh.palmettoscholars.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.activities.MainActivity;
import com.piggeh.palmettoscholars.classes.ConfigUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by peter on 9/12/2016.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    private static final String TAG = "HomeRecyclerAdapter";

    private Context mContext;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        private final CardView cardOurMission;
        private final CardView cardOpenHours;
        private final CardView cardApplyNow;
        private final CardView cardNewsletters;
        private final CardView cardMerch;

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
            cardOurMission = (CardView) v.findViewById(R.id.card_ourMission);
            cardOpenHours = (CardView) v.findViewById(R.id.card_openhours);
            cardApplyNow = (CardView) v.findViewById(R.id.card_applyNow);
            cardNewsletters = (CardView) v.findViewById(R.id.card_newsletters);
            cardMerch = (CardView) v.findViewById(R.id.card_merch);
        }

        public CardView getCardOurMission() {
            return cardOurMission;
        }
        public CardView getCardOpenHours() {
            return cardOpenHours;
        }
        public CardView getCardApplyNow() {
            return cardApplyNow;
        }
        public CardView getCardNewsletters() {
            return cardNewsletters;
        }
        public CardView getCardMerch() {
            return cardMerch;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    public HomeRecyclerAdapter(Context context) {
        mContext = context;
    }

    /*public Bundle getTeacherData(int position){ return mDataSet.get(position); }*/
    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_home, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");
        switch (position){
            case 0:
                viewHolder.getCardOurMission().setVisibility(View.VISIBLE);
                viewHolder.getCardOpenHours().setVisibility(View.GONE);
                viewHolder.getCardNewsletters().setVisibility(View.GONE);
                viewHolder.getCardMerch().setVisibility(View.GONE);
                viewHolder.getCardApplyNow().setVisibility(View.GONE);
                /*ImageView banner = (ImageView) viewHolder.getCardOurMission().findViewById(R.id.imageView_ourMission);
                if (!ConfigUtils.shouldSaveData(mContext)){
                    Picasso.with(mContext)
                            .load("http://www.palmettoscholarsacademy.org/wp-content/uploads/2016/07/school-exterior.jpg")
                            .fit()
                            .centerCrop()
                            .into(banner);
                }*/
                break;
            case 1:
                viewHolder.getCardOurMission().setVisibility(View.GONE);
                viewHolder.getCardOpenHours().setVisibility(View.VISIBLE);
                viewHolder.getCardNewsletters().setVisibility(View.GONE);
                viewHolder.getCardMerch().setVisibility(View.GONE);
                viewHolder.getCardApplyNow().setVisibility(View.GONE);

                //check current time against open hours
                if (isPSAOpen()){
                    ((TextView)viewHolder.getCardOpenHours().findViewById(R.id.textView_hoursHeader)).setText(R.string.home_psa_is_open);
                }
                /*ImageView frontDesk = (ImageView) viewHolder.getCardOpenHours().findViewById(R.id.imageView_openHours);
                //TODO: Get a better picture of the front desk
                //TODO: Get a picture of nobody at the front desk
                if (!ConfigUtils.shouldSaveData(mContext)){
                    Picasso.with(mContext)
                            .load("http://peterglaab.com/wp-content/uploads/2016/09/BusinessHours2-small.jpg")
                            .fit()
                            .centerCrop()
                            .into(frontDesk);
                }*/
                break;
            case 2:
                viewHolder.getCardOurMission().setVisibility(View.GONE);
                viewHolder.getCardOpenHours().setVisibility(View.GONE);
                viewHolder.getCardNewsletters().setVisibility(View.VISIBLE);
                viewHolder.getCardMerch().setVisibility(View.GONE);
                viewHolder.getCardApplyNow().setVisibility(View.GONE);
                break;
            case 3:
                viewHolder.getCardOurMission().setVisibility(View.GONE);
                viewHolder.getCardOpenHours().setVisibility(View.GONE);
                viewHolder.getCardNewsletters().setVisibility(View.GONE);
                viewHolder.getCardMerch().setVisibility(View.VISIBLE);
                viewHolder.getCardApplyNow().setVisibility(View.GONE);
                break;
            case 4:
                viewHolder.getCardOurMission().setVisibility(View.GONE);
                viewHolder.getCardOpenHours().setVisibility(View.GONE);
                viewHolder.getCardNewsletters().setVisibility(View.GONE);
                viewHolder.getCardMerch().setVisibility(View.GONE);
                viewHolder.getCardApplyNow().setVisibility(View.VISIBLE);
                break;
        }
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 5/*mDataSet.size()*/;
    }

    private boolean isPSAOpen(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY
                || day == Calendar.SATURDAY){
            return false;
        }

        try {
            String string1 = "08";
            Date time1 = new SimpleDateFormat("HH").parse(string1);
            Calendar openCalendar = Calendar.getInstance();
            openCalendar.setTime(time1);

            String string2 = "16";
            Date time2 = new SimpleDateFormat("HH").parse(string2);
            Calendar closeCalendar = Calendar.getInstance();
            closeCalendar.setTime(time2);
            closeCalendar.add(Calendar.DATE, 1);

            String someRandomTime = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            Date d = new SimpleDateFormat("HH").parse(someRandomTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            return (x.after(openCalendar.getTime()) && x.before(closeCalendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}