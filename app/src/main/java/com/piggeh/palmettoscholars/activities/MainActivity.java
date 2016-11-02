package com.piggeh.palmettoscholars.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
/*import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;*/
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
import android.widget.Toast;

//import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.classes.ConfigUtils;
import com.piggeh.palmettoscholars.classes.TeacherConstants;
import com.piggeh.palmettoscholars.fragments.ContactFragment;
import com.piggeh.palmettoscholars.fragments.DebugFragment;
import com.piggeh.palmettoscholars.fragments.HomeFragment;
import com.piggeh.palmettoscholars.fragments.NewsletterFragment;
import com.piggeh.palmettoscholars.fragments.ResourcesFragment;
import com.piggeh.palmettoscholars.fragments.TeachersFragment;
//import com.piggeh.palmettoscholars.listeners.AppBarStateChangeListener;
import com.piggeh.palmettoscholars.services.MyFirebaseMessagingService;
import com.piggeh.palmettoscholars.utils.PreferenceKeys;
//import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        TeachersFragment.OnTeacherClickListener,
        ResourcesFragment.OnResourceClickListener {
    //constants
    private static final String TAG = "MainActivity";
    //modes
    public static final int PAGE_HOME = 0;
    public static final int PAGE_ANNOUNCEMENTS = 1;
    public static final int PAGE_TEACHERS = 2;
    public static final int PAGE_RESOURCES = 3;
    public static final int PAGE_CONTACT_US = 4;
    public static final int PAGE_NEWSLETTER = 5;
    public static final int PAGE_SETTINGS = 6;
    public static final int PAGE_DEBUG = 7;

    //views
    public CoordinatorLayout coordinatorLayout;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    //private AppBarLayout appBarLayout;

    //vars
    private int navigationPage = PAGE_HOME;
    private int previousPage = PAGE_HOME;
    private boolean isLarge = false;
    private boolean hasTouch = true;
    //private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLarge = ConfigUtils.isLarge(this);
        //hasTouch = ConfigUtils.hasTouch(this);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            fab.bringToFront();
        }

        //appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Set up the Navigation Drawer/Panel and App Bar for large or small devices
        if (isLarge){
            //set up tablet layout
            Log.d(TAG, "Settings up tablet layout");

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else{
            //set up regular layout
            Log.d(TAG, "Setting up regular layout");

            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
        }

        //Set up Navigation Drawer list
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Switch pages if the app was launcher from a shortcut or notification
        if (getIntent().getIntExtra("navigation_page", -1) != -1){
            Log.d(TAG, "Launched with page data");
            int page = getIntent().getIntExtra("navigation_page", PAGE_HOME);
            switch (page){
                default:
                    if (savedInstanceState == null){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new HomeFragment())
                                .commit();
                    }
                    setupAppbarForPage(PAGE_HOME, true);
                    setupFabForPage(PAGE_HOME);
                    navigationView.setCheckedItem(R.id.drawer_home);
                    navigationPage = PAGE_HOME;
                    break;
                case PAGE_CONTACT_US:
                    if (savedInstanceState == null){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ContactFragment())
                                .commit();
                    }
                    setupAppbarForPage(PAGE_CONTACT_US, true);
                    setupFabForPage(PAGE_CONTACT_US);
                    navigationView.setCheckedItem(R.id.drawer_contactus);
                    navigationPage = PAGE_CONTACT_US;

                    //analytics
                    /*Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Contact Us");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);*/
                    break;
                case PAGE_TEACHERS:
                    if (savedInstanceState == null){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new TeachersFragment())
                                .commit();
                    }
                    setupAppbarForPage(PAGE_TEACHERS, true);
                    setupFabForPage(PAGE_TEACHERS);
                    navigationView.setCheckedItem(R.id.drawer_teachers);
                    navigationPage = PAGE_TEACHERS;

                    //analytics
                    /*Bundle bundle2 = new Bundle();
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, "Teachers");
                    bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle2);*/
                    break;
                case PAGE_RESOURCES:
                    if (savedInstanceState == null){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ResourcesFragment())
                                .commit();
                    }
                    setupAppbarForPage(PAGE_RESOURCES, true);
                    setupFabForPage(PAGE_RESOURCES);
                    navigationView.setCheckedItem(R.id.drawer_resources);
                    navigationPage = PAGE_RESOURCES;

                    //analytics
                    /*Bundle bundle4 = new Bundle();
                    bundle4.putString(FirebaseAnalytics.Param.ITEM_NAME, "Resources");
                    bundle4.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle4);*/
                    break;
                case PAGE_ANNOUNCEMENTS:
                    if (savedInstanceState == null){
                        /*Toast.makeText(this, "Announcements page coming soon", Toast.LENGTH_SHORT).show();getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new HomeFragment())
                                .commit();*/
                        openWebUrl("https://docs.google.com/document/d/1AVYG-oGyeHVFlNVwbXeZwR8t44Z-MCTmdSWwsmMN79k/edit");
                        NotificationManagerCompat notificationManager2 =
                                NotificationManagerCompat.from(this);
                        notificationManager2.cancel(MyFirebaseMessagingService.NOTIFICATION_ID_ANNOUNCEMENT);

                        //analytics
                        /*Bundle bundle5 = new Bundle();
                        bundle5.putString(FirebaseAnalytics.Param.ITEM_NAME, "Announcements");
                        bundle5.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle5);*/

                        finish();
                    }
                    /*setupAppbarForPage(PAGE_HOME, true);
                    setupFabForPage(PAGE_HOME);
                    navigationView.setCheckedItem(R.id.drawer_home);
                    navigationPage = PAGE_HOME;
                    //dismiss notifications
                    *//*NotificationManagerCompat notificationManager2 =
                            NotificationManagerCompat.from(this);
                    notificationManager2.cancel(MyFirebaseMessagingService.NOTIFICATION_ID_ANNOUNCEMENT);*/
                    break;
                case PAGE_NEWSLETTER:
                    if (savedInstanceState == null){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new NewsletterFragment())
                                .commit();
                    }
                    setupAppbarForPage(PAGE_NEWSLETTER, true);
                    setupFabForPage(PAGE_NEWSLETTER);
                    navigationView.setCheckedItem(R.id.drawer_newsletter);
                    navigationPage = PAGE_NEWSLETTER;
                    //dismiss notifications
                    NotificationManagerCompat notificationManager3 =
                            NotificationManagerCompat.from(this);
                    notificationManager3.cancel(MyFirebaseMessagingService.NOTIFICATION_ID_NEWSLETTER);

                    //analytics
                    /*Bundle bundle6 = new Bundle();
                    bundle6.putString(FirebaseAnalytics.Param.ITEM_NAME, "Newsletter");
                    bundle6.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle6);*/
                    break;
            }
        } else{
            //launched normally
            if (navigationPage == PAGE_HOME){
                // Display the fragment as the main content.
                if (savedInstanceState == null){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                }
            }
        }

        //set up FAB & App Bar
        setupFabForPage(navigationPage);
        setupAppbarForPage(navigationPage);

        //Set Firebase database to be persistable
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (DatabaseException e){
            e.printStackTrace();
        }

        //manage notification subscriptions
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(PreferenceKeys.notifAnnouncements, getResources().getBoolean(R.bool.preference_notif_announcements_default))){
            Log.d(TAG, "Subscribing to Announcements");
            FirebaseMessaging.getInstance().subscribeToTopic("announcements");
        } else{
            Log.d(TAG, "Unsubscribing from Announcements");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("announcements");
        }
        if (sharedPreferences.getBoolean(PreferenceKeys.notifNewsletters, getResources().getBoolean(R.bool.preference_notif_newsletter_default))){
            Log.d(TAG, "Subscribing to Newsletter");
            FirebaseMessaging.getInstance().subscribeToTopic("newsletters");
        } else{
            Log.d(TAG, "Unsubscribing from Newsletter");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("newsletters");
        }

        //set up Overview screen on Lollipop+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //Overview screen
            Bitmap overviewIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_flat);
            ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, overviewIcon, ContextCompat.getColor(this, R.color.colorPrimary));
            setTaskDescription(description);
            //status bar
            if (!isLarge){
                Log.d(TAG, "Isn't tablet, setting status bar to transparent");
                getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
            }
        }
    }

    public boolean switchNavigationPage(int page){
        if (navigationPage == page){
            Log.d(TAG, "Selected page is already open, do nothing");
            return false;
        }

        previousPage = navigationPage;

        invalidateOptionsMenu();

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

                //configure FAB & header for new page
                setupFabForPage(PAGE_HOME);
                setupAppbarForPage(PAGE_HOME);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_home);

                //analytics
                /*Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);*/

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

                //configure FAB & header for new page
                setupFabForPage(PAGE_CONTACT_US);
                setupAppbarForPage(PAGE_CONTACT_US);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_contactus);

                //analytics
                /*Bundle bundle2 = new Bundle();
                bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, "Contact Us");
                bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle2);*/

                Log.d(TAG, "Switched to Contact page");
                return true;
            case PAGE_TEACHERS:
                //switch fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.fragment_container, new TeachersFragment())
                        .commit();
                //set page variable
                navigationPage = PAGE_TEACHERS;

                //configure FAB & header for new page
                setupFabForPage(PAGE_TEACHERS);
                setupAppbarForPage(PAGE_TEACHERS);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_teachers);

                //analytics
                /*Bundle bundle3 = new Bundle();
                bundle3.putString(FirebaseAnalytics.Param.ITEM_NAME, "Teachers");
                bundle3.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle3);*/

                Log.d(TAG, "Switched to Teachers page");
                return true;
            case PAGE_SETTINGS:
                //analytics
                /*Bundle bundle4 = new Bundle();
                bundle4.putString(FirebaseAnalytics.Param.ITEM_NAME, "Settings");
                bundle4.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle4);*/

                Log.d(TAG, "Opened Settings");
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                return true;
            case PAGE_RESOURCES:
                //switch fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.fragment_container, new ResourcesFragment())
                        .commit();
                //set page variable
                navigationPage = PAGE_RESOURCES;

                //configure FAB & header for new page
                setupFabForPage(PAGE_RESOURCES);
                setupAppbarForPage(PAGE_RESOURCES);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_resources);

                //analytics
                /*Bundle bundle5 = new Bundle();
                bundle5.putString(FirebaseAnalytics.Param.ITEM_NAME, "Resources");
                bundle5.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle5);*/

                Log.d(TAG, "Switched to Resources page");
                return true;
            case PAGE_DEBUG:
                //switch fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.fragment_container, new DebugFragment())
                        .commit();
                //set page variable
                navigationPage = PAGE_DEBUG;

                //configure FAB & header for new page
                setupFabForPage(PAGE_DEBUG);
                setupAppbarForPage(PAGE_DEBUG);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_debug);

                Log.d(TAG, "Switched to Debug page");
                return true;
            case PAGE_NEWSLETTER:
                //switch fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.fragment_container, new NewsletterFragment())
                        .commit();
                //set page variable
                navigationPage = PAGE_NEWSLETTER;

                //configure FAB & header for new page
                setupFabForPage(PAGE_NEWSLETTER);
                setupAppbarForPage(PAGE_NEWSLETTER);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_newsletter);

                //analytics
                /*Bundle bundle6 = new Bundle();
                bundle6.putString(FirebaseAnalytics.Param.ITEM_NAME, "Newsletter");
                bundle6.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle6);*/

                Log.d(TAG, "Switched to Newsletter page");
                return true;
        }
    }

    public boolean setupFabForPage(int page){
        switch (page){
            default:
                Log.d(TAG, "Tried to set up FAB for unknown page");
                return false;
            case PAGE_HOME:
                fab.hide();

                Log.d(TAG, "Set up FAB for Home page");
                return true;
            case PAGE_CONTACT_US:
                if (fab.getVisibility() == View.VISIBLE){
                    fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onHidden(FloatingActionButton fab) {
                            super.onHidden(fab);
                            fab.setImageResource(R.drawable.ic_call);
                            fab.setContentDescription(getString(R.string.accessibility_fab_callphone));
                            fab.show();
                        }
                    });
                } else{
                    fab.setImageResource(R.drawable.ic_call);
                    fab.setContentDescription(getString(R.string.accessibility_fab_callphone));
                    fab.show();
                }

                Log.d(TAG, "Set up FAB for Contact page");
                return true;
            case PAGE_TEACHERS:
                fab.hide();

                Log.d(TAG, "Set up FAB for Teachers page");
                return true;
            case PAGE_RESOURCES:
                fab.hide();

                Log.d(TAG, "Set up FAB for Resources page");
                return true;
            case PAGE_DEBUG:
                if (fab.getVisibility() == View.VISIBLE){
                    fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onHidden(FloatingActionButton fab) {
                            super.onHidden(fab);
                            fab.setImageResource(R.drawable.ic_notifications_on);
                            fab.setContentDescription("Test announcement notification");
                            fab.show();
                        }
                    });
                } else{
                    fab.setImageResource(R.drawable.ic_notifications_on);
                    fab.setContentDescription("Test announcement notification");
                    fab.show();
                }

                Log.d(TAG, "Set up FAB for Debug page");
                return true;
            case PAGE_NEWSLETTER:
                fab.hide();

                Log.d(TAG, "Set up FAB for Newsletter page");
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //private int menuposition = 0;
    //public void setMenuPosition(int position){menuposition = position;}

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem openExternally = menu.findItem(R.id.menu_open_externally);
        if (navigationPage == PAGE_RESOURCES
                || navigationPage == PAGE_NEWSLETTER){
            openExternally.setVisible(true);
        } else{
            openExternally.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                Log.d(TAG, "Unknown menu item clicked");
                return false;
            case R.id.menu_open_externally:
                onFabClick(item.getActionView());
                return true;
        }
    }

    public boolean setupAppbarForPage(int page){
        return setupAppbarForPage(page, false);
    }

    public boolean setupAppbarForPage(int page, boolean recreated){
        switch (page){
            default:
                Log.d(TAG, "Tried to set up app bar for unknown page");
                return false;
            case PAGE_HOME:
                getSupportActionBar().setTitle(getString(R.string.toolbar_title));
                Log.d(TAG, "Set up app bar for Home page");
                return true;
            case PAGE_CONTACT_US:
                if (!isLarge){
                    getSupportActionBar().setTitle(getString(R.string.drawer_contactus));
                }
                Log.d(TAG, "Set up app bar for Contact page");
                return true;
            case PAGE_TEACHERS:
                if (!isLarge){
                    getSupportActionBar().setTitle(getString(R.string.drawer_teachers));
                }
                Log.d(TAG, "Set up app bar for Teachers page");
                return true;
            case PAGE_SETTINGS:
                if (!isLarge){
                    getSupportActionBar().setTitle(getString(R.string.drawer_settings));
                }
                Log.d(TAG, "Set up app bar for Settings page");
                return true;
            case PAGE_RESOURCES:
                if (!isLarge){
                    getSupportActionBar().setTitle(getString(R.string.drawer_resources));
                }
                Log.d(TAG, "Set up app bar for Resources page");
                return true;
            case PAGE_DEBUG:
                if (!isLarge){
                    getSupportActionBar().setTitle(getString(R.string.drawer_debug));
                }
                Log.d(TAG, "Set up app bar for Debug page");
                return true;
            case PAGE_NEWSLETTER:
                if (!isLarge){
                    getSupportActionBar().setTitle(getString(R.string.drawer_newsletter));
                }
                Log.d(TAG, "Set up app bar for Newsletter page");
                return true;
        }
    }

    public void openSettings(View view){
        switchNavigationPage(PAGE_SETTINGS);
    }
    public void newsletterEmails(View view){
        openWebUrl("https://palmettoscholarsacademy.us4.list-manage.com/subscribe?u=96897800bc040556edb4d8d9c&id=2c6db3f60b");
    }
    public void psaStore(View view){
        openWebUrl("http://psaphoenix.qbstores.com/");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //save navigation page for setting up FAB again
        savedInstanceState.putInt("navigation_page", navigationPage);
        savedInstanceState.putInt("previous_page", previousPage);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        //restore navigation page to set up FAB with
        navigationPage = savedInstanceState.getInt("navigation_page");
        previousPage = savedInstanceState.getInt("previous_page");

        //set up FAB & header
        setupFabForPage(navigationPage);
        setupAppbarForPage(navigationPage, true);
    }

    public void onFabClick(View view){
        Log.d(TAG, "FAB clicked");
        switch (navigationPage){
            default:
                Log.d(TAG, "Unknown page");
                break;
            case PAGE_HOME:
                Log.d(TAG, "Enroll now");
                openWebUrl("http://www.palmettoscholarsacademy.org/attend-psa/");
                break;
            case PAGE_CONTACT_US:
                Log.d(TAG, "Calling phone");
                tryToCallPhone();
                break;
            case PAGE_SETTINGS:
                Log.d(TAG, "Going back");
                onBackPressed();
                break;
            case PAGE_TEACHERS:
                Log.d(TAG, "Opening teachers page externally");
                openWebUrl("http://www.palmettoscholarsacademy.org/psa-parents/teacherpages/");
                //Snackbar.make(coordinatorLayout, "Search teachers", Snackbar.LENGTH_SHORT).show();
                break;
            case PAGE_RESOURCES:
                Log.d(TAG, "Opening resources page externally");
                openWebUrl("http://www.palmettoscholarsacademy.org/psa-parents/quick-links/");
                break;
            case PAGE_DEBUG:
                Log.d(TAG, "Testing Announcement notification");
                //testAnnouncementNotification();
                break;
            case PAGE_NEWSLETTER:
                Log.d(TAG, "Opening newsletters page externally");
                openWebUrl("http://www.palmettoscholarsacademy.org/psa-parents/newsletter/");
                break;
        }
    }

    @Override
    public void onTeacherClick(View view, int teacherId){
        Log.d(TAG, "Teacher index " + String.valueOf(teacherId) + " clicked");
        //Toast.makeText(this, "Teacher index " + String.valueOf(teacherId) + " clicked", Toast.LENGTH_SHORT).show();
        Intent teacherDetail = new Intent(this, TeacherDetailActivity.class);
        teacherDetail.putExtra(TeacherConstants.KEY_INDEX, teacherId);
        teacherDetail.putExtra("launched_from_shortcut", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                if (isInMultiWindowMode()){
                    startActivity(teacherDetail);
                    Log.d(TAG, "Opening teacher on Nougat+ in multiwindow");
                } else{
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            Pair.create(view.findViewById(R.id.teacherAvatar), "avatar"),
                            Pair.create(findViewById(android.R.id.navigationBarBackground), Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME),
                            Pair.create(findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                    );
                    startActivity(teacherDetail, options.toBundle());
                    Log.d(TAG, "Opening teacher on Nougat+");
                }
            } else{
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        Pair.create(view.findViewById(R.id.teacherAvatar), "avatar"),
                        Pair.create(findViewById(android.R.id.navigationBarBackground), Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME),
                        Pair.create(findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                );
                startActivity(teacherDetail, options.toBundle());
                Log.d(TAG, "Opening teacher on Lollipop+");
            }
        } else{
            startActivity(teacherDetail);
            Log.d(TAG, "Opening teacher on Kitkat-");
        }
    }

    @Override
    public void onResourceClick(View view, int position, String url){
        openWebUrl(url);
    }

    public void testNotifications(View view){
        switch (view.getId()){
            case R.id.button_debug_subscribe:
                FirebaseMessaging.getInstance().subscribeToTopic("debug");
                Toast.makeText(this, "Subscribed to debug notifications", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_debug_unsubscribe:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("debug");
                Toast.makeText(this, "Unsubscribed from debug notifications", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !ConfigUtils.isLarge(this)){
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG, "Navigation drawer item clicked");
        switch (item.getItemId()){
            default:
                Toast.makeText(this, R.string.popup_demo_comingsoon, Toast.LENGTH_SHORT).show();
                return false;
            case R.id.drawer_home:
                switchNavigationPage(PAGE_HOME);
                break;
            case R.id.drawer_contactus:
                switchNavigationPage(PAGE_CONTACT_US);
                break;
            case R.id.drawer_settings:
                switchNavigationPage(PAGE_SETTINGS);
                break;
            case R.id.drawer_teachers:
                switchNavigationPage(PAGE_TEACHERS);
                break;
            case R.id.drawer_calendar:
                openWebUrl("https://calendar.google.com/calendar/embed?src=pvnh2dgi9jetb22q9ldj26co1k@group.calendar.google.com&ctz=America/New_York");
                return false;
            case R.id.drawer_resources:
                switchNavigationPage(PAGE_RESOURCES);
                break;
            case R.id.drawer_debug:
                Log.d(TAG, "Opened Debug Mode page");
                switchNavigationPage(PAGE_DEBUG);
                break;
            case R.id.drawer_notifications:
                openWebUrl("https://docs.google.com/document/d/1AVYG-oGyeHVFlNVwbXeZwR8t44Z-MCTmdSWwsmMN79k/edit");
                return false;
            case R.id.drawer_newsletter:
                switchNavigationPage(PAGE_NEWSLETTER);
                break;
        }

        if (!isLarge){
            Log.d(TAG, "Isn't tablet, closing drawer");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!ConfigUtils.isLarge(this)){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                Log.d(TAG, "Back pressed, closing nav drawer");
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if (navigationPage == PAGE_SETTINGS){
                    //back to previous page
                    Log.d(TAG, "Back pressed, back to previous page");
                    switchNavigationPage(previousPage);
                } else if ( navigationPage != PAGE_HOME){
                    //back to Home page
                    Log.d(TAG, "Back pressed, back to Home page");
                    switchNavigationPage(PAGE_HOME);
                } else{
                    super.onBackPressed();
                }
            }
        } else{
            if (navigationPage == PAGE_SETTINGS){
                //back to previous page
                Log.d(TAG, "Back pressed, back to previous page");
                switchNavigationPage(previousPage);
            } else if ( navigationPage != PAGE_HOME){
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

    private boolean openWebUrl(String url){
        Uri webpage = Uri.parse(url);
        CustomTabsIntent.Builder customTabBuilder = new CustomTabsIntent.Builder();
        customTabBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = customTabBuilder.build();
        customTabsIntent.launchUrl(this, webpage);
        return true;
    }
    public static boolean openWebUrlExternally(Context context, String url){
        Uri webpage = Uri.parse(url);
        Intent open = new Intent(Intent.ACTION_VIEW, webpage);
        if (open.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(open);
            return true;
        } else{
            return false;
        }
    }
    public static void openWebUrl(Activity activity, String url){
        Uri webpage = Uri.parse(url);
        CustomTabsIntent.Builder customTabBuilder = new CustomTabsIntent.Builder();
        customTabBuilder.setToolbarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = customTabBuilder.build();
        customTabsIntent.launchUrl(activity, webpage);
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
                String[] addresses =  new String[]{getString(R.string.contact_email_address)};

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
