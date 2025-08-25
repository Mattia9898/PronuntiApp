package views.fragment.userPaziente.personaggi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Collections;
import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.personaggio.Personaggio;

import viewsModels.pazienteViewsModels.controller.PersonaggiController;

public class PersonaggiSbloccatiAdapter extends RecyclerView.Adapter<PersonaggiSbloccatiAdapter.ViewHolder> {

    private Context context;
    private List<Personaggio> personaggiSbloccati;
    private PersonaggiController mController;

    public PersonaggiSbloccatiAdapter(Context context, List<Personaggio> personaggiSbloccati, PersonaggiController personaggiController) {
        this.context = context;
        this.personaggiSbloccati = personaggiSbloccati;
        this.mController = personaggiController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_personaggio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Personaggio personaggio = personaggiSbloccati.get(position);
        String urlPersonaggio = personaggio.getTexturePersonaggio();
        String nomePersonaggio = personaggio.getNomePersonaggio();
        String idPersonaggio = personaggio.getIdPersonaggio();

        if (position == 0) {
            holder.buttonPossiediPersonaggio.setVisibility(View.GONE);
            holder.llPersonaggioSelezionato.setVisibility(View.VISIBLE);
        } else {
            holder.llPersonaggioSelezionato.setVisibility(View.GONE);
            holder.buttonPossiediPersonaggio.setVisibility(View.VISIBLE);
        }

        holder.textViewNomePersonaggio.setText(nomePersonaggio);
        Glide.with(context).asBitmap().apply(new RequestOptions().override(150, 150)).load(urlPersonaggio).into(holder.imageViewPersonaggio);

        holder.buttonPossiediPersonaggio.setOnClickListener(v -> {
            refreshPersonaggioSelezionato(position);
            mController.updateSelezionePersonaggio(idPersonaggio);
        });
    }

    private void refreshPersonaggioSelezionato(int position) {
        Collections.swap(personaggiSbloccati, 0, position);
        notifyDataSetChanged();
    }

    public void addPersonaggioSbloccato(Personaggio personaggio){
        personaggiSbloccati.add(0,personaggio);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return personaggiSbloccati.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPersonaggio;
        private TextView textViewNomePersonaggio;
        private LinearLayout llAcquistaPersonaggio; // DA NON VISUALIZZARE QUI
        private LinearLayout llPersonaggioSelezionato;
        private Button buttonPossiediPersonaggio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPersonaggio = itemView.findViewById(R.id.character);
            textViewNomePersonaggio = itemView.findViewById(R.id.textViewNomePersonaggio);

            llAcquistaPersonaggio = itemView.findViewById(R.id.llAcquistaPersonaggio);
            llAcquistaPersonaggio.setVisibility(View.GONE);

            llPersonaggioSelezionato = itemView.findViewById(R.id.llPersonaggioSelezionato);
            buttonPossiediPersonaggio  = itemView.findViewById(R.id.buttonPossiediPersonaggio);
        }
    }

}
