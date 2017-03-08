package com.piggeh.palmettoscholars.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.piggeh.palmettoscholars.adapters.ContactRecyclerAdapter;
import com.piggeh.palmettoscholars.adapters.HomeRecyclerAdapter;

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

    private RecyclerView recyclerView;
    private ContactRecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new ContactRecyclerAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_home, container, false);*/
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //progressBarLoadingResources.setVisibility(View.GONE);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_home);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new RecyclerItemDivider(getContext()));
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("recycler_state", layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        if (savedInstanceState != null){
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("recycler_state"));
        }
    }
}