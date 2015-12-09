package com.oxilo.cash.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.oxilo.cash.ApplicationController;
import com.oxilo.cash.R;
import com.oxilo.cash.activity.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ericbasendra on 15/11/15.
 */
public class ActivityUtils {

    /**
     * Finish the Activity After showing the Toast
     */
    public static void finishMyCurrentActivityAfterShowingToast(final Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((AppCompatActivity)mContext).finish();
            }
        }, ApplicationController.SHORT_DELAY);
    }


    public static void launchFragementWithAnimation(Fragment secondFragment,Context context){
        Fragment currentFragment = ((AppCompatActivity)context).getFragmentManager().findFragmentById(R.id.main_content);
        FragmentManager fm =  ((AppCompatActivity)context).getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(context, fragmentTransaction, currentFragment, secondFragment, R.id.main_content);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.GLIDE);
        fragmentTransactionExtended.commit();
    }



}
