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
import viewsModels.genitoreViewsModels.controller.EditDataSceneryController;

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.ScenarioViewHolder> {

    private List<ScenarioGioco> listScenarioGioco;

    private int idNavigationToEsercizioCoppiaImmagini;

    private int idNavigationToEsercizioSequenzaParole;

    private int idNavigationToEsercizioDenominazioneImmagine;

    private int therapy;

    private EditDataSceneryController editDataSceneryController;

    private Navigation navigation;

    private String idPatient;

    private int indexPatient;


    public ScenarioAdapter(List<ScenarioGioco> listScenarioGioco, Navigation navigation,
                           int idNavigationToEsercizioDenominazioneImmagine, int idNavigationToEsercizioCoppiaImmagini,
                           int idNavigationToEsercizioSequenzaParole, EditDataSceneryController editDataSceneryController,
                           int therapy, String idPatient, int indexPatient) {

        this.listScenarioGioco = listScenarioGioco;
        this.navigation = navigation;
        this.editDataSceneryController = editDataSceneryController;
        this.therapy = therapy;
        this.idNavigationToEsercizioCoppiaImmagini = idNavigationToEsercizioCoppiaImmagini;
        this.idNavigationToEsercizioDenominazioneImmagine = idNavigationToEsercizioDenominazioneImmagine;
        this.idNavigationToEsercizioSequenzaParole = idNavigationToEsercizioSequenzaParole;
        this.idPatient = idPatient;
        this.indexPatient = indexPatient;
    }


    private void toggleVisibility(ScenarioViewHolder scenarioViewHolder) {
        if (scenarioViewHolder.recyclerViewExercise.getVisibility() == View.VISIBLE) {
            scenarioViewHolder.recyclerViewExercise.setVisibility(View.GONE);
            scenarioViewHolder.arrowDown.setRotation(0);
        } else {
            scenarioViewHolder.recyclerViewExercise.setVisibility(View.VISIBLE);
            scenarioViewHolder.arrowDown.setRotation(180);
        }
    }

    @Override
    public int getItemCount() {
        return listScenarioGioco.size();
    }

    @NonNull
    @Override
    public ScenarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenario, parent, false);
        return new ScenarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScenarioViewHolder holder, int position) {

        ScenarioGioco scenarioGioco = listScenarioGioco.get(position);

        holder.dayScenario.setText(DateTimeFormatter.ofPattern("dd").format(scenarioGioco.getDataInizioScenarioGioco()));
        holder.monthYearScenario.setText(DateTimeFormatter.ofPattern("MMMM yyyy").format(scenarioGioco.getDataInizioScenarioGioco()));
        holder.linearLayoutScenarioDel.setOnClickListener(v -> toggleVisibility(holder));

        if(scenarioGioco.getDataInizioScenarioGioco().isBefore(LocalDate.now())) {
            holder.cardViewScenario.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.hintTextColorDisabled));
            holder.editDataScenario.setVisibility(View.INVISIBLE);
            Log.d("ScenarioAdapter","" +scenarioGioco.getDataInizioScenarioGioco().toString());
        }

        else {
            holder.cardViewScenario.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
            holder.editDataScenario.setVisibility(View.VISIBLE);

            //si puù modificare la data dello scenario
            holder.linearLayoutEditDataScenario.setOnClickListener(v -> {
                LocalDate actualDate = LocalDate.now();

                DatePickerDialog datePickerDialog = new DatePickerDialog(holder.itemView.getContext(), (view, year, month, dayOfMonth) -> {
                    LocalDate localDate = LocalDate.parse(LocalDate.of(year, month+1, dayOfMonth).toString());

                    scenarioGioco.setDataInizioScenarioGioco(localDate);
                    holder.dayScenario.setText(DateTimeFormatter.ofPattern("dd").format(scenarioGioco.getDataInizioScenarioGioco()));
                    holder.monthYearScenario.setText(DateTimeFormatter.ofPattern("MMMM yyyy").format(scenarioGioco.getDataInizioScenarioGioco()));

                    Log.d("ScenarioAdapter","" + scenarioGioco.toString());

                    editDataSceneryController.editDataScenery(localDate, therapy, position, idPatient, indexPatient);
                    scenarioGioco.setDataInizioScenarioGioco(localDate);
                    notifyDataSetChanged();
                }, actualDate.getYear(), actualDate.getMonthValue()-1, actualDate.getDayOfMonth());

                datePickerDialog.show();
            });
        }

        RecyclerView recyclerView = holder.recyclerViewExercise;
        recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        EsercizioAdapter esercizioAdapter = new EsercizioAdapter(scenarioGioco.getlistEsercizioRealizzabile(),
                                                                navigation, idNavigationToEsercizioDenominazioneImmagine,
                                                                idNavigationToEsercizioCoppiaImmagini,
                                                                idNavigationToEsercizioSequenzaParole, therapy,
                                                                position, idPatient);
        recyclerView.setAdapter(esercizioAdapter);

    }


    public class ScenarioViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewScenario;

        private TextView dayScenario;

        private TextView monthYearScenario;

        private ImageView arrowDown;

        private RecyclerView recyclerViewExercise;

        private LinearLayout linearLayoutScenarioDel;

        private LinearLayout linearLayoutEditDataScenario;

        private ImageView editDataScenario;

        public ScenarioViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewScenario = itemView.findViewById(R.id.cardViewScenario);
            dayScenario = itemView.findViewById(R.id.dayScenario);
            monthYearScenario = itemView.findViewById(R.id.monthYearScenario);
            linearLayoutScenarioDel = itemView.findViewById(R.id.linearLayoutScenarioDel);
            linearLayoutEditDataScenario = itemView.findViewById(R.id.linearLayoutEditDataScenario);
            arrowDown = itemView.findViewById(R.id.arrowDown);
            arrowDown.setImageResource(R.drawable.icona_freccia_giu_bianca);
            editDataScenario = itemView.findViewById(R.id.editDataScenario);
            recyclerViewExercise = itemView.findViewById(R.id.recyclerViewExercise);
            recyclerViewExercise.setVisibility(View.GONE);
        }
    }

}
