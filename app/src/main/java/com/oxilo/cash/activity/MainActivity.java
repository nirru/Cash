package com.oxilo.cash.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.oxilo.cash.ApplicationController;
import com.oxilo.cash.R;
import com.oxilo.cash.fragement.AddProductFragement;
import com.oxilo.cash.fragement.ProductListFragment;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.util.ActivityUtils;
import com.path.android.jobqueue.JobManager;

public class MainActivity extends AppCompatActivity implements ProductListFragment.OnFragmentInteractionListener,AddProductFragement.OnFragmentInteractionListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root);
        initDrawerItem();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getFragmentManager().getBackStackEntryCount()==0) {
            super.onBackPressed();
        }
        else{
            getFragmentManager().popBackStack();
        }
    }

    /**
     * Initialize all the drawer item here
     * and handling the click event of drawer item
     */
    private void initDrawerItem(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                }
                return false;
            }
        });

        loadDefaultFregament();
    }


    private void loadDefaultFregament(){
        Fragment fragment = ProductListFragment.newInstance("","");
        ActivityUtils.launchFragementWithAnimation(fragment,MainActivity.this);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
