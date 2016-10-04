package com.piggeh.palmettoscholars.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.classes.CircleTransform;
import com.piggeh.palmettoscholars.classes.TeacherConstants;
import com.piggeh.palmettoscholars.utils.DPUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
    private Button emailButton;
    private RelativeLayout connectionErrorLayout;
    private RelativeLayout teacherDataLayout;
    //vars
    private boolean launchedFromShortcut = false;

    /*private DatabaseReference databaseReference;*/

    /*private int teacherIndex;*/
    private String teacherName;
    private long teacherPrefix = 3;
    /*private int teacherCategory;*/
    private String teacherEmail;
    //private Bundle teacherDataBundle;
    private String teacherAvatarUrl;
    private String bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        Firebase.setAndroidContext(this);

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
        emailButton = (Button) findViewById(R.id.button_contact_email);
        connectionErrorLayout = (RelativeLayout) findViewById(R.id.relativeLayout_error_loading);
        teacherDataLayout = (RelativeLayout) findViewById(R.id.relativeLayout_teacherInfo);

        //get data from Intent
        launchedFromShortcut = getIntent().getBooleanExtra("launched_from_shortcut", false);

        //databaseReference = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra(TeacherConstants.KEY_INDEX));
        Firebase myFirebaseRef = new Firebase(getIntent().getStringExtra(TeacherConstants.KEY_INDEX));
        Query queryRef = myFirebaseRef;
        final Context context = this;
        queryRef.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()){
                    default:
                        Log.d(TAG, "Unknown data tag found");
                        break;
                    case "name":
                        teacherName = (String) dataSnapshot.getValue();
                        setupName();
                        break;
                    case "prefix":
                        teacherPrefix = (long) dataSnapshot.getValue();
                        setupName();
                        break;
                    case "email":
                        teacherEmail = (String) dataSnapshot.getValue();
                        emailView.setText(teacherEmail);
                        emailButton.setEnabled(true);
                        break;
                    case "bio":
                        bio = (String) dataSnapshot.getValue();
                        //bioView.setText(bio);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            bioView.setText(Html.fromHtml((String)dataSnapshot.getValue(), Html.FROM_HTML_MODE_COMPACT));
                        } else{
                            bioView.setText(Html.fromHtml((String)dataSnapshot.getValue()));
                        }
                        bioProgressBar.setVisibility(View.GONE);
                        break;
                    case "avatar":
                        teacherAvatarUrl = (String) dataSnapshot.getValue();
                        Picasso.with(context).load((String) dataSnapshot.getValue()).placeholder(R.drawable.avatar_loading).error(R.drawable.avatar_failed).into(avatarImage);
                        break;
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "onCancelled: " + firebaseError.toString());
                teacherDataLayout.setVisibility(View.GONE);
                connectionErrorLayout.setVisibility(View.VISIBLE);
            }
        });

        /*databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                switch (dataSnapshot.getKey()){
                    default:
                        Log.d(TAG, "Unknown data tag found");
                        break;
                    case "name":
                        teacherName = (String) dataSnapshot.getValue();
                        setupName();
                        break;
                    case "prefix":
                        teacherPrefix = (int) dataSnapshot.getValue();
                        setupName();
                        break;
                    case "email":
                        teacherEmail = (String) dataSnapshot.getValue();
                        emailView.setText(teacherEmail);
                        emailButton.setEnabled(true);
                        break;
                    case "bio":
                        bio = (String) dataSnapshot.getValue();
                        bioView.setText(bio);
                        bioProgressBar.setVisibility(View.GONE);
                }

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                //Comment newComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                //Comment movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load teacher info.", Toast.LENGTH_SHORT).show();
            }
        });*/

        //set up views
        appBarLayout.setExpanded(true);

        if (launchedFromShortcut){
            Log.d(TAG, "Launched from shortcut, showing X button");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_close);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    supportFinishAfterTransition();
                } else{
                    finish();
                }*/
            }
        });
    }

    private void setupName(){
        if (teacherPrefix == TeacherConstants.PREFIX_DR){
            collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_dr),
                    teacherName));
        } else if (teacherPrefix == TeacherConstants.PREFIX_MS){
            collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_ms),
                    teacherName));
        } else if (teacherPrefix == TeacherConstants.PREFIX_MRS){
            collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_mrs),
                    teacherName));
        } else{
            collapsingToolbarLayout.setTitle(String.format(getString(R.string.teachers_prefix_mr),
                    teacherName));
        }

        /*switch (teacherPrefix){
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
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_add_shortcut_teacher:
                addShortcut();
                return true;
        }

        return false;
    }

    private String formatName(){
            if (teacherPrefix == TeacherConstants.PREFIX_DR){
                return String.format(getString(R.string.teachers_prefix_dr),
                        teacherName);
            } else if (teacherPrefix == TeacherConstants.PREFIX_MS){
                return String.format(getString(R.string.teachers_prefix_ms),
                        teacherName);
            } else if (teacherPrefix == TeacherConstants.PREFIX_MRS){
                return String.format(getString(R.string.teachers_prefix_mrs),
                        teacherName);
            } else{
                return String.format(getString(R.string.teachers_prefix_mr),
                        teacherName);
            }
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

    public Intent addIntent;
    private void addShortcut(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.dialog_adding_shortcut));

        Intent shortcutIntent = new Intent(this, TeacherDetailActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.putExtra(TeacherConstants.KEY_INDEX, getIntent().getStringExtra(TeacherConstants.KEY_INDEX));
        shortcutIntent.putExtra("launched_from_shortcut", true);

        /*final Intent */addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, formatName());
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(R.string.dialog_action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addIntent = null;
                progressDialog.dismiss();
            }
        });
        progressDialog.show();

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try{
                    int size = DPUtils.convertDpToPx(44);
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, Bitmap.createScaledBitmap(bitmap, size, size, false));
                    sendBroadcast(addIntent);

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), formatName() + " " + getString(R.string.toast_shortcut_added), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                int size = DPUtils.convertDpToPx(44);
                try{
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                            R.drawable.avatar_loading));
                    sendBroadcast(addIntent);

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), formatName() + " " + getString(R.string.toast_shortcut_added), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(this).load(teacherAvatarUrl).transform(new CircleTransform()).into(target);

        /*int size = DPUtils.convertDpToPx(46);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, Bitmap.createScaledBitmap(convertToBitmap(avatarImage.getDrawable(), ), size, size, false));
        sendBroadcast(addIntent);

        progressDialog.hide();
        Toast.makeText(getApplicationContext(), formatName() + " " + getString(R.string.toast_shortcut_added), Toast.LENGTH_SHORT).show();*/
    }

    /*public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }*/

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
