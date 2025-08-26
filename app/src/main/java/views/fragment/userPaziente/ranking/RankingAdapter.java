package views.fragment.userPaziente.ranking;


import it.uniba.dib.pronuntiapp.R;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.PazienteViewHolder> {

    private List<Ranking> listPatientsInRanking;

    private String actualPatient;


    public RankingAdapter(List<Ranking> entriesPazienti, String actualPatient) {
        this.listPatientsInRanking = entriesPazienti;
        this.actualPatient = actualPatient;
    }

    @Override
    public PazienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new PazienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PazienteViewHolder holder, int position) {

        Ranking entryPatient = listPatientsInRanking.get(position);

        if (position == 0) { // prima posizione
            holder.imageCrown.setVisibility(View.VISIBLE);
            holder.imageCrown.setImageResource(R.drawable.corona_oro);
        } else if (position == 1) { // seconda posizione
            holder.imageCrown.setVisibility(View.VISIBLE);
            holder.imageCrown.setImageResource(R.drawable.corona_argento);
        } else if (position == 2) {  // terza posizione
            holder.imageCrown.setVisibility(View.VISIBLE);
            holder.imageCrown.setImageResource(R.drawable.corona_bronzo);
        } else { // dalla quarta in poi non viene visualizzata la corona
            holder.imageCrown.setVisibility(View.INVISIBLE);
        }

        if (actualPatient.equals(entryPatient.getUsername())) {
            holder.rankingPosition.
                    setBackground(holder.itemView.getContext().getDrawable(R.drawable.rettangolo_blu));
            holder.rankingPatient.
                    setBackground(holder.itemView.getContext().getDrawable(R.drawable.rettangolo_blu));

        }

        holder.position.setText(String.valueOf(position + 1)); // +1 perchè parte da 0
        holder.username.setText(entryPatient.getUsername());
        holder.score.setText(String.valueOf(entryPatient.getScore()));

        Picasso.get().load(entryPatient.getImageCharacter()).into(holder.imagePatient);
    }

    @Override
    public int getItemCount() {
        return listPatientsInRanking.size();
    }


    public class PazienteViewHolder extends RecyclerView.ViewHolder {

        public TextView position;

        public TextView score;

        public TextView username;

        public ImageView imagePatient;

        public ImageView imageCrown;

        public LinearLayout rankingPosition, rankingPatient;

        public PazienteViewHolder(View itemView) {

            super(itemView);

            position = itemView.findViewById(R.id.position);

            imagePatient = itemView.findViewById(R.id.imagePatient);
            username = itemView.findViewById(R.id.username);

            score = itemView.findViewById(R.id.score);
            imageCrown = itemView.findViewById(R.id.imageCrown);

            rankingPosition = itemView.findViewById(R.id.rankingPosition);
            rankingPatient = itemView.findViewById(R.id.rankingPatient);
        }
    }

}


