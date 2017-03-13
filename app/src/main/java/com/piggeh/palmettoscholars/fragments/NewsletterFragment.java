package com.piggeh.palmettoscholars.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.utils.DPUtils;

public class NewsletterFragment extends Fragment {
    private static final String TAG = "NewsletterFragment";

    private OnFragmentInteractionListener mListener;

    WebView webView;

    public NewsletterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_newsletter, container, false);

        webView = (WebView) mView.findViewById(R.id.newsletterView);
        /*final ProgressBar */progressBar = (ProgressBar) mView.findViewById(R.id.progressBar_loadingNewsletter);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("currentnewsletter")){
                    Log.d(TAG, "Loading page");
                    webView.loadUrl((String)dataSnapshot.getValue());
                    webView.setWebViewClient(new WebViewClient() {

                        public void onPageFinished(WebView view, String url) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                int cx = webView.getWidth() / 2;
                                int cy = DPUtils.convertDpToPx(44);
                                Animator anim =
                                        ViewAnimationUtils.createCircularReveal(webView, cx, cy, 0, Math.max(webView.getWidth(), webView.getHeight()));
                                anim.setDuration(getResources().getInteger(R.integer.duration_fullscreen));
                                anim.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                                anim.setInterpolator(new FastOutSlowInInterpolator());
                                webView.setVisibility(View.VISIBLE);
                                anim.start();
                            } else{
                                final ObjectAnimator fadeIn = new ObjectAnimator().ofFloat(webView, View.ALPHA, 0, 1);
                                fadeIn.setDuration(150);
                                ObjectAnimator fadeOut = new ObjectAnimator().ofFloat(progressBar, View.ALPHA, 1, 0);
                                fadeOut.setDuration(150);
                                fadeOut.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        webView.setVisibility(View.VISIBLE);
                                        fadeIn.start();
                                    }
                                });
                                fadeOut.start();
                            }
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        /*mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.defaultconfig);
        mFirebaseRemoteConfig.fetch(3600)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            mFirebaseRemoteConfig.activateFetched();

                            loadNewsletter();
                        } else {
                            //task failed
                            mView.findViewById(R.id.relativeLayout_error_loading).setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });*/

        return mView;
    }

    private void loadNewsletter(){
        webView.loadUrl(mFirebaseRemoteConfig.getString("current_newsletter"));
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                final ObjectAnimator fadeIn = new ObjectAnimator().ofFloat(webView, View.ALPHA, 0, 1);
                fadeIn.setDuration(150);
                ObjectAnimator fadeOut = new ObjectAnimator().ofFloat(progressBar, View.ALPHA, 1, 0);
                fadeOut.setDuration(150);
                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        webView.setVisibility(View.VISIBLE);
                        fadeIn.start();
                    }
                });
                fadeOut.start();
            }
        });
    }

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
