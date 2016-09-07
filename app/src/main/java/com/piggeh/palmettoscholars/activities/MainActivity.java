package com.piggeh.palmettoscholars.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.fragments.ContactFragment;
import com.piggeh.palmettoscholars.fragments.HomeFragment;
import com.piggeh.palmettoscholars.utils.PSANotifications;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener {
    //constants
    private static final String TAG = "MainActivity";
    //modes
    public static final int PAGE_HOME = 0;
    public static final int PAGE_ENROLLMENT = 1;
    public static final int PAGE_ABOUT_US = 2;
    public static final int PAGE_NOTIFICATIONS = 3;
    public static final int PAGE_TEACHERS = 4;
    public static final int PAGE_PARENTS = 5;
    public static final int PAGE_STUDENTS = 6;
    public static final int PAGE_CONTACT_US = 7;
    public static final int PAGE_NEWSLETTER = 8;
    public static final int PAGE_SETTINGS = 9;

    //views
    public CoordinatorLayout coordinatorLayout;
    //private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout fragmentContainer;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private AppBarLayout appBarLayout;
    private ImageView appbarImage;

    //vars
    private int navigationPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "FAB clicked, doing nothing for now");
                Toast.makeText(getApplicationContext(), "Enroll now", Toast.LENGTH_SHORT).show();
            }
        });*/

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appbarImage = (ImageView) findViewById(R.id.appbarImage);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //tabLayout = (TabLayout) findViewById(R.id.tablayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        if (navigationPage == PAGE_HOME){
            // Display the fragment as the main content.
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        }
        //set up FAB & header
        setupFabForPage(navigationPage);
        setupHeaderForPage(navigationPage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //Overview screen
            Bitmap overviewIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_flat);
            ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, overviewIcon, ContextCompat.getColor(this, R.color.colorPrimary));
            setTaskDescription(description);
            //status bar
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    public boolean switchNavigationPage(int page){
        if (navigationPage == page){
            Log.d(TAG, "Selected page is already open, do nothing");
            return false;
        }

        switch (page){
            default:
                Log.d(TAG, "Tried to switch to unknown page");
                return false;
            case PAGE_HOME:
                //switch fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                //set page variable
                navigationPage = PAGE_HOME;
                //set toolbar title
                /*collapsingToolbarLayout.setTitle(getString(R.string.toolbar_title));*/

                //configure FAB & header for new page
                setupFabForPage(PAGE_HOME);
                setupHeaderForPage(PAGE_HOME);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_home);

                //expand toolbar
                appBarLayout.setExpanded(true);

                Log.d(TAG, "Switched to Home page");
                return true;
            case PAGE_CONTACT_US:
                //switch fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.fragment_container, new ContactFragment())
                        .commit();
                //set page variable
                navigationPage = PAGE_CONTACT_US;
                //set toolbar title
                /*collapsingToolbarLayout.setTitle(getString(R.string.drawer_contactus));*/

                //configure FAB & header for new page
                setupFabForPage(PAGE_CONTACT_US);
                setupHeaderForPage(PAGE_CONTACT_US);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_contactus);

                //expand toolbar
                appBarLayout.setExpanded(true);

                Log.d(TAG, "Switched to Contact page");
                return true;
        }
    }

    public boolean setupFabForPage(int page){
        switch (page){
            default:
                Log.d(TAG, "Tried to set up FAB for unknown page");
                return false;
            case PAGE_HOME:
                fab.setImageResource(R.drawable.ic_enrollment);
                fab.setContentDescription(getString(R.string.accessibility_fab_enrollnow));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "FAB clicked, doing nothing for now");
                        Toast.makeText(getApplicationContext(), "Enroll now", Toast.LENGTH_SHORT).show();

                        //resources
                        Resources resources = getResources();
                        Resources systemResources = Resources.getSystem();

                        //notification settings intent
                        Intent settingsIntent = new Intent(getApplicationContext(), MainActivity.class);
                        PendingIntent settingsPendingIntent =
                                PendingIntent.getActivity(
                                        getApplicationContext(),
                                        0,
                                        settingsIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );
                        //notification manager
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        //announcement notification
                        /*NotificationCompat.Builder announcement = new NotificationCompat.Builder(getApplicationContext());
                        announcement.setSmallIcon(R.drawable.notification_icon);
                        announcement.setContentTitle("No homework forever");
                        announcement.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        announcement.setContentText(getString(R.string.notif_announcement_text));
                        announcement.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        announcement.addAction(R.drawable.ic_notifications_off, getString(R.string.notif_action_options), settingsPendingIntent);
                        announcement.setAutoCancel(true);
                        announcement.setContentIntent(settingsPendingIntent);
                        announcement.setDefaults(NotificationCompat.DEFAULT_VIBRATE|NotificationCompat.DEFAULT_SOUND);
                        announcement.setLights(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                                resources.getInteger(systemResources
                                        .getIdentifier("config_defaultNotificationLedOn", "integer", "android")),
                                resources.getInteger(systemResources
                                        .getIdentifier("config_defaultNotificationLedOff", "integer", "android")));


                        //newsletter notification
                        NotificationCompat.Builder newsletter = new NotificationCompat.Builder(getApplicationContext());
                        newsletter.setSmallIcon(R.drawable.ic_newsletter);
                        newsletter.setContentTitle(getString(R.string.notif_newsletter_title));
                        newsletter.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        newsletter.setContentText("Title of newsletter");
                        newsletter.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        newsletter.addAction(R.drawable.ic_notifications_off, getString(R.string.notif_action_options), settingsPendingIntent);
                        newsletter.setAutoCancel(true);
                        newsletter.setContentIntent(settingsPendingIntent);
                        newsletter.setDefaults(NotificationCompat.DEFAULT_VIBRATE|NotificationCompat.DEFAULT_SOUND);
                        newsletter.setLights(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                                resources.getInteger(systemResources
                                        .getIdentifier("config_defaultNotificationLedOn", "integer", "android")),
                                resources.getInteger(systemResources
                                        .getIdentifier("config_defaultNotificationLedOff", "integer", "android")));*/

                        mNotificationManager.notify(1, PSANotifications.generateAnnouncement(getApplicationContext(), "No homework", settingsPendingIntent, settingsPendingIntent)/*announcement.build()*/);
                        //mNotificationManager.notify(2, newsletter.build());
                    }
                });
                Log.d(TAG, "Set up FAB for Home page");
                return true;
            case PAGE_CONTACT_US:
                fab.setImageResource(R.drawable.ic_call);
                fab.setContentDescription(getString(R.string.accessibility_fab_callphone));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "FAB clicked, calling phone");
                        tryToCallPhone();
                    }
                });
                Log.d(TAG, "Set up FAB for Contact page");
                return true;
        }
    }
    public boolean setupHeaderForPage(int page){
        switch (page){
            default:
                Log.d(TAG, "Tried to set up header for unknown page");
                return false;
            case PAGE_HOME:
                collapsingToolbarLayout.setTitle(getString(R.string.toolbar_title));
                appbarImage.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Set up header for Home page");
                return true;
            case PAGE_CONTACT_US:
                collapsingToolbarLayout.setTitle(getString(R.string.drawer_contactus));
                appbarImage.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Set up header for Contact page");
                return true;
        }
    }

    //doesn't work
    /*public boolean isAppbarFullyExpanded(){
        *//*boolean fullyExpanded =
                (appBarLayout.getHeight() - appBarLayout.getBottom()) == 0;*//*
        return (appBarLayout.getHeight() - appBarLayout.getBottom()) == 0;
    }*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //save navigation page for setting up FAB again
        savedInstanceState.putInt("navigation_page", navigationPage);
        //save whether app bar is expanded, so I can collapse it again if needed
        //savedInstanceState.putBoolean("appbar_expanded", isAppbarFullyExpanded());
        //workaround for collapsed title being in the wrong place after rotating
        appBarLayout.setExpanded(true, false);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        //restore navigation page to set up FAB with
        navigationPage = savedInstanceState.getInt("navigation_page");
        appBarLayout.setExpanded(true, false);
        //if app bar wasn't expanded before, collapse it
        /*if (savedInstanceState.getBoolean("appbar_expanded:, true")){
            Log.d(TAG, "App bar was expanded before, expanding");
            appBarLayout.setExpanded(true, false);
        } else{
            Log.d(TAG, "App bar wasn't expanded before, collapsing");
            appBarLayout.setExpanded(false, false);
        }*/
        //set up FAB & header
        setupFabForPage(navigationPage);
        setupHeaderForPage(navigationPage);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG, "Navigation drawer item clicked");
        switch (item.getItemId()){
            case R.id.drawer_home:
                switchNavigationPage(PAGE_HOME);
                break;
            case R.id.drawer_contactus:
                switchNavigationPage(PAGE_CONTACT_US);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "Back pressed, closing nav drawer");
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ( navigationPage != PAGE_HOME){
                //back to Home page
                Log.d(TAG, "Back pressed, back to Home page");
                switchNavigationPage(PAGE_HOME);
            } else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    public boolean openWebUrl(String url){
        Uri webpage = Uri.parse(url);
        Intent open = new Intent(Intent.ACTION_VIEW, webpage);
        if (open.resolveActivity(getPackageManager()) != null) {
            startActivity(open);
            return true;
        } else{
            return false;
        }
    }

    private static final int PERMISSION_REQUEST_PHONE = 1;

    public void contactButtonClicked(View view){
        switch(view.getId()){
            default:
                Log.d(TAG, "Unknown contact button clicked");
                break;
            case R.id.button_contact_call:
                Log.d(TAG, "Call button clicked");
                tryToCallPhone();
                break;
            case R.id.button_contact_email:
                Log.d(TAG, "Email button clicked");

                //define address
                String[] addresses =  new String[]{"info@palmettoscholarsacademy.org"};

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
                break;
            case R.id.button_contact_twitter:
                //open school twitter page
                Log.d(TAG, "Opening school Twitter page");
                Uri webpage = Uri.parse("https://twitter.com/PSASchool");
                Intent twitter = new Intent(Intent.ACTION_VIEW, webpage);
                if (twitter.resolveActivity(getPackageManager()) != null) {
                    startActivity(twitter);
                } else{
                    //something went wrong
                    Log.d(TAG, "No app was found to handle internet");
                    AlertDialog.Builder noEmail = new AlertDialog.Builder(this);
                    noEmail.setTitle(R.string.dialog_errorcalling_title)
                            .setMessage(R.string.dialog_errorinternet_message)
                            .setPositiveButton(R.string.dialog_action_ok, null)
                            .show();
                }
                break;
        }
    }

    public void tryToCallPhone(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            //request Phone permission
            Log.d(TAG, "Requesting Phone permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_PHONE);
        } else{
            //app has Phone permission
            Log.d(TAG, "App has Phone permission, placing call");
            placeCall();
        }
    }

    public void placeCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:8433004118"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            //place call
            Log.d(TAG, "Placing call");
            startActivity(intent);
        } else{
            //something went wrong
            Log.d(TAG, "No app was found to handle call");
            AlertDialog.Builder noPhone = new AlertDialog.Builder(this);
            noPhone.setTitle(R.string.dialog_errorcalling_title)
                    .setMessage(R.string.dialog_errorcalling_message)
                    .setPositiveButton(R.string.dialog_action_ok, null)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    placeCall();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AlertDialog.Builder phoneDenied = new AlertDialog.Builder(this);
                    phoneDenied.setTitle(R.string.dialog_permissiondenied_phone_title)
                            .setMessage(R.string.dialog_permissiondenied_phone_message)
                            .setPositiveButton(R.string.dialog_action_ok, null)
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
