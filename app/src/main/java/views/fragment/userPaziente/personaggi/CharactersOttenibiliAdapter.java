package views.fragment.userPaziente.personaggi;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import views.dialog.InfoDialog;
import views.dialog.RequestConfirmDialog;
import models.domain.profili.personaggio.Personaggio;
import viewsModels.pazienteViewsModels.controller.CharactersController;


public class CharactersOttenibiliAdapter extends RecyclerView.Adapter<CharactersOttenibiliAdapter.ViewHolder> {

    private Context context;
    
    private List<Personaggio> listCharactersToBuy;
    
    private CharactersSbloccatiAdapter charactersUnlockedAdapter;
    
    private NestedScrollView mainNestedScrollView;
    
    private CharactersController charactersController;

    
    public CharactersOttenibiliAdapter(Context context, List<Personaggio> listCharactersToBuy,
                                       CharactersSbloccatiAdapter charactersUnlockedAdapter,
                                       NestedScrollView mainNestedScrollView,
                                       CharactersController charactersController) {
        this.context = context;
        this.listCharactersToBuy = listCharactersToBuy;
        this.charactersUnlockedAdapter = charactersUnlockedAdapter;
        this.mainNestedScrollView = mainNestedScrollView;
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
        
        Personaggio character = listCharactersToBuy.get(position);
        String idCharacter = character.getIdPersonaggio();
        String urlCharacter = character.getTexturePersonaggio();
        String nameCharacter = character.getNomePersonaggio();
        
        int unlockingCostCharacter = character.getCostoSbloccoPersonaggio();
        int costCharacter = character.getCostoSbloccoPersonaggio();

        holder.nameCharacter.setText(nameCharacter);
        Glide.with(context).asBitmap().apply(new RequestOptions().
                override(150, 150)).load(urlCharacter).into(holder.character);
        holder.costCharacter.setText(String.valueOf(costCharacter));

        holder.buyCharacter.setOnClickListener(v ->
                purchaseCharacter(unlockingCostCharacter, character, idCharacter));
    }

    private RequestConfirmDialog setRequestPurchase() {
        RequestConfirmDialog requestConfirmDialog = new RequestConfirmDialog(context,
                context.getString(R.string.acquistoPersonaggioTitle),
                context.getString(R.string.acquistoPersonaggioDescription));
        requestConfirmDialog.setOnCancelButtonClickListener(() -> {});
        requestConfirmDialog.show();

        return requestConfirmDialog;
    }

    private void refreshCharacters(Personaggio character){
        listCharactersToBuy.remove(character);
        charactersUnlockedAdapter.addUnlockedCharacter(character);
    }

    private Animator getAnimator(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mainNestedScrollView.getScrollY(), 0);
        valueAnimator.setDuration(1250);
        valueAnimator.addUpdateListener(animation -> {
            int value = (Integer) animation.getAnimatedValue();
            mainNestedScrollView.scrollTo(0, value);
        });
        return valueAnimator;
    }

    private void purchaseCharacter(int unlockingCostCharacter, Personaggio character, String idCharacter) {

        // controlla se il paziente ha monete suffiicenti per l'acquisto
        if (charactersController.isSufficientCoins(unlockingCostCharacter)) {

            setRequestPurchase().setOnConfirmButtonClickListener(() -> {
                charactersController.updateSelectedCharacter(idCharacter);
                charactersController.updateCoinsPaziente(unlockingCostCharacter);

                refreshCharacters(character);
                getAnimator().start();
                notifyDataSetChanged();
            });
        } else {

            // dialog che mostra un messaggio di valuta insufficiente per l'acquisto
            showInfoDialog();
        }
    }

    @Override
    public int getItemCount() {
        return listCharactersToBuy.size();
    }

    private void showInfoDialog(){
        InfoDialog infoDialog = new InfoDialog(context,
                context.getString(R.string.valutaInsufficiente),
                context.getString(R.string.infoOk));
        infoDialog.setOnConfirmButtonClickListener(() -> {});
        infoDialog.show();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView character;

        private TextView nameCharacter;

        private LinearLayout buyCharacter;

        private LinearLayout selectedCharacter;

        private TextView costCharacter;

        private Button buttonObtainCharacter;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            character = itemView.findViewById(R.id.character);
            nameCharacter = itemView.findViewById(R.id.nameCharacter);

            selectedCharacter = itemView.findViewById(R.id.selectedCharacter);
            buttonObtainCharacter = itemView.findViewById(R.id.buttonObtainCharacter);

            buyCharacter = itemView.findViewById(R.id.buyCharacter);
            costCharacter = itemView.findViewById(R.id.costCharacter);

            selectedCharacter.setVisibility(View.GONE);
            buttonObtainCharacter.setVisibility(View.GONE);
        }
    }

}
