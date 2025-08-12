package views.fragment.adapter;


import it.uniba.dib.pronuntiapp.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import models.domain.esercizi.EsercizioSequenzaParole;
import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.esercizi.EsercizioCoppiaImmagini;
import models.domain.esercizi.EsercizioRealizzabile;

import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.ImageView;


public class EsercizioAdapter extends RecyclerView.Adapter<EsercizioAdapter.EsercizioViewHolder> {

    private List<EsercizioRealizzabile> listEsercizioRealizzabile;

    private int idNavigationToEsercizioCoppiaImmagini;

    private int idNavigationToEsercizioSequenzaParole;

    private int idNavigationToEsercizioDenominazioneImmagine;

    private int therapy;

    private int scenario;

    private Navigation navigation;

    private String idPaziente = String.valueOf(0);


    @NonNull
    @Override
    public EsercizioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_esercizio, parent, false);
        return new EsercizioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EsercizioViewHolder holder, int position) {

        EsercizioRealizzabile esercizioRealizzabile = listEsercizioRealizzabile.get(position);
        // Imposta il numero dell'esercizio
        holder.indiceExercise.setText(position+1 +"Â°");

        if(esercizioRealizzabile instanceof EsercizioDenominazioneImmagini) {
            holder.tipoExercise.setText("Denominazione immagine");
        }
        else if(esercizioRealizzabile instanceof EsercizioCoppiaImmagini) {
            holder.tipoExercise.setText("Coppia immagini");
        }
        else if(esercizioRealizzabile instanceof EsercizioSequenzaParole) {
            holder.tipoExercise.setText("Sequenza parole");
        }

        // Gestione delle icone in base allo stato dell'esercizio
        if (esercizioRealizzabile.getRisultatoEsercizio()==null) {
            holder.checkExercise.setVisibility(View.GONE);
            holder.wrongExercise.setVisibility(View.GONE);
            holder.notExecutedExercise.setVisibility(View.VISIBLE);
            holder.iconGoToExercise.setVisibility(View.VISIBLE);

            //non svolto mostra solo l'esercizio
            onClickCardView(holder, esercizioRealizzabile, position);

        } else if (esercizioRealizzabile.getRisultatoEsercizio().isEsitoCorretto()) {
            holder.checkExercise.setVisibility(View.VISIBLE);
            holder.wrongExercise.setVisibility(View.GONE);
            holder.notExecutedExercise.setVisibility(View.GONE);
            onClickCardView(holder, esercizioRealizzabile, position);

        } else {
            holder.checkExercise.setVisibility(View.GONE);
            holder.wrongExercise.setVisibility(View.VISIBLE);
            holder.notExecutedExercise.setVisibility(View.GONE);
            onClickCardView(holder, esercizioRealizzabile, position);
        }

    }


    public EsercizioAdapter(List<EsercizioRealizzabile> listaEsercizioRealizzabile, Navigation navigation,
                            int idNavigationToEsercizioDenominazioneImmagine, int idNavigationToEsercizioCoppiaImmagini,
                            int idNavigationToEsercizioSequenzaParole, int therapy, int scenario, String idPaziente) {

        this.listEsercizioRealizzabile = listaEsercizioRealizzabile;
        this.navigation = navigation;
        this.idNavigationToEsercizioDenominazioneImmagine = idNavigationToEsercizioDenominazioneImmagine;
        this.idNavigationToEsercizioCoppiaImmagini = idNavigationToEsercizioCoppiaImmagini;
        this.idNavigationToEsercizioSequenzaParole = idNavigationToEsercizioSequenzaParole;
        Log.d("EsercizioAdapterCostruttore","Ex"+idNavigationToEsercizioDenominazioneImmagine);
        this.idPaziente = idPaziente;
        this.scenario = scenario;
        this.therapy = therapy;
    }


    private void onClickCardView(EsercizioViewHolder holder, EsercizioRealizzabile esercizioRealizzabile, int position) {

        holder.itemView.setOnClickListener(v->{
            if(esercizioRealizzabile instanceof EsercizioDenominazioneImmagini) {
                Log.d("EsercizioAdapter",""+idNavigationToEsercizioDenominazioneImmagine);
                navigateTo(idNavigationToEsercizioDenominazioneImmagine, position);
            }
            else if(esercizioRealizzabile instanceof EsercizioCoppiaImmagini) {
                navigateTo(idNavigationToEsercizioCoppiaImmagini, position);
            }
            else if(esercizioRealizzabile instanceof EsercizioSequenzaParole) {
                navigateTo(idNavigationToEsercizioSequenzaParole, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listEsercizioRealizzabile.size();
    }

    private void navigateTo(int id, int esercizio) {

        Bundle bundle = new Bundle();

        bundle.putInt("esercizio", esercizio);
        bundle.putInt("terapia", therapy);
        bundle.putInt("scenario", scenario);
        bundle.putString("idPaziente", idPaziente);
        Log.d("EsercizioAdapter", "onBindViewHolder bundle: "+bundle);
        navigation.navigationId(id, bundle);
    }


    public class EsercizioViewHolder extends RecyclerView.ViewHolder {

        private TextView indiceExercise;

        private TextView tipoExercise;

        private ImageView checkExercise;

        private ImageView wrongExercise;

        private ImageView notExecutedExercise;

        private ImageView iconGoToExercise;

        private RelativeLayout relativeLayoutExercise;

        public EsercizioViewHolder(@NonNull View itemView) {
            super(itemView);
            indiceExercise = itemView.findViewById(R.id.indiceExercise);
            tipoExercise = itemView.findViewById(R.id.tipoExercise);
            checkExercise = itemView.findViewById(R.id.checkExercise);
            wrongExercise = itemView.findViewById(R.id.wrongExercise);
            notExecutedExercise = itemView.findViewById(R.id.notExecutedExercise);
            iconGoToExercise = itemView.findViewById(R.id.iconGoToExercise);
            relativeLayoutExercise = itemView.findViewById(R.id.relativeLayoutExercise);
        }
    }

}