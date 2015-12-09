package com.oxilo.cash.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.oxilo.cash.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView myView = (ImageView)findViewById(R.id.splash);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(
                ObjectAnimator.ofFloat(myView, "rotationX", 0, 360),
                ObjectAnimator.ofFloat(myView, "rotationY", 0, 180),
                ObjectAnimator.ofFloat(myView, "rotation", 0, -90),
                ObjectAnimator.ofFloat(myView, "translationX", 0, 90),
                ObjectAnimator.ofFloat(myView, "translationY", 0, 90),
                ObjectAnimator.ofFloat(myView, "scaleX", 1, 1.5f),
                ObjectAnimator.ofFloat(myView, "scaleY", 1, 0.5f),
                ObjectAnimator.ofFloat(myView, "alpha", 1, 0.25f, 1)
        );
        animSet.setDuration(5000).start();

        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
              Intent i = new Intent(SplashActivity.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }
}
