package com.piggeh.palmettoscholars.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.adapters.TeachersRecyclerAdapter;
import com.piggeh.palmettoscholars.classes.RecyclerItemDivider;
import com.piggeh.palmettoscholars.classes.TeacherConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by peter on 9/8/2016.
 */
public class TeachersFragment extends Fragment
implements TeachersRecyclerAdapter.RecyclerItemClickListener {
    private static final String TAG = "ContactFragment";

    private OnTeacherClickListener mListener;

    private RecyclerView recyclerView;
    private ProgressBar progressBarLoadingTeachers;
    private TeachersRecyclerAdapter recyclerAdapter;

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
        recyclerAdapter = new TeachersRecyclerAdapter(getTeachersFromIndex(),
                null,
                getContext(),
                this);
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

    /*@Override
    public void onStart(){
        super.onStart();

    }*/

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