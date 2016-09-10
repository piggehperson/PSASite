package com.piggeh.palmettoscholars.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.classes.TeacherConstants;
import com.piggeh.palmettoscholars.fragments.TeachersFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherDetailActivity extends AppCompatActivity {

    private static final String TAG = "TeachDetailActivity";

    //views
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView appbarBackground;
    private CircleImageView avatarImage;
    private TextView emailView;
    private TextView bioView;
    private ProgressBar bioProgressBar;
    //vars
    private int teacherIndex;
    private String teacherName;
    private int teacherPrefix;
    private int teacherCategory;
    private String teacherEmail;
    private Bundle teacherDataBundle;
    private int teacherAvatarId;
    private String bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

        //set up SupportActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set up views
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        appbarBackground = (ImageView) findViewById(R.id.appbarBackground);
        avatarImage = (CircleImageView) findViewById(R.id.avatarImageHeader);
        emailView = (TextView) findViewById(R.id.textView_emailAddress);
        bioView = (TextView) findViewById(R.id.textView_bio);
        bioProgressBar = (ProgressBar) findViewById(R.id.progressBar_loadingBio);

        //get data from Intent
        teacherIndex = getIntent().getIntExtra(TeacherConstants.KEY_INDEX, 0);
        //get data from bundle
        teacherDataBundle = TeachersFragment.getTeachersFromIndex(this).get(teacherIndex);
        teacherName = teacherDataBundle.getString(TeacherConstants.KEY_NAME);
        teacherPrefix = teacherDataBundle.getInt(TeacherConstants.KEY_PREFIX);
        teacherCategory = teacherDataBundle.getInt(TeacherConstants.KEY_CATEGORY);
        teacherEmail = teacherDataBundle.getString(TeacherConstants.KEY_EMAIL);
        teacherAvatarId = teacherDataBundle.getInt(TeacherConstants.KEY_AVATAR);

        //set up views
        appBarLayout.setExpanded(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    supportFinishAfterTransition();
                } else{
                    finish();
                }
            }
        });

        switch (teacherPrefix){
            case TeacherConstants.PREFIX_DR:
                collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_dr),
                        teacherName));
                break;
            case TeacherConstants.PREFIX_MS:
                collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_ms),
                        teacherName));
                break;
            case TeacherConstants.PREFIX_MRS:
                collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_mrs),
                        teacherName));
                break;
            case TeacherConstants.PREFIX_MR:
                collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_mr),
                        teacherName));
                break;
        }
        emailView.setText(teacherEmail);
        //avatarImage.setImageDrawable(ContextCompat.getDrawable(this, teacherAvatarId));
        avatarImage.setImageResource(teacherAvatarId);
        /*if (savedInstanceState == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bioProgressBar.setVisibility(View.GONE);
                    bioView.setVisibility(View.VISIBLE);
                }
            }, 1000);
        } else{
            bioProgressBar.setVisibility(View.GONE);
            bioView.setVisibility(View.VISIBLE);
            bioView.setText(savedInstanceState.getString("bio")*//*bio*//*);
        }*/
    }

    public void onFabClick(View view){
        Log.d(TAG, "FAB clicked, emailing teacher");
        //define address
        String[] addresses =  new String[]{teacherEmail};

        //make intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if (intent.resolveActivity(getPackageManager()) != null) {
            //send email
            Log.d(TAG, "Sending email");
            startActivity(intent);
        } else{
            //something went wrong
            Log.d(TAG, "No app was found to handle email");
            AlertDialog.Builder noEmail = new AlertDialog.Builder(this);
            noEmail.setTitle(R.string.dialog_errorcalling_title)
                    .setMessage(R.string.dialog_erroremailing_message)
                    .setPositiveButton(R.string.dialog_action_ok, null)
                    .show();
        }
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("bio", bio);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        bio = savedInstanceState.getString("bio");
    }*/
}
