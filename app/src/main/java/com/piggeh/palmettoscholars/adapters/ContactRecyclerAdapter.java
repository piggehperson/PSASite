package com.piggeh.palmettoscholars.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.utils.DPUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by peter on 9/12/2016.
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {
    private static final String TAG = "HomeRecyclerAdapter";

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        private final ImageView icon;
        private final TextView header;
        private final TextView text;
        private final Button action;
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
            icon = (ImageView) v.findViewById(R.id.imageView_contact_recycler_icon);
            header = (TextView) v.findViewById(R.id.textView_contact_recycler_header);
            text = (TextView) v.findViewById(R.id.textView_contact_recycler);
            action = (Button) v.findViewById(R.id.button_contact_recycler);
            divider = v.findViewById(R.id.divider_contact);
        }

        public ImageView getIcon(){return icon;}
        public TextView getHeader() {
            return header;
        }
        public TextView getText() {
            return text;
        }
        public Button getAction() {
            return action;
        }
        public View getDivider() {
            return divider;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    public ContactRecyclerAdapter() {

    }

    /*public Bundle getTeacherData(int position){ return mDataSet.get(position); }*/
    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_contact, viewGroup, false);

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
                viewHolder.getHeader().setText(R.string.contact_phone);
                viewHolder.getIcon().setImageResource(R.drawable.ic_call);
                viewHolder.getText().setText(R.string.contact_phone_number);
                viewHolder.getAction().setText(R.string.contact_phone_call);
                viewHolder.getAction().setId(R.id.button_contact_call);
                break;
            case 1:
                viewHolder.getHeader().setText(R.string.contact_email);
                viewHolder.getIcon().setImageResource(R.drawable.ic_email);
                viewHolder.getText().setText(R.string.contact_email_address);
                viewHolder.getAction().setText(R.string.contact_email_send);
                viewHolder.getAction().setId(R.id.button_contact_email);
                break;
            case 2:
                viewHolder.getHeader().setText(R.string.contact_location);
                viewHolder.getIcon().setImageResource(R.drawable.ic_location);
                viewHolder.getText().setText(R.string.contact_location_address_full);
                viewHolder.getText().setPaddingRelative(
                        0,
                        0,
                        0,
                        DPUtils.convertDpToPx(16)
                );
                viewHolder.getAction().setVisibility(View.GONE);;
                break;
            case 3:
                viewHolder.getHeader().setText(R.string.contact_fax);
                viewHolder.getIcon().setImageResource(R.drawable.ic_printer);
                viewHolder.getText().setText(R.string.contact_fax_number);
                viewHolder.getText().setPaddingRelative(
                        0,
                        0,
                        0,
                        DPUtils.convertDpToPx(16)
                );
                viewHolder.getAction().setVisibility(View.GONE);
                break;
            case 4:
                viewHolder.getHeader().setText(R.string.contact_twitter);
                viewHolder.getIcon().setImageResource(R.drawable.ic_twitter);
                viewHolder.getText().setText(R.string.contact_twitter_username);
                viewHolder.getAction().setText(R.string.contact_social_open);
                viewHolder.getAction().setId(R.id.button_contact_twitter);
                viewHolder.getDivider().setVisibility(View.GONE);
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