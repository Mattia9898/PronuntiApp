package views.fragment.userGenitore.monitoraggio;

import android.content.pm.ActivityInfo;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import it.uniba.dib.pronuntiapp.R;

import models.domain.esercizi.EsercizioDenominazioneImmagini;

import models.utils.audioPlayer.AudioPlayerLink;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class RisultatiEserciziDenominazioneImmaginiGenitoreFragment extends AbstractNavigazioneFragment {

    private ImageView imageViewCheck;

    private ImageView imageViewWrong;

    private ImageView imageViewNonSvoltoEsercizio;

    private TextView textAiutiUtilizzati;

    private ImageButton playButton;

    private ImageButton pauseButton;

    private ImageView immagineEsercizioDenominazioneImageView;

    private EsercizioDenominazioneImmagini mEsercizioDenominazioneImmagine;

    private int indiceEsercizio;

    private int indiceScenario;

    private int indiceTerapia;

    private GenitoreViewsModels mGenitoreViewModel;

    private AudioPlayerLink audioPlayerLink;

    private LinearLayout linearLayoutRispostaData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_denominazione_immagini, container, false);

        setToolBar(view, getString(R.string.risultatoEsercizio));

        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("indiceEsercizio") && savedInstanceState.containsKey("indiceScenario") && savedInstanceState.containsKey("indiceTerapia")) {
            indiceEsercizio = savedInstanceState.getInt("indiceEsercizio");
            indiceScenario = savedInstanceState.getInt("indiceScenario");
            indiceTerapia = savedInstanceState.getInt("indiceTerapia");
        } else {
            indiceTerapia = 0;
            indiceEsercizio = 0;
            indiceScenario = 0;
        }

        immagineEsercizioDenominazioneImageView = view.findViewById(R.id.imageViewImmagineEsercizioDenominazione);
        imageViewCheck = view.findViewById(R.id.imageViewCheckEsercizio);
        imageViewWrong = view.findViewById(R.id.imageViewWrongEsercizio);
        textAiutiUtilizzati = view.findViewById(R.id.textAiutiUtilizzati);
        imageViewNonSvoltoEsercizio = view.findViewById(R.id.imageViewNonSvoltoEsercizio);
        linearLayoutRispostaData = view.findViewById(R.id.linearLayoutRispostaData);
        playButton = view.findViewById(R.id.imageButtonAvviaAudioRegistrato);
        pauseButton = view.findViewById(R.id.imageButtonPausaAudioRegistrato);
        pauseButton.setVisibility(View.GONE);

        mGenitoreViewModel = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.mEsercizioDenominazioneImmagine = getEsercizioDenominazioneFromViewModel(indiceEsercizio, indiceScenario, indiceTerapia);

        Picasso.get().load(mEsercizioDenominazioneImmagine.getImmagineEsercizioDenominazioneImmagini()).into(immagineEsercizioDenominazioneImageView);

        if (isNonSvolto()) {
            imageViewCheck.setVisibility(View.GONE);
            imageViewWrong.setVisibility(View.GONE);
            textAiutiUtilizzati.setVisibility(View.GONE);
            linearLayoutRispostaData.setVisibility(View.INVISIBLE);
            imageViewNonSvoltoEsercizio.setVisibility(View.VISIBLE);
        } else {
            if (isCorrect()) {
                imageViewCheck.setVisibility(View.VISIBLE);
                imageViewWrong.setVisibility(View.GONE);
            } else {
                imageViewCheck.setVisibility(View.GONE);
                imageViewWrong.setVisibility(View.VISIBLE);
            }
            int aiuti = mEsercizioDenominazioneImmagine.getRisultatoEsercizio().getCounterAiutiUtilizzati();
            textAiutiUtilizzati.setText(textAiutiUtilizzati.getText().toString() + " " + aiuti);
        }

        playButton.setOnClickListener(v -> playAudio());
        pauseButton.setOnClickListener(v -> stopAudio());

    }

    private void playAudio() {
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        audioPlayerLink = new AudioPlayerLink(mEsercizioDenominazioneImmagine.getRisultatoEsercizio().getAudioRegistrato());
        audioPlayerLink.playAudio();
    }

    private void stopAudio() {
        playButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

    private boolean isCorrect() {
        return mEsercizioDenominazioneImmagine.getRisultatoEsercizio().isEsitoCorretto();
    }
    private boolean isNonSvolto() {
        return this.mEsercizioDenominazioneImmagine.getRisultatoEsercizio() == null;
    }
    private EsercizioDenominazioneImmagini getEsercizioDenominazioneFromViewModel(int indiceEsercizio, int indiceScenario, int indiceTerapia){
        Log.d("RisultatoDenominazione", ":"+ indiceEsercizio);
        Log.d("RisultatoSequenzaParole", ":"+  mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie().get(indiceTerapia).getListScenariGioco().get(indiceScenario).getlistEsercizioRealizzabile().get(indiceEsercizio).toString());
        return (EsercizioDenominazioneImmagini) mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie().get(indiceTerapia).getListScenariGioco().get(indiceScenario).getlistEsercizioRealizzabile().get(indiceEsercizio);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Blocca l'orientamento in portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Sblocca l'orientamento quando il fragment non è più visibile
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

}