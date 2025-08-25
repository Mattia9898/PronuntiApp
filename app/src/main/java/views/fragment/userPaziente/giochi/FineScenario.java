package views.fragment.userPaziente.giochi;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.animation.Animator;
import android.animation.ValueAnimator;

import models.utils.audioPlayer.AudioPlayerRaw;
import views.fragment.AbstractNavigationFragment;


public class FineScenario extends FrameLayout {

    private FrameLayout mainFrameLayout;

    private ImageView upCoins;

    private TextView coins, correctExercise, wrongExercise, endScenery;


    public FineScenario(Context context) {
        super(context);
        initializeView(context);
    }

    public FineScenario(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    private void initializeView(Context context) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_fine_scenario, this, true);

        endScenery = view.findViewById(R.id.endScenery);
        endScenery.setVisibility(View.GONE);

        mainFrameLayout = view.findViewById(R.id.mainFrameLayout);
        mainFrameLayout.setVisibility(View.GONE);

        upCoins = view.findViewById(R.id.upCoins);
        coins = view.findViewById(R.id.coins);

        correctExercise = view.findViewById(R.id.correctExercise);
        wrongExercise = view.findViewById(R.id.wrongExercise);
    }

    public void setWrongExercise(int coins, int idNavigation, AbstractNavigationFragment abstractNavigationFragment, Bundle bundle) {

        AudioPlayerRaw audioPlayerRaw = new AudioPlayerRaw(getContext(), R.raw.error_sound);
        audioPlayerRaw.playAudio();

        mainFrameLayout.setVisibility(View.VISIBLE);
        upCoins.setVisibility(View.VISIBLE);
        wrongExercise.setVisibility(View.VISIBLE);

        animationUpCoins(coins, idNavigation, abstractNavigationFragment, bundle);
    }

    public void setCorrectExercise(int coins, int idNavigation, AbstractNavigationFragment abstractNavigationFragment, Bundle bundle) {

        AudioPlayerRaw audioPlayerRaw = new AudioPlayerRaw(getContext(), R.raw.correct_sound);
        audioPlayerRaw.playAudio();

        mainFrameLayout.setVisibility(View.VISIBLE);
        upCoins.setVisibility(View.VISIBLE);
        correctExercise.setVisibility(View.VISIBLE);

        animationUpCoins(coins, idNavigation, abstractNavigationFragment, bundle);
    }

    public void showFineScenario(int coins, ImageView positionFirstGame, ImageView positionSecondGame, ImageView positionThirdGame) {

        AudioPlayerRaw audioPlayerRaw = new AudioPlayerRaw(getContext(), R.raw.coins_sound);
        audioPlayerRaw.playAudio();

        mainFrameLayout.setVisibility(View.VISIBLE);
        upCoins.setVisibility(View.VISIBLE);
        endScenery.setVisibility(View.VISIBLE);

        animationUpCoinsEndScenery(coins, positionFirstGame, positionSecondGame, positionThirdGame);
    }

    private void animationUpCoins(int targetCoins, int idNavigation, AbstractNavigationFragment abstractNavigationFragment, Bundle bundle) {

        ValueAnimator animator = ValueAnimator.ofInt(0, targetCoins);
        animator.setDuration(4000);

        animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            coins.setText(String.valueOf(animatedValue));
        });

        animator.start();

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(@NonNull Animator animation) {}

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                new Handler().postDelayed(()->{abstractNavigationFragment.navigateTo(idNavigation, bundle);},1000);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {}

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {}
        });

    }

    private void animationUpCoinsEndScenery(int targetCoins, ImageView positionFirstGame, ImageView positionSecondGame, ImageView positionThirdGame) {

        ValueAnimator animator = ValueAnimator.ofInt(0, targetCoins);
        animator.setDuration(4000);

        animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            coins.setText(String.valueOf(animatedValue));
        });

        animator.start();

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(@NonNull Animator animation) {}

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                new Handler().postDelayed(()->{
                    mainFrameLayout.setVisibility(View.GONE);
                    positionFirstGame.setVisibility(View.VISIBLE);
                    positionSecondGame.setVisibility(View.VISIBLE);
                    positionThirdGame.setVisibility(View.VISIBLE);
                },1000);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {}

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {}
        });

    }

}
