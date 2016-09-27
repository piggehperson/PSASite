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
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
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
import com.piggeh.palmettoscholars.fragments.SettingsFragment;
import com.piggeh.palmettoscholars.fragments.TeachersFragment;
import com.piggeh.palmettoscholars.listeners.AppBarStateChangeListener;
import com.piggeh.palmettoscholars.services.MyFirebaseMessagingService;
import com.piggeh.palmettoscholars.utils.PreferenceKeys;
/*import com.piggeh.palmettoscholars.utils.PSANotifications;

import java.net.HttpURLConnection;
import java.net.URL;*/

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
    //private TabLayout tabLayout;
    //private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //private FrameLayout fragmentContainer;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private AppBarLayout appBarLayout;
    private ImageView appbarImage;

    //vars
    private int navigationPage = PAGE_HOME;
    private int previousPage = PAGE_HOME;
    private int appbarState = AppBarStateChangeListener.STATE_IDLE;
    private boolean isLarge = false;
    private boolean hasTouch = true;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLarge = ConfigUtils.isLarge(this);
        //hasTouch = ConfigUtils.hasTouch(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            fab.bringToFront();
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appbarImage = (ImageView) findViewById(R.id.appbarImage);

        /*if (appbarState == AppBarStateChangeListener.STATE_COLLAPSED
                || navigationPage == PAGE_SETTINGS){
            appBarLayout.setExpanded(false, false);
        }*/

        //set listener for appbar collapsed state changes
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, /*State*/int state) {
                //Log.d(TAG, "App bar collapsed state changed to " + String.valueOf(state));
                appbarState = state;
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (isLarge){
            //set up tablet layout
            Log.d(TAG, "Settings up tablet layout");

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            /*drawerLayout.setDrawerElevation(0);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            drawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));*/

            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
            params.setScrollFlags(0);

            //appBarLayout.setElevation(getResources().getDimension(R.dimen.appbar_elevation));
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

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
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Contact Us");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
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
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, "Teachers");
                    bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle2);
                    break;
                case PAGE_SETTINGS:
                    if (savedInstanceState == null){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new SettingsFragment())
                                .commit();
                    }
                    setupAppbarForPage(PAGE_SETTINGS, true);
                    setupFabForPage(PAGE_SETTINGS);
                    navigationView.setCheckedItem(R.id.drawer_settings);
                    navigationPage = PAGE_SETTINGS;

                    //dismiss notifications
                    NotificationManagerCompat notificationManager =
                            NotificationManagerCompat.from(this);
                    notificationManager.cancelAll();

                    //analytics
                    Bundle bundle3 = new Bundle();
                    bundle3.putString(FirebaseAnalytics.Param.ITEM_NAME, "Settings");
                    bundle3.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle3);
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
                    Bundle bundle4 = new Bundle();
                    bundle4.putString(FirebaseAnalytics.Param.ITEM_NAME, "Resources");
                    bundle4.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle4);
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
                        Bundle bundle5 = new Bundle();
                        bundle5.putString(FirebaseAnalytics.Param.ITEM_NAME, "Announcements");
                        bundle5.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle5);

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
                    Bundle bundle6 = new Bundle();
                    bundle6.putString(FirebaseAnalytics.Param.ITEM_NAME, "Newsletter");
                    bundle6.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle6);
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

        /*if (navigationPage == PAGE_HOME){
            // Display the fragment as the main content.
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        }*/
        //set up FAB & header
        setupFabForPage(navigationPage);
        setupAppbarForPage(navigationPage);

        //show Demo App popup
        if (getResources().getBoolean(R.bool.is_demo)
                && savedInstanceState == null){
            Log.d(TAG, "Showing Demo App popup");
            AlertDialog.Builder demoApp = new AlertDialog.Builder(this);
            demoApp.setTitle(R.string.dialog_demo_title);
            demoApp.setMessage(R.string.dialog_demo_message);
            demoApp.setPositiveButton(R.string.dialog_action_ok, null);
            demoApp.show();

            FirebaseMessaging.getInstance().unsubscribeFromTopic("debug");
        }

        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (DatabaseException e){
            e.printStackTrace();
        }

        //manage notification subscriptions
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

                //expand toolbar
                /*appBarLayout.setExpanded(true);*/

                //analytics
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

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

                //expand toolbar
                /*appBarLayout.setExpanded(true);*/

                //analytics
                Bundle bundle2 = new Bundle();
                bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, "Contact Us");
                bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle2);

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

                //expand toolbar
                /*appBarLayout.setExpanded(true);*/

                //analytics
                Bundle bundle3 = new Bundle();
                bundle3.putString(FirebaseAnalytics.Param.ITEM_NAME, "Teachers");
                bundle3.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle3);

                Log.d(TAG, "Switched to Teachers page");
                return true;
            case PAGE_SETTINGS:
                //switch fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
                //set page variable
                navigationPage = PAGE_SETTINGS;

                //configure FAB & header for new page
                setupFabForPage(PAGE_SETTINGS);
                setupAppbarForPage(PAGE_SETTINGS);

                //set selected item in drawer, for switching pages programmatically
                navigationView.setCheckedItem(R.id.drawer_settings);

                //collapse toolbar
                //appBarLayout.setExpanded(false);

                //analytics
                Bundle bundle4 = new Bundle();
                bundle4.putString(FirebaseAnalytics.Param.ITEM_NAME, "Settings");
                bundle4.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle4);

                Log.d(TAG, "Switched to Settings page");
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

                //expand toolbar
                /*appBarLayout.setExpanded(true);*/

                //analytics
                Bundle bundle5 = new Bundle();
                bundle5.putString(FirebaseAnalytics.Param.ITEM_NAME, "Resources");
                bundle5.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle5);

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

                //expand toolbar
                /*appBarLayout.setExpanded(true);*/

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

                //expand toolbar
                /*appBarLayout.setExpanded(true);*/

                //analytics
                Bundle bundle6 = new Bundle();
                bundle6.putString(FirebaseAnalytics.Param.ITEM_NAME, "Newsletter");
                bundle6.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "navigation_page");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle6);

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
                fab.setImageResource(R.drawable.ic_enrollment);
                fab.setContentDescription(getString(R.string.accessibility_fab_enrollnow));
                //fab.setVisibility(View.VISIBLE);
                fab.show();
                Log.d(TAG, "Set up FAB for Home page");
                return true;
            case PAGE_CONTACT_US:
                fab.setImageResource(R.drawable.ic_call);
                fab.setContentDescription(getString(R.string.accessibility_fab_callphone));
                //fab.setVisibility(View.VISIBLE);
                fab.show();
                Log.d(TAG, "Set up FAB for Contact page");
                return true;
            case PAGE_TEACHERS:
                fab.setImageResource(R.drawable.ic_open_externally);
                fab.setContentDescription(getString(R.string.accessibility_fab_openexternally));
                //fab.setVisibility(View.VISIBLE);
                fab.show();
                Log.d(TAG, "Set up FAB for Teachers page");
                return true;
            case PAGE_SETTINGS:
                fab.setImageResource(R.drawable.ic_check);
                fab.setContentDescription(getString(R.string.accessibility_fab_done));
                Log.d(TAG, "Set up FAB for Settings page");
                return true;
            case PAGE_RESOURCES:
                fab.setImageResource(R.drawable.ic_open_externally);
                fab.setContentDescription(getString(R.string.accessibility_fab_openexternally));
                //fab.setVisibility(View.VISIBLE);
                fab.show();
                Log.d(TAG, "Set up FAB for Resources page");
                return true;
            case PAGE_DEBUG:
                fab.setImageResource(R.drawable.ic_notifications_on);
                fab.setContentDescription("Test announcement notification");
                //fab.setVisibility(View.VISIBLE);
                fab.show();
                Log.d(TAG, "Set up FAB for Debug page");
                return true;
            case PAGE_NEWSLETTER:
                fab.setImageResource(R.drawable.ic_open_externally);
                fab.setContentDescription(getString(R.string.accessibility_fab_openexternally));
                //fab.setVisibility(View.VISIBLE);
                fab.show();
                Log.d(TAG, "Set up FAB for Newsletter page");
                return true;
        }
    }
    public boolean setupAppbarForPage(int page){
        return setupAppbarForPage(page, false);
    }

    public boolean setupAppbarForPage(int page, boolean recreated){
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        switch (page){
            default:
                Log.d(TAG, "Tried to set up app bar for unknown page");
                return false;
            case PAGE_HOME:
                collapsingToolbarLayout.setTitle(getString(R.string.toolbar_title));
                appbarImage.setVisibility(View.INVISIBLE);
                if (!isLarge){
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
                appBarLayout.setExpanded(true);
                Log.d(TAG, "Set up app bar for Home page");
                return true;
            case PAGE_CONTACT_US:
                collapsingToolbarLayout.setTitle(getString(R.string.drawer_contactus));
                //TODO: Maybe make banner image for Contact Us page
                appbarImage.setVisibility(View.INVISIBLE);
                if (!isLarge){
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
                appBarLayout.setExpanded(true);
                Log.d(TAG, "Set up app bar for Contact page");
                return true;
            case PAGE_TEACHERS:
                collapsingToolbarLayout.setTitle(getString(R.string.drawer_teachers));
                //TODO: Make banner image for Teachers page
                appbarImage.setVisibility(View.INVISIBLE);
                if (!isLarge){
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
                appBarLayout.setExpanded(true);
                Log.d(TAG, "Set up app bar for Teachers page");
                return true;
            case PAGE_SETTINGS:
                collapsingToolbarLayout.setTitle(getString(R.string.drawer_settings));
                appbarImage.setVisibility(View.INVISIBLE);
                if (isLarge){
                    Log.d(TAG, "Is tablet, not collapsing app bar");
                    appBarLayout.setExpanded(true);
                } else{
                    if (recreated){
                        appBarLayout.setExpanded(true);
                    } else{
                        appBarLayout.setExpanded(false);
                    }
                }
                if (!isLarge){
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
                Log.d(TAG, "Set up app bar for Settings page");
                return true;
            case PAGE_RESOURCES:
                collapsingToolbarLayout.setTitle(getString(R.string.drawer_resources));
                appbarImage.setVisibility(View.INVISIBLE);
                if (!isLarge){
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
                appBarLayout.setExpanded(true);
                Log.d(TAG, "Set up app bar for Resources page");
                return true;
            case PAGE_DEBUG:
                collapsingToolbarLayout.setTitle(getString(R.string.drawer_debug));
                appbarImage.setVisibility(View.INVISIBLE);
                if (!isLarge){
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
                appBarLayout.setExpanded(true);
                Log.d(TAG, "Set up app bar for Debug page");
                return true;
            case PAGE_NEWSLETTER:
                collapsingToolbarLayout.setTitle(getString(R.string.drawer_newsletter));
                appbarImage.setVisibility(View.INVISIBLE);
                params.setScrollFlags(0);
                appBarLayout.setExpanded(true);
                Log.d(TAG, "Set up app bar for Newsletter page");
                return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //save navigation page for setting up FAB again
        savedInstanceState.putInt("navigation_page", navigationPage);
        savedInstanceState.putInt("previous_page", previousPage);
        //save whether app bar is expanded, so I can collapse it again if needed
        //savedInstanceState.putBoolean("appbar_expanded", isAppbarFullyExpanded());
        //workaround for collapsed title being in the wrong place after rotating
        //appBarLayout.setExpanded(true, false);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        //restore navigation page to set up FAB with
        navigationPage = savedInstanceState.getInt("navigation_page");
        previousPage = savedInstanceState.getInt("previous_page");
        /*if (navigationPage != PAGE_SETTINGS){
            appBarLayout.setExpanded(true, false);
        }*/
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
        setupAppbarForPage(navigationPage, true);

        /*if (navigationPage == PAGE_SETTINGS){
            *//*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    appBarLayout.setExpanded(false, false);
                }
            }, 100);*//*
            fab.hide();
        }*/
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
            /*case R.id.button_debug_announcement:
                //testAnnouncementNotification();
                //MyFirebaseMessagingService.
                break;
            case R.id.button_debug_newsletter:
                testNewsletterNotification();
                break;*/
            case R.id.button_debug_subscribe:
                FirebaseMessaging.getInstance().subscribeToTopic("debug");
                Toast.makeText(this, "Subscribed to debug notifications", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_debug_unsubscribe:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("debug");
                Toast.makeText(this, "Unsubscribed from debug notifications", Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.button_debug_httppost:
                Log.d(TAG, "Trying HTTP POST");
                try {
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");
                    //client.setRequestMode("POST");
                    //client.setRequestProperty(“Key”,”Value”);
                    client.addRequestProperty("");
                    client.setDoOutput(true);
                } catch (Exception e){
                    e.printStackTrace();
                }*/
        }
    }
    /*private void testAnnouncementNotification(){
        //notification settings intent
        Intent settingsIntent = new Intent(this, MainActivity.class);
        settingsIntent.putExtra("navigation_page", PAGE_SETTINGS);
        PendingIntent settingsPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        settingsIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //notification content intent
        Intent contentIntent = new Intent(this, MainActivity.class);
        //settingsIntent.putExtra("navigation_page", PAGE_SETTINGS);
        PendingIntent contentPendingIntent =
                PendingIntent.getActivity(
                        this,
                        1,
                        contentIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //notification
        Notification announcementNotif = PSANotifications.generateAnnouncement(this,
                "No homework",
                contentPendingIntent,
                settingsPendingIntent);
        //notification manager
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(PSANotifications.NOTIFICATION_ID_ANNOUNCEMENT,
                announcementNotif);
    }*/
    /*private void testNewsletterNotification(){
        //notification settings intent
        Intent settingsIntent = new Intent(this, MainActivity.class);
        settingsIntent.putExtra("navigation_page", PAGE_SETTINGS);
        PendingIntent settingsPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        settingsIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //notification content intent
        Intent contentIntent = new Intent(this, MainActivity.class);
        //settingsIntent.putExtra("navigation_page", PAGE_SETTINGS);
        PendingIntent contentPendingIntent =
                PendingIntent.getActivity(
                        this,
                        1,
                        contentIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //notification
        Notification newsletterNotif = PSANotifications.generateNewsletter(this,
                "No homework",
                contentPendingIntent,
                contentPendingIntent,
                settingsPendingIntent);
        //notification manager
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(PSANotifications.NOTIFICATION_ID_NEWSLETTER,
                newsletterNotif);
    }*/

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
