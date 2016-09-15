package com.piggeh.palmettoscholars.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.activities.TeacherDetailActivity;
import com.piggeh.palmettoscholars.adapters.FirebaseTeacherHolder;
import com.piggeh.palmettoscholars.adapters.TeachersRecyclerAdapter;
import com.piggeh.palmettoscholars.classes.TeacherConstants;
import com.piggeh.palmettoscholars.classes.TeacherData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TeachersFragment extends Fragment
implements TeachersRecyclerAdapter.RecyclerItemClickListener {
    private static final String TAG = "TeachersFragment";

    private OnTeacherClickListener mListener;

    private RecyclerView recyclerView;
    private ProgressBar progressBarLoadingTeachers;
    private FirebaseRecyclerAdapter/*TeachersRecyclerAdapter*/ recyclerAdapter;
    private FirebaseDatabase database;
    private DatabaseReference teachersDatabaseReference;

    public TeachersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static TeachersFragment newInstance(String argments) {
        TeachersFragment fragment = new TeachersFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            *//*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*//*
        }*/
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (DatabaseException e){
            e.printStackTrace();
        }
        database = FirebaseDatabase.getInstance();

        teachersDatabaseReference = database.getReference().child("teachers");

        recyclerAdapter = new FirebaseRecyclerAdapter<TeacherData, FirebaseTeacherHolder>(TeacherData.class, R.layout.recycler_item_teacher, FirebaseTeacherHolder.class, teachersDatabaseReference) {
            @Override
            public void populateViewHolder(FirebaseTeacherHolder teacherHolder, final TeacherData teacherData, int position) {
                if (progressBarLoadingTeachers.getVisibility() != View.GONE){
                    progressBarLoadingTeachers.setVisibility(View.GONE);
                }

                //name
                if (teacherData.getPrefix() == TeacherConstants.PREFIX_MS){
                    teacherHolder.setName(String.format(
                            getContext().getString(R.string.teachers_prefix_ms),
                            teacherData.getName()
                    ));
                } else if (teacherData.getPrefix() == TeacherConstants.PREFIX_MRS){
                    teacherHolder.setName(String.format(
                            getContext().getString(R.string.teachers_prefix_mrs),
                            teacherData.getName()
                    ));
                } else if (teacherData.getPrefix() == TeacherConstants.PREFIX_MR){
                    teacherHolder.setName(String.format(
                            getContext().getString(R.string.teachers_prefix_mr),
                            teacherData.getName()
                    ));
                } else if (teacherData.getPrefix() == TeacherConstants.PREFIX_DR){
                    teacherHolder.setName(String.format(
                            getContext().getString(R.string.teachers_prefix_dr),
                            teacherData.getName()
                    ));
                } else{
                    teacherHolder.setName(teacherData.getName());
                }

                teacherHolder.setSubject(teacherData.getSubject());
                //teacherHolder.getHiddenDataView().setText(teacherData.getIdentifier());
                //hide divider if necessary
                if (position == recyclerAdapter.getItemCount() - 1){
                    teacherHolder.getDivider().setVisibility(View.INVISIBLE);
                } else{
                    teacherHolder.getDivider().setVisibility(View.VISIBLE);
                }
                teacherHolder.getRootview().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        //teacherData.get
                        Query queryRef = teachersDatabaseReference.orderByChild("name").equalTo(teacherData.getName());
                        queryRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                //System.out.println(snapshot.getKey());
                                //snapshot.getRef().toString()
                                Intent teacherDetail = new Intent(getContext(), TeacherDetailActivity.class);
                                //teacherDetail.putExtra(TeacherConstants.KEY_INDEX, teacherId);
                                teacherDetail.putExtra(TeacherConstants.KEY_INDEX, snapshot.getRef().toString());
                                teacherDetail.putExtra("launched_from_shortcut", false);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                            Pair.create(view.findViewById(R.id.teacherAvatar), "avatar"),
                                            Pair.create(getActivity().findViewById(android.R.id.navigationBarBackground), Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME),
                                            Pair.create(getActivity().findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                                    );
                                    startActivity(teacherDetail, options.toBundle());
                                } else{
                                    startActivity(teacherDetail);
                                }
                            }
                            @Override
                            public void onChildRemoved(DataSnapshot snapshot){}
                            @Override
                            public void onCancelled(DatabaseError databaseError){
                                Log.w(TAG, "Cancelled: " + databaseError.toString());
                            }
                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String string){}
                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String string){}
                        });
                    }
                });
            }
        };

        /*recyclerAdapter = new TeachersRecyclerAdapter(getTeachersFromIndex(),
                null,
                getContext(),
                this);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teachers, container, false);

        progressBarLoadingTeachers = (ProgressBar) root.findViewById(R.id.progressBar_loadingTeachers);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_teachers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        //recyclerView.addItemDecoration(new RecyclerItemDivider(getContext()));
        // Inflate the layout for this fragment
        return root;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerAdapter.cleanup();
        /*mAdapter.cleanup();*/
    }

    public static final String SORT_MODE_NAME = "name";
    public static final String SORT_MODE_CATEGORY = "category";

    private ArrayList<Bundle> queryTeachers(String sortBy){
        ArrayList<Bundle> bundles = new ArrayList<>();
        String[] names;
        int[] categories;
        int[] prefixes;
        String[] emails;
        String[] subjects;
        String[] bios;

        if (sortBy.equals(SORT_MODE_NAME)){
            //sort by name
            Query teachersNameQuery = teachersDatabaseReference.orderByChild("name");

            /*teachersNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot teacherSnapshot: dataSnapshot.getChildren()) {
                        // TODO: handle the teacher
                        Bundle bundle = new Bundle();
                        bundle.putString("name", teacherSnapshot.get);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });*/
        }
        return bundles;
    }

    private ArrayList<Bundle> getTeachersFromIndex(){
        String[] names = getResources().getStringArray(R.array.teacher_index_names);
        int[] categories = getResources().getIntArray(R.array.teacher_index_categories);
        int[] prefixes = getResources().getIntArray(R.array.teacher_index_prefixes);
        String[] emails = getResources().getStringArray(R.array.teacher_index_emails);
        TypedArray avatars = getResources().obtainTypedArray(R.array.teacher_index_avatars);

        ArrayList<Bundle> bundles = new ArrayList<>();
        for(int loop = 0; loop + 1 < Math.min(names.length, Math.min(categories.length, prefixes.length)); loop = loop + 1){
            Bundle bundle = new Bundle();
            bundle.putString(TeacherConstants.KEY_NAME, names[loop]);
            bundle.putInt(TeacherConstants.KEY_CATEGORY, categories[loop]);
            bundle.putInt(TeacherConstants.KEY_PREFIX, prefixes[loop]);
            bundle.putString(TeacherConstants.KEY_EMAIL, emails[loop]);
            bundle.putInt(TeacherConstants.KEY_AVATAR, avatars.getResourceId(loop, 0));
            bundles.add(bundle);
        }
        //recycle lists
        avatars.recycle();
        //finish
        return bundles;
    }

    public static ArrayList<Bundle> getTeachersFromIndex(Context context){
        String[] names = context.getResources().getStringArray(R.array.teacher_index_names);
        int[] categories = context.getResources().getIntArray(R.array.teacher_index_categories);
        int[] prefixes = context.getResources().getIntArray(R.array.teacher_index_prefixes);
        String[] emails = context.getResources().getStringArray(R.array.teacher_index_emails);
        TypedArray avatars = context.getResources().obtainTypedArray(R.array.teacher_index_avatars);

        ArrayList<Bundle> bundles = new ArrayList<>();
        for(int loop = 0; loop + 1 < Math.min(names.length, Math.min(categories.length, prefixes.length)); loop = loop + 1){
            Bundle bundle = new Bundle();
            bundle.putString(TeacherConstants.KEY_NAME, names[loop]);
            bundle.putInt(TeacherConstants.KEY_CATEGORY, categories[loop]);
            bundle.putInt(TeacherConstants.KEY_PREFIX, prefixes[loop]);
            bundle.putString(TeacherConstants.KEY_EMAIL, emails[loop]);
            bundle.putInt(TeacherConstants.KEY_AVATAR, avatars.getResourceId(loop, 0));
            bundles.add(bundle);
        }
        //recycle lists
        avatars.recycle();
        //finish
        return bundles;
    }

    @Override
    public void onRecyclerItemClick(View view, int position, String teacherName){
        mListener.onTeacherClick(view, position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTeacherClickListener) {
            mListener = (OnTeacherClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTeacherClickListener");
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
    public interface OnTeacherClickListener {
        // TODO: Update argument type and name
        void onTeacherClick(View view, int teacherId);
    }
}