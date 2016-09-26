package com.piggeh.palmettoscholars.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piggeh.palmettoscholars.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";

    private OnFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }

    /*private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ProgressBar progressBarLoadingContactInfo;
    private TextView phoneView;
    private TextView emailView;
    private TextView streetView;
    private TextView cityView;
    private TextView faxView;
    private TextView twitterView;

    private String phoneDisplay;
    private long phoneNumber;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setRetainInstance(true);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("contact");*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return /*View mView = */inflater.inflate(R.layout.fragment_contact, container, false);
        /*progressBarLoadingContactInfo = (ProgressBar) mView.findViewById(R.id.progressbar_loadingContactInfo);
        phoneView = (TextView) mView.findViewById(R.id.textView_phoneNumber);
        emailView = (TextView) mView.findViewById(R.id.textView_emailAddress);
        streetView = (TextView) mView.findViewById(R.id.textView_locationStreet);
        cityView = (TextView) mView.findViewById(R.id.textView_locationCity);
        faxView = (TextView) mView.findViewById(R.id.textView_faxNumber);
        twitterView = (TextView) mView.findViewById(R.id.textView_twitterUsername);
        return mView;*/
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()){
                    case "phone":
                        phoneView.setText((String)dataSnapshot.getValue());
                        break;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
