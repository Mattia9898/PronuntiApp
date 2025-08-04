package views.fragment.userLogopedista.elencoPazienti;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Filter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Paziente;

import views.fragment.adapter.Navigation;

public class PazienteAdapter extends RecyclerView.Adapter<PazienteAdapter.PazienteViewHolder> {

    private List<Paziente> pazientiCopia;

    private List<Paziente> pazientiFull;

    private CardView lastClickedCardView = null;

    private Navigation navigation;

    public PazienteAdapter(List<Paziente> pazienti, Navigation navigation) {
        this.navigation = navigation;
        if (pazienti == null) {
            this.pazientiCopia = new ArrayList<>();
            this.pazientiFull = new ArrayList<>();
        } else {
            this.pazientiCopia = new ArrayList<>(pazienti);
            pazientiCopia.sort((p1,p2)-> {
                int confrontoCognome = p1.getCognome().compareTo(p2.getCognome());
                if(confrontoCognome == 0){
                    return p1.getNome().compareTo(p2.getCognome());
                }else {
                    return confrontoCognome;
                }
            });

            this.pazientiFull = new ArrayList<>(pazientiCopia);
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

        Paziente paziente = pazientiCopia.get(position);

        holder.textViewNomePaziente.setText(paziente.getNome());
        holder.textViewCognomePaziente.setText(paziente.getCognome());
        holder.textViewDataNascitaPaziente.setText(paziente.getDataNascita().toString());
        holder.textViewSessoPaziente.setText(Character.toString(paziente.getSesso()));

        holder.textViewNomePaziente.setOnClickListener(v -> selezionePaziente(holder));
        holder.textViewCognomePaziente.setOnClickListener(v -> selezionePaziente(holder));
        holder.textViewDataNascitaPaziente.setOnClickListener(v -> selezionePaziente(holder));
        holder.textViewSessoPaziente.setOnClickListener(v -> selezionePaziente(holder));
        holder.cardViewPazienteInListaLogopedista.setOnClickListener(v -> selezionePaziente(holder));
    }

    private void selezionePaziente(PazienteViewHolder holder) {

        //metodo di onclick sul paziente nella lista dei pazienti
        holder.cardViewPazienteInListaLogopedista.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.primaryColorSoft));

        if(lastClickedCardView != null && lastClickedCardView != holder.cardViewPazienteInListaLogopedista){
            lastClickedCardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
        }
        lastClickedCardView = holder.cardViewPazienteInListaLogopedista;

        String idPazienteSelezionato = pazientiCopia.get(holder.getAdapterPosition()).getIdProfilo();
        String nomePazienteSelezionato = pazientiCopia.get(holder.getAdapterPosition()).getNome();
        String cognomePazienteSelezionato = pazientiCopia.get(holder.getAdapterPosition()).getCognome();

        Bundle bundle = new Bundle();
        bundle.putString("idPaziente",idPazienteSelezionato);
        bundle.putString("nomePaziente",nomePazienteSelezionato);
        bundle.putString("cognomePaziente",cognomePazienteSelezionato);
        navigation.navigationId(R.id.action_pazientiFragment_to_schedaPazienteFragment, bundle);

    }

    @Override
    public int getItemCount() {
        return pazientiCopia.size();
    }

    public Paziente getItem(int position) {
        return pazientiCopia.get(position);
    }

    public static class PazienteViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNomePaziente;

        TextView textViewCognomePaziente;

        TextView textViewDataNascitaPaziente;

        TextView textViewSessoPaziente;

        CardView cardViewPazienteInListaLogopedista;

        public PazienteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewPazienteInListaLogopedista = itemView.findViewById(R.id.cardViewPazienteInListaLogopedista);
            textViewNomePaziente = itemView.findViewById(R.id.textViewNomePaziente);
            textViewCognomePaziente = itemView.findViewById(R.id.textViewCognomePaziente);
            textViewDataNascitaPaziente = itemView.findViewById(R.id.textViewDataNascitaPaziente);
            textViewSessoPaziente = itemView.findViewById(R.id.textViewSessoPaziente);
        }
    }

    public Filter getFilter() {
        return new PazienteFilter();
    }

    private class PazienteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Paziente> filteredPazienti = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredPazienti.addAll(pazientiFull);
            } else {
                String query = charSequence.toString().toLowerCase();
                for (Paziente paziente : pazientiFull) {
                    String nome = paziente.getNome().toLowerCase();
                    String cognome = paziente.getCognome().toLowerCase();
                    if (nome.contains(query) || cognome.contains(query)) {
                        filteredPazienti.add(paziente);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredPazienti;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            pazientiCopia.clear();
            pazientiCopia.addAll((List<Paziente>) filterResults.values);
            notifyDataSetChanged();
        }
    }

}
