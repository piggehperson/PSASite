package com.piggeh.palmettoscholars.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.activities.MainActivity;
import com.piggeh.palmettoscholars.adapters.FirebaseResourceHolder;
import com.piggeh.palmettoscholars.classes.ResourceData;

public class ResourcesFragment extends Fragment
        /*implements ResourcesRecyclerAdapter.RecyclerItemClickListener*/ {
    private static final String TAG = "ContactFragment";

    private OnResourceClickListener mListener;

    private RecyclerView recyclerView;
    private ProgressBar progressBarLoadingResources;
    //private ResourcesRecyclerAdapter recyclerAdapter;
    private FirebaseRecyclerAdapter/*TeachersRecyclerAdapter*/ recyclerAdapter;
    private FirebaseDatabase database;
    private DatabaseReference resourcesDatabaseReference;
    private LinearLayoutManager layoutManager;
    private Parcelable mListState;

    public ResourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        /*recyclerAdapter = new ResourcesRecyclerAdapter(getActivity(), getResourcesFromIndex(),
                this);*/

        database = FirebaseDatabase.getInstance();
        resourcesDatabaseReference = database.getReference().child("resources");

        if (savedInstanceState == null){
            recyclerAdapter = new FirebaseRecyclerAdapter<ResourceData, FirebaseResourceHolder>(ResourceData.class, R.layout.recycler_item_resources_composite, FirebaseResourceHolder.class, resourcesDatabaseReference) {
                @Override
                public void populateViewHolder(FirebaseResourceHolder resourceHolder, final ResourceData resourceData, int position) {
                    if (progressBarLoadingResources.getVisibility() != View.GONE){
                        progressBarLoadingResources.setVisibility(View.GONE);
                    }

                    //name
                    if (resourceData.getUrl() != null){
                        //is resource
                        resourceHolder.getDivider().setVisibility(View.GONE);
                        resourceHolder.getSubheaderView().setVisibility(View.GONE);
                        resourceHolder.getItemView().setVisibility(View.VISIBLE);

                        resourceHolder.setTitle(resourceData.getTitle());
                        resourceHolder.setUrl(resourceData.getUrl());

                        resourceHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.openWebUrl(getActivity(), resourceData.getUrl());
                            }
                        });
                    } else{
                        //is subheader
                        if (position > 0){
                            resourceHolder.getDivider().setVisibility(View.VISIBLE);
                        } else{
                            resourceHolder.getDivider().setVisibility(View.GONE);
                        }
                        resourceHolder.getSubheaderView().setVisibility(View.VISIBLE);
                        resourceHolder.getItemView().setVisibility(View.GONE);

                        resourceHolder.setSubheader(resourceData.getTitle());
                    }
                }
            };
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teachers, container, false);

        progressBarLoadingResources = (ProgressBar) root.findViewById(R.id.progressBar_loadingTeachers);
        //progressBarLoadingResources.setVisibility(View.GONE);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_teachers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new RecyclerItemDivider(getContext()));
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerAdapter.cleanup();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        mListState = layoutManager.onSaveInstanceState();
        outState.putParcelable("list_state", mListState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        if (mListState != null){
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("list_state"));
        }
    }

    /*@Override
    public void onRecyclerItemClick(View view, int position, String url){
        mListener.onResourceClick(view, position, url);
    }*/

    /*@Override
    public void onStart(){
        super.onStart();

    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResourceClickListener) {
            mListener = (OnResourceClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResourceClickListener");
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
    public interface OnResourceClickListener {
        // TODO: Update argument type and name
        void onResourceClick(View view, int resourceId, String url);
    }
}