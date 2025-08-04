package views.fragment.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;
import models.domain.esercizi.EsercizioCoppiaImmagini;
import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.esercizi.EsercizioSequenzaParole;

public class EsercizioAdapter extends RecyclerView.Adapter<EsercizioAdapter.EsercizioViewHolder> {

    private List<EsercizioRealizzabile> listaEsercizi;

    private Navigation navigation;

    private int idNavToEsercizioDenominazioneImmagine;

    private int idNavToEsercizioCoppiaImmagini;

    private int idNavToEsercizioSequenzaParole;

    private int indiceTerapia;

    private int indiceScenario;

    private String idPaziente = String.valueOf(0);

    public EsercizioAdapter(List<EsercizioRealizzabile> listaEsercizi, Navigation navigation, int idNavToEsercizioDenominazioneImmagine, int idNavToEsercizioCoppiaImmagini, int idNavToEsercizioSequenzaParole, int indiceTerapia, int indiceScenario) {
        this.listaEsercizi = listaEsercizi;
        this.navigation = navigation;
        this.idNavToEsercizioDenominazioneImmagine = idNavToEsercizioDenominazioneImmagine;
        this.idNavToEsercizioCoppiaImmagini = idNavToEsercizioCoppiaImmagini;
        this.idNavToEsercizioSequenzaParole = idNavToEsercizioSequenzaParole;
        Log.d("EsercizioAdapterCostruttore","Ex"+idNavToEsercizioDenominazioneImmagine);

        this.indiceScenario = indiceScenario;
        this.indiceTerapia = indiceTerapia;
    }

    public EsercizioAdapter(List<EsercizioRealizzabile> listaEsercizi, Navigation navigation, int idNavToEsercizioDenominazioneImmagine, int idNavToEsercizioCoppiaImmagini, int idNavToEsercizioSequenzaParole, int indiceTerapia, int indiceScenario, String idPaziente) {
        this.listaEsercizi = listaEsercizi;
        this.navigation = navigation;
        this.idNavToEsercizioDenominazioneImmagine = idNavToEsercizioDenominazioneImmagine;
        this.idNavToEsercizioCoppiaImmagini = idNavToEsercizioCoppiaImmagini;
        this.idNavToEsercizioSequenzaParole = idNavToEsercizioSequenzaParole;
        Log.d("EsercizioAdapterCostruttore","Ex"+idNavToEsercizioDenominazioneImmagine);

        this.idPaziente = idPaziente;
        this.indiceScenario = indiceScenario;
        this.indiceTerapia = indiceTerapia;
    }

    @NonNull
    @Override
    public EsercizioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_esercizio, parent, false);
        return new EsercizioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EsercizioViewHolder holder, int position) {
        EsercizioRealizzabile esercizio = listaEsercizi.get(position);

        // Imposta il numero dell'esercizio
        holder.textViewNumeroEsercizio.setText(position+1 +"Â°");

        if(esercizio instanceof EsercizioDenominazioneImmagini) {
            holder.textViewTipoEsercizio.setText("Denominazione immagine");
        }
        else if(esercizio instanceof EsercizioCoppiaImmagini) {
            holder.textViewTipoEsercizio.setText("Coppia immagini");
        }
        else if(esercizio instanceof EsercizioSequenzaParole) {
            holder.textViewTipoEsercizio.setText("Sequenza parole");
        }

        // Gestisci le icone di stato in base allo stato dell'esercizio
        if (esercizio.getRisultatoEsercizio()==null) {
            holder.imageViewCheckEsercizio.setVisibility(View.GONE);
            holder.imageViewWrongEsercizio.setVisibility(View.GONE);
            holder.imageViewNonEseguito.setVisibility(View.VISIBLE);
            holder.imageViewSeeMoreEsercizio.setVisibility(View.VISIBLE);
            //non svolto mostra solo l'esercizio
            setUpOnClickCardView(holder, esercizio, position);

        } else if (esercizio.getRisultatoEsercizio().isEsitoCorretto()) {
            holder.imageViewCheckEsercizio.setVisibility(View.VISIBLE);
            holder.imageViewWrongEsercizio.setVisibility(View.GONE);
            holder.imageViewNonEseguito.setVisibility(View.GONE);
            setUpOnClickCardView(holder, esercizio, position);

        } else {
            holder.imageViewCheckEsercizio.setVisibility(View.GONE);
            holder.imageViewWrongEsercizio.setVisibility(View.VISIBLE);
            holder.imageViewNonEseguito.setVisibility(View.GONE);
            setUpOnClickCardView(holder, esercizio, position);
        }


    }

    private void setUpOnClickCardView(EsercizioViewHolder holder, EsercizioRealizzabile esercizio, int position) {
        holder.itemView.setOnClickListener(v->{
            if(esercizio instanceof EsercizioDenominazioneImmagini) {
                Log.d("EsercizioAdapter",""+idNavToEsercizioDenominazioneImmagine);
                navigateToEsercizio(idNavToEsercizioDenominazioneImmagine,position);
            }
            else if(esercizio instanceof EsercizioCoppiaImmagini) {
                navigateToEsercizio(idNavToEsercizioCoppiaImmagini,position);
            }
            else if(esercizio instanceof EsercizioSequenzaParole) {
                navigateToEsercizio(idNavToEsercizioSequenzaParole,position);
            }
        });
    }

    private void navigateToEsercizio(int id, int posizioneInLista) {
        Bundle bundle = new Bundle();
        bundle.putInt("indiceEsercizio",posizioneInLista);
        bundle.putInt("indiceTerapia",indiceTerapia);
        bundle.putInt("indiceScenario",indiceScenario);
        bundle.putString("idPaziente",idPaziente);
        Log.d("EsercizioAdapter", "onBindViewHolder bundle: "+bundle);
        navigation.navigationId(id, bundle);
    }

    @Override
    public int getItemCount() {
        return listaEsercizi.size();
    }

    public class EsercizioViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNumeroEsercizio;
        private TextView textViewTipoEsercizio;
        private ImageView imageViewCheckEsercizio;
        private ImageView imageViewWrongEsercizio;
        private ImageView imageViewNonEseguito;
        private ImageView imageViewSeeMoreEsercizio;
        private RelativeLayout relativeLayoutEsercizio;

        public EsercizioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumeroEsercizio = itemView.findViewById(R.id.textViewNumeroEsercizio);
            textViewTipoEsercizio = itemView.findViewById(R.id.textViewTipoEsercizio);
            imageViewCheckEsercizio = itemView.findViewById(R.id.imageViewCheckEsercizio);
            imageViewWrongEsercizio = itemView.findViewById(R.id.imageViewWrongEsercizio);
            imageViewNonEseguito = itemView.findViewById(R.id.imageViewNonEseguito);
            imageViewSeeMoreEsercizio = itemView.findViewById(R.id.imageViewSeeMoreEsercizio);
            relativeLayoutEsercizio = itemView.findViewById(R.id.relativeLayoutEsercizio);
        }
    }

}