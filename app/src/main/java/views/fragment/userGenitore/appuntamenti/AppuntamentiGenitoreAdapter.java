package views.fragment.userGenitore.appuntamenti;


import it.uniba.dib.pronuntiapp.R;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;


import models.domain.profili.Appuntamento;


public class AppuntamentiGenitoreAdapter extends RecyclerView.Adapter<AppuntamentiGenitoreAdapter.ViewHolder> {

    private List<Appuntamento> listAppuntamento;


    public AppuntamentiGenitoreAdapter(List<Appuntamento> appuntamenti) {
        this.listAppuntamento = appuntamenti;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dayAppuntamento;

        private TextView monthAppuntamento;

        private TextView placeAppuntamento;

        private TextView timeAppuntamento;

        private CardView cardViewAppuntamentoGenitore;

        public ViewHolder(View itemView) {
            super(itemView);
            cardViewAppuntamentoGenitore = itemView.findViewById(R.id.cardViewAppuntamentoGenitore);
            dayAppuntamento = itemView.findViewById(R.id.dayAppuntamento);
            monthAppuntamento = itemView.findViewById(R.id.monthAppuntamento);
            placeAppuntamento = itemView.findViewById(R.id.placeAppuntamento);
            timeAppuntamento = itemView.findViewById(R.id.timeAppuntamento);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appuntamento_in_genitore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Appuntamento appuntamento = listAppuntamento.get(position);

        LocalDateTime actualDate = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        LocalDateTime appuntamentoDate = LocalDateTime.of(appuntamento.getData(), appuntamento.getOra());

        //disabilita se la data è già passata
        if (appuntamentoDate.isBefore(actualDate) || appuntamentoDate.isEqual(actualDate)){
            holder.cardViewAppuntamentoGenitore.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.hintTextColorDisabled));
        } else {
            holder.cardViewAppuntamentoGenitore.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
        }

        holder.dayAppuntamento.setText(DateTimeFormatter.ofPattern("dd").format(appuntamento.getData()));
        holder.monthAppuntamento.setText(DateTimeFormatter.ofPattern("MMMM yyyy").format(appuntamento.getData()));
        holder.placeAppuntamento.setText(appuntamento.getLuogo());
        holder.timeAppuntamento.setText(DateTimeFormatter.ofPattern("HH:mm").format(appuntamento.getOra()));
    }

    @Override
    public int getItemCount() {
        return listAppuntamento.size();
    }

}
