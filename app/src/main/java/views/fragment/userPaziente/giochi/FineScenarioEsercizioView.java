package views.fragment.userPaziente.giochi;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import it.uniba.dib.pronuntiapp.R;
import models.utils.audioPlayer.AudioPlayerRaw;
import views.fragment.AbstractNavigazioneFragment;

public class FineScenarioEsercizioView extends FrameLayout {

    private FrameLayout frameLayoutFineEsercizio;
    private ImageView imageViewUpCoin;
    private TextView textViewCoins, textViewEsercizioCorretto, textViewEsercizioSbagliato, textViewFineScenario;


    public FineScenarioEsercizioView(Context context) {
        super(context);
        initView(context);
    }

    public FineScenarioEsercizioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_fine_esercizio, this, true);

        textViewFineScenario = view.findViewById(R.id.textViewFineScenario);
        textViewFineScenario.setVisibility(View.GONE);
        frameLayoutFineEsercizio = view.findViewById(R.id.frameLayoutFineEsercizioDenominazioneImmagineFine);
        frameLayoutFineEsercizio.setVisibility(View.GONE);
        imageViewUpCoin = view.findViewById(R.id.imageViewUpCoinFineEsercizio);
        textViewCoins = view.findViewById(R.id.textViewCoinsFineEsercizio);
        textViewEsercizioCorretto = view.findViewById(R.id.textViewEsercizioCorretto);
        textViewEsercizioSbagliato = view.findViewById(R.id.textViewEsercizioSbagliato);
    }

    public void showFineScenario(int coins, ImageView imageView1, ImageView imageView2, ImageView imageView3) {

        AudioPlayerRaw audioPlayerRaw = new AudioPlayerRaw(getContext(), R.raw.coins_sound);
        audioPlayerRaw.playAudio();

        frameLayoutFineEsercizio.setVisibility(View.VISIBLE);
        imageViewUpCoin.setVisibility(View.VISIBLE);
        textViewFineScenario.setVisibility(View.VISIBLE);

        animazioneIncrementoValutaFineScenario(coins, imageView1, imageView2, imageView3);
    }

    public void setEsercizioCorretto(int coins, int idNagiation, AbstractNavigazioneFragment fragment, Bundle bundle) {

        AudioPlayerRaw audioPlayerRaw = new AudioPlayerRaw(getContext(), R.raw.correct_sound);
        audioPlayerRaw.playAudio();

        frameLayoutFineEsercizio.setVisibility(View.VISIBLE);
        imageViewUpCoin.setVisibility(View.VISIBLE);
        textViewEsercizioCorretto.setVisibility(View.VISIBLE);

        animazioneIncrementoValuta(coins, idNagiation, fragment, bundle);
    }

    public void setEsercizioSbagliato(int coins, int idNagiation, AbstractNavigazioneFragment fragment, Bundle bundle) {

        AudioPlayerRaw audioPlayerRaw = new AudioPlayerRaw(getContext(), R.raw.error_sound);
        audioPlayerRaw.playAudio();

        frameLayoutFineEsercizio.setVisibility(View.VISIBLE);
        imageViewUpCoin.setVisibility(View.VISIBLE);
        textViewEsercizioSbagliato.setVisibility(View.VISIBLE);

        animazioneIncrementoValuta(coins, idNagiation, fragment, bundle);
    }

    private void animazioneIncrementoValuta(int targetCoins, int idNagiation, AbstractNavigazioneFragment fragment, Bundle bundle) {

        ValueAnimator animator = ValueAnimator.ofInt(0, targetCoins);
        animator.setDuration(4000);
        animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            textViewCoins.setText(String.valueOf(animatedValue));
        });
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(@NonNull Animator animation) {}

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                new Handler().postDelayed(()->{fragment.navigateTo(idNagiation, bundle);},1000);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {}

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {}
        });

    }

    private void animazioneIncrementoValutaFineScenario(int targetCoins, ImageView imageView1, ImageView imageView2, ImageView imageView3) {

        ValueAnimator animator = ValueAnimator.ofInt(0, targetCoins);
        animator.setDuration(4000);
        animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            textViewCoins.setText(String.valueOf(animatedValue));
        });

        animator.start();

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(@NonNull Animator animation) {}

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                new Handler().postDelayed(()->{
                    frameLayoutFineEsercizio.setVisibility(View.GONE);
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                },1000);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {}

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {}
        });

    }

}
