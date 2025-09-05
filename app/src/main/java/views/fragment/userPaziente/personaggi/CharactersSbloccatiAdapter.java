package views.fragment.userPaziente.personaggi;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Collections;
import java.util.List;

import models.domain.profili.personaggio.Personaggio;

import viewsModels.pazienteViewsModels.controller.CharactersController;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CharactersSbloccatiAdapter extends RecyclerView.Adapter<CharactersSbloccatiAdapter.ViewHolder> {

    private Context context;

    private List<Personaggio> listUnlockedCharacters;

    private CharactersController charactersController;


    public CharactersSbloccatiAdapter(Context context,
                                      List<Personaggio> listUnlockedCharacters,
                                      CharactersController charactersController) {
        this.context = context;
        this.listUnlockedCharacters = listUnlockedCharacters;
        this.charactersController = charactersController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_personaggio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Personaggio character = listUnlockedCharacters.get(position);
        String urlCharacter = character.getTexturePersonaggio();
        String nameCharacter = character.getNomePersonaggio();
        String idCharacter = character.getIdPersonaggio();

        if (position == 0) {
            holder.buttonObtainCharacter.setVisibility(View.GONE);
            holder.selectedCharacter.setVisibility(View.VISIBLE);
        } else {
            holder.selectedCharacter.setVisibility(View.GONE);
            holder.buttonObtainCharacter.setVisibility(View.VISIBLE);
        }

        holder.nameCharacter.setText(nameCharacter);
        Glide.with(context).asBitmap().apply(new RequestOptions().
                override(150, 150)).load(urlCharacter).into(holder.character);

        holder.buttonObtainCharacter.setOnClickListener(v -> {
            refreshSelectedCharacter(position);
            charactersController.updateSelectedCharacter(idCharacter);
        });
    }

    @Override
    public int getItemCount() {
        return listUnlockedCharacters.size();
    }

    public void addUnlockedCharacter(Personaggio personaggio){
        listUnlockedCharacters.add(0,personaggio);
        notifyDataSetChanged();
    }

    private void refreshSelectedCharacter(int position) {
        Collections.swap(listUnlockedCharacters, 0, position);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView character;

        private TextView nameCharacter;

        private LinearLayout buyCharacter;

        private LinearLayout selectedCharacter;

        private Button buttonObtainCharacter;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            character = itemView.findViewById(R.id.character);
            nameCharacter = itemView.findViewById(R.id.nameCharacter);

            buyCharacter = itemView.findViewById(R.id.buyCharacter);
            buyCharacter.setVisibility(View.GONE);

            selectedCharacter = itemView.findViewById(R.id.selectedCharacter);
            buttonObtainCharacter = itemView.findViewById(R.id.buttonObtainCharacter);
        }
    }

}
