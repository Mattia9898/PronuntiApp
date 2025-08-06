package views.fragment.adapter;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import it.uniba.dib.pronuntiapp.R;
import models.domain.scenariGioco.ScenarioGioco;
import viewsModels.genitoreViewsModels.controller.ModificaDataScenariController;

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.ScenarioViewHolder> {

    private List<ScenarioGioco> listaScenari;

    private Navigation navigation;

    private int idNavToEsercizioDenominazioneImmagine;

    private int idNavToEsercizioCoppiaImmagini;

    private int idNavToEsercizioSequenzaParole;

    private ModificaDataScenariController mController;

    private int indiceTerapia;

    private String idPaziente;

    private int indicePaziente;



    public ScenarioAdapter(List<ScenarioGioco> listaScenari, Navigation navigation, int idNavToEsercizioDenominazioneImmagine, int idNavToEsercizioCoppiaImmagini, int idNavToEsercizioSequenzaParole, ModificaDataScenariController mController, int indiceTerapia, String idPaziente, int indicePaziente) {

        this.listaScenari = listaScenari;
        this.navigation = navigation;
        this.mController = mController;
        this.indiceTerapia = indiceTerapia;
        this.idNavToEsercizioCoppiaImmagini = idNavToEsercizioCoppiaImmagini;
        this.idNavToEsercizioDenominazioneImmagine = idNavToEsercizioDenominazioneImmagine;
        this.idNavToEsercizioSequenzaParole = idNavToEsercizioSequenzaParole;
        this.idPaziente = idPaziente;
        this.indicePaziente = indicePaziente;
    }

    @NonNull
    @Override
    public ScenarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenario, parent, false);
        return new ScenarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScenarioViewHolder holder, int position) {
        ScenarioGioco scenario = listaScenari.get(position);

        holder.textViewGiornoScenario.setText(DateTimeFormatter.ofPattern("dd").format(scenario.getDataInizioScenarioGioco()));
        holder.textViewMeseAnnoScenario.setText(DateTimeFormatter.ofPattern("MMMM yyyy").format(scenario.getDataInizioScenarioGioco()));
        holder.linearLayoutScenarioDel.setOnClickListener(v -> toggleVisibility(holder));

        if(scenario.getDataInizioScenarioGioco().isBefore(LocalDate.now())) {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.hintTextColorDisabled));
            holder.imageViewModificaDataScenario.setVisibility(View.INVISIBLE);
            Log.d("ScenarioAdapter",""+scenario.getDataInizioScenarioGioco().toString());
        }

        else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
            holder.imageViewModificaDataScenario.setVisibility(View.VISIBLE);

            //è concesso modificare la data dello scenario
            holder.linearLayoutModificaDataScenario.setOnClickListener(v -> {
                LocalDate now = LocalDate.now();
                DatePickerDialog datePickerDialog = new DatePickerDialog(holder.itemView.getContext(), (view, year, month, dayOfMonth) -> {
                    LocalDate date = LocalDate.parse(LocalDate.of(year, month+1, dayOfMonth).toString());
                    scenario.setDataInizioScenarioGioco(date);
                    holder.textViewGiornoScenario.setText(DateTimeFormatter.ofPattern("dd").format(scenario.getDataInizioScenarioGioco()));
                    holder.textViewMeseAnnoScenario.setText(DateTimeFormatter.ofPattern("MMMM yyyy").format(scenario.getDataInizioScenarioGioco()));

                    Log.d("ScenarioAdapter",""+scenario.toString());


                    mController.modificaDataScenari(date,indiceTerapia,position,idPaziente,indicePaziente);

                    scenario.setDataInizioScenarioGioco(date);
                    notifyDataSetChanged();
                }, now.getYear(), now.getMonthValue()-1, now.getDayOfMonth());
                datePickerDialog.show();
            });
        }
        RecyclerView recyclerViewEsercizi = holder.recyclerViewEsercizi;
        recyclerViewEsercizi.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        EsercizioAdapter esercizioAdapter = new EsercizioAdapter(scenario.getlistEsercizioRealizzabile(), navigation, idNavToEsercizioDenominazioneImmagine, idNavToEsercizioCoppiaImmagini, idNavToEsercizioSequenzaParole,indiceTerapia,position,idPaziente);
        recyclerViewEsercizi.setAdapter(esercizioAdapter);

    }

    private void toggleVisibility(ScenarioViewHolder holder) {
        if (holder.recyclerViewEsercizi.getVisibility() == View.VISIBLE) {
            holder.recyclerViewEsercizi.setVisibility(View.GONE);
            holder.imageViewArrowDown.setRotation(0);
        } else {
            holder.recyclerViewEsercizi.setVisibility(View.VISIBLE);
            holder.imageViewArrowDown.setRotation(180);
        }
    }

    @Override
    public int getItemCount() {
        return listaScenari.size();
    }

    public class ScenarioViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        private TextView textViewGiornoScenario;

        private TextView textViewMeseAnnoScenario;

        private ImageView imageViewArrowDown;

        private RecyclerView recyclerViewEsercizi;

        private LinearLayout linearLayoutScenarioDel;

        private LinearLayout linearLayoutModificaDataScenario;

        private ImageView imageViewModificaDataScenario;

        public ScenarioViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewScenario);
            textViewGiornoScenario = itemView.findViewById(R.id.textViewGiornoScenario);
            textViewMeseAnnoScenario = itemView.findViewById(R.id.textViewMeseAnnoScenario);
            linearLayoutScenarioDel = itemView.findViewById(R.id.linearLayoutScenarioDel);
            linearLayoutModificaDataScenario = itemView.findViewById(R.id.linearLayoutModificaDataScenario);
            imageViewArrowDown = itemView.findViewById(R.id.imageViewArrowDown);
            imageViewArrowDown.setImageResource(R.drawable.icona_freccia_giu_bianca);
            imageViewModificaDataScenario = itemView.findViewById(R.id.imageViewModificaDataScenario);
            recyclerViewEsercizi = itemView.findViewById(R.id.recyclerViewEsercizi);
            recyclerViewEsercizi.setVisibility(View.GONE);
        }
    }
}
