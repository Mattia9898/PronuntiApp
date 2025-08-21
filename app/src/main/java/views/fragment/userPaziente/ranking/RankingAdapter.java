package views.fragment.userPaziente.ranking;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.PazienteViewHolder> {

    private List<Ranking> entriesPazienti;

    private String pazienteAttuale;

    public RankingAdapter(List<Ranking> entriesPazienti, String pazienteAttuale) {
        this.entriesPazienti = entriesPazienti;
        this.pazienteAttuale = pazienteAttuale;
    }

    @Override
    public PazienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classifica, parent, false);
        return new PazienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PazienteViewHolder holder, int position) {

        Ranking entryPaziente = entriesPazienti.get(position);

        if (position == 0) {
            holder.imageViewCorona.setVisibility(View.VISIBLE);
            holder.imageViewCorona.setImageResource(R.drawable.corona_oro);
        } else if (position == 1) {
            holder.imageViewCorona.setVisibility(View.VISIBLE);
            holder.imageViewCorona.setImageResource(R.drawable.corona_argento);
        } else if (position == 2) {
            holder.imageViewCorona.setVisibility(View.VISIBLE);
            holder.imageViewCorona.setImageResource(R.drawable.corona_bronzo);
        } else {
            holder.imageViewCorona.setVisibility(View.INVISIBLE);
        }

        if (pazienteAttuale.equals(entryPaziente.getUsername())) {
            holder.linearLayoutClassificaPosizione.
                    setBackground(holder.itemView.getContext().getDrawable(R.drawable.rettangolo_blu));
            holder.linearLayoutClassificaPaziente.
                    setBackground(holder.itemView.getContext().getDrawable(R.drawable.rettangolo_blu));

        }

        holder.textViewPosizione.setText(String.valueOf(position + 1));
        holder.textViewUsernamePaziente.setText(entryPaziente.getUsername());
        holder.textViewPunteggio.setText(String.valueOf(entryPaziente.getPunteggio()));
        Picasso.get().load(entryPaziente.getImmaginePersonaggio()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return entriesPazienti.size();
    }


    public class PazienteViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewPosizione;

        public ImageView imageView;

        public TextView textViewUsernamePaziente;

        public TextView textViewPunteggio;

        public ImageView imageViewCorona;

        public LinearLayout linearLayoutClassificaPosizione, linearLayoutClassificaPaziente;

        public PazienteViewHolder(View itemView) {

            super(itemView);

            textViewPosizione = itemView.findViewById(R.id.textViewPosizione);
            imageView = itemView.findViewById(R.id.imageViewPaziente);
            textViewUsernamePaziente = itemView.findViewById(R.id.textViewUsernamePaziente);
            textViewPunteggio = itemView.findViewById(R.id.textViewPunteggio);
            imageViewCorona = itemView.findViewById(R.id.imageViewCorona);
            linearLayoutClassificaPosizione = itemView.findViewById(R.id.linearLayoutPosizione);
            linearLayoutClassificaPaziente = itemView.findViewById(R.id.linearLayoutClassificaPaziente);
        }
    }

}


