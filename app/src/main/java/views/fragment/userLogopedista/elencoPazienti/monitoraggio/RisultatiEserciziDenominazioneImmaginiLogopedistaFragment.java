package views.fragment.userLogopedista.elencoPazienti.monitoraggio;

import android.os.Bundle;

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

import models.domain.esercizi.EsercizioCoppiaImmagini;

import models.domain.esercizi.EsercizioDenominazioneImmagini;

import models.domain.profili.Paziente;

import models.utils.audioPlayer.AudioPlayerLink;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class RisultatiEserciziDenominazioneImmaginiLogopedistaFragment extends AbstractNavigazioneFragment {

    private ImageView imageViewCheck;

    private ImageView imageViewWrong;

    private TextView textAiutiUtilizzati;

    private ImageButton playButton;

    private ImageButton pauseButton;

    private ImageView immagineEsercizioDenominazioneImageView;

    private EsercizioDenominazioneImmagini mEsercizioDenominazioneImmagine;

    private int indiceEsercizio;

    private int indiceScenario;

    private int indiceTerapia;

    private String idPaziente;

    private LogopedistaViewsModels mLogopedistaViewModel;

    private AudioPlayerLink audioPlayerLink;

    private LinearLayout linearLayoutRispostaData;

    private ImageView imageViewNonSvoltoEsercizio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_denominazione_immagini, container, false);

        setToolBar(view, getString(R.string.risultatoEsercizio));

        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("indiceEsercizio") && savedInstanceState.containsKey("indiceScenario") && savedInstanceState.containsKey("indiceTerapia")) {
            indiceEsercizio = savedInstanceState.getInt("indiceEsercizio");
            indiceScenario = savedInstanceState.getInt("indiceScenario");
            indiceTerapia = savedInstanceState.getInt("indiceTerapia");
            idPaziente = savedInstanceState.getString("idPaziente");
        } else {
            indiceTerapia = 0;
            indiceEsercizio = 0;
            indiceScenario = 0;
        }

        mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        immagineEsercizioDenominazioneImageView = view.findViewById(R.id.imageViewImmagineEsercizioDenominazione);
        imageViewCheck = view.findViewById(R.id.imageViewCheckEsercizio);
        imageViewWrong = view.findViewById(R.id.imageViewWrongEsercizio);
        imageViewNonSvoltoEsercizio = view.findViewById(R.id.imageViewNonSvoltoEsercizio);
        linearLayoutRispostaData = view.findViewById(R.id.linearLayoutRispostaData);
        textAiutiUtilizzati = view.findViewById(R.id.textAiutiUtilizzati);
        playButton = view.findViewById(R.id.imageButtonAvviaAudioRegistrato);
        pauseButton = view.findViewById(R.id.imageButtonPausaAudioRegistrato);
        pauseButton.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.mEsercizioDenominazioneImmagine = getmEsercizioDenominazioneImmagineFromViewModel();
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
            textAiutiUtilizzati.setText("Aiuti utilizzati: " + mEsercizioDenominazioneImmagine.getRisultatoEsercizio().getCounterAiutiUtilizzati());
        }


        playButton.setOnClickListener(v -> playAudio());
        pauseButton.setOnClickListener(v -> stopAudio());

    }

    private boolean isNonSvolto() {
        return this.mEsercizioDenominazioneImmagine.getRisultatoEsercizio() == null;
    }

    private void playAudio() {
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        this.audioPlayerLink = new AudioPlayerLink(mEsercizioDenominazioneImmagine.getRisultatoEsercizio().getAudioRegistrato());
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

    private EsercizioDenominazioneImmagini getmEsercizioDenominazioneImmagineFromViewModel(){

        for (Paziente pazienti : mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti()) {
            if(pazienti.getIdProfilo().equals(idPaziente)){
                return (EsercizioDenominazioneImmagini) pazienti.getTerapie().get(indiceTerapia).getScenariGioco().get(indiceScenario).getlistEsercizioRealizzabile().get(indiceEsercizio);
            }
        }
        return new EsercizioDenominazioneImmagini(0,0,"","","");
    }




}