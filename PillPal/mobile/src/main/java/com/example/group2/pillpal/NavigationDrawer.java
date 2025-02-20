package com.example.group2.pillpal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.stats.Stats;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MonthlyStatsFragment.OnFragmentInteractionListener,
        StatsLaunchFragment.OnFragmentInteractionListener,
        SetRemindersFragment.OnFragmentInteractionListener,
        RefillsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        DailyStatsFragment.OnFragmentInteractionListener,
        WeeklyStatsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nav_name);
        UserInstance u = UserInstance.getInstance();
        nav_user.setText(u.name);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        StatsLaunchFragment frag = new StatsLaunchFragment();
        tx.replace(R.id.main, frag);
        tx.addToBackStack(null);
        tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stats, menu);
        return true;
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_stats) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            StatsLaunchFragment statsFragment = new StatsLaunchFragment();
            tx.replace(R.id.main, statsFragment);
            tx.addToBackStack(null);
            tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            tx.commit();
            setTitle("Statistics");
        } else if (id == R.id.nav_reminders) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            SetRemindersFragment remindersFragment = new SetRemindersFragment();
            tx.replace(R.id.main, remindersFragment);
            tx.addToBackStack(null);
            tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            tx.commit();
            setTitle("Reminders");
        } else if (id == R.id.nav_refills) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            RefillsFragment refillsFragment = new RefillsFragment();
            tx.replace(R.id.main, refillsFragment);
            tx.addToBackStack(null);
            tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            tx.commit();
            setTitle("Refills");
        } else if (id == R.id.nav_settings) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            SettingsFragment settingsFragment = new SettingsFragment();
            tx.replace(R.id.main, settingsFragment);
            tx.addToBackStack(null);
            tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            tx.commit();
            setTitle("Settings");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
