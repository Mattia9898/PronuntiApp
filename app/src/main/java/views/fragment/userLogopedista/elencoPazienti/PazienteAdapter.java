package views.fragment.userLogopedista.elencoPazienti;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;

import android.os.Bundle;

import java.util.List;
import java.util.ArrayList;

import views.fragment.adapter.Navigation;

import models.domain.profili.Paziente;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;


public class PazienteAdapter extends RecyclerView.Adapter<PazienteAdapter.PazienteViewHolder> {

    private List<Paziente> patient;

    private List<Paziente> fullListPatients;

    private CardView lastCardView = null;

    private Navigation navigation;


    public PazienteAdapter(List<Paziente> listPatient, Navigation navigation) {
        this.navigation = navigation;
        if (listPatient == null) {
            this.patient = new ArrayList<>();
            this.fullListPatients = new ArrayList<>();
        } else {
            this.patient = new ArrayList<>(listPatient);
            patient.sort((p1,p2)-> {
                int compareSurname = p1.getCognome().compareTo(p2.getCognome());
                if (compareSurname == 0){
                    return p1.getNome().compareTo(p2.getCognome());
                }else {
                    return compareSurname;
                }
            });

            this.fullListPatients = new ArrayList<>(patient);
        }
    }

    @NonNull
    @Override
    public PazienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paziente_in_lista_logopedista, parent, false);
        return new PazienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PazienteViewHolder holder, int position) {

        Paziente paziente = patient.get(position);

        holder.namePatient.setText(paziente.getNome());
        holder.surnamePatient.setText(paziente.getCognome());
        holder.birthdatePatient.setText(paziente.getDataNascita().toString());
        holder.sexPatient.setText(Character.toString(paziente.getSesso()));

        holder.namePatient.setOnClickListener(v -> selectionPatient(holder));
        holder.surnamePatient.setOnClickListener(v -> selectionPatient(holder));
        holder.birthdatePatient.setOnClickListener(v -> selectionPatient(holder));
        holder.sexPatient.setOnClickListener(v -> selectionPatient(holder));

        holder.cardViewPatient.setOnClickListener(v -> selectionPatient(holder));
    }

    public Filter getFilter() {
        return new PazienteFilter();
    }

    public Paziente getItem(int position) {
        return patient.get(position);
    }

    @Override
    public int getItemCount() {
        return patient.size();
    }


    private void selectionPatient(PazienteViewHolder holder) {

        // onclick method on patient in list patients
        holder.cardViewPatient.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.primaryColorSoft));

        if(lastCardView != null && lastCardView != holder.cardViewPatient){
            lastCardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
        }
        lastCardView = holder.cardViewPatient;

        String idSelectedPatient = patient.get(holder.getAdapterPosition()).getIdProfilo();
        String nameSelectedPatient = patient.get(holder.getAdapterPosition()).getNome();
        String surnameSelectedPatient = patient.get(holder.getAdapterPosition()).getCognome();

        Bundle bundle = new Bundle();

        bundle.putString("idPaziente", idSelectedPatient);
        bundle.putString("namePatient", nameSelectedPatient);
        bundle.putString("surnamePatient", surnameSelectedPatient);
        navigation.navigationId(R.id.action_pazientiFragment_to_schedaPazienteFragment, bundle);

    }

    public static class PazienteViewHolder extends RecyclerView.ViewHolder {

        TextView namePatient;

        TextView surnamePatient;

        TextView birthdatePatient;

        TextView sexPatient;

        CardView cardViewPatient;

        public PazienteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewPatient = itemView.findViewById(R.id.cardViewPatient);
            namePatient = itemView.findViewById(R.id.namePatient);
            surnamePatient = itemView.findViewById(R.id.surnamePatient);
            birthdatePatient = itemView.findViewById(R.id.birthdatePatient);
            sexPatient = itemView.findViewById(R.id.sexPatient);
        }
    }

    private class PazienteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Paziente> filteredPatients = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredPatients.addAll(fullListPatients);
            } else {
                String query = charSequence.toString().toLowerCase();
                for (Paziente paziente : fullListPatients) {
                    String name = paziente.getNome().toLowerCase();
                    String surname = paziente.getCognome().toLowerCase();
                    if (name.contains(query) || surname.contains(query)) {
                        filteredPatients.add(paziente);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredPatients;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            patient.clear();
            patient.addAll((List<Paziente>) filterResults.values);
            notifyDataSetChanged();
        }
    }

}
