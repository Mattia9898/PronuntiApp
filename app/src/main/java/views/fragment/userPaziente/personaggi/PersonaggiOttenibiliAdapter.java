package views.fragment.userPaziente.personaggi;

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
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;
import models.domain.profili.personaggio.Personaggio;
import viewsModels.pazienteViewsModels.controller.PersonaggiController;
import views.dialog.InfoDialog;
import views.dialog.RichiestaConfermaDialog;

public class PersonaggiOttenibiliAdapter extends RecyclerView.Adapter<PersonaggiOttenibiliAdapter.ViewHolder> {

    private Context context;
    private List<Personaggio> personaggiAcquistabili;
    private PersonaggiSbloccatiAdapter personaggiSbloccatiAdapter;
    private NestedScrollView nestedScrollView;
    private PersonaggiController mController;

    public PersonaggiOttenibiliAdapter(Context context, List<Personaggio> personaggiAcquistabili, PersonaggiSbloccatiAdapter personaggiSbloccatiAdapter, NestedScrollView nestedScrollView, PersonaggiController personaggiController) {
        this.context = context;
        this.personaggiAcquistabili = personaggiAcquistabili;
        this.personaggiSbloccatiAdapter = personaggiSbloccatiAdapter;
        this.nestedScrollView = nestedScrollView;
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
        Personaggio personaggio = personaggiAcquistabili.get(position);

        String idPersonaggio = personaggio.getIdPersonaggio();
        String urlPersonaggio = personaggio.getTexturePersonaggio();
        String nomePersonaggio = personaggio.getNomePersonaggio();
        int costoSbloccoPersonaggio =personaggio.getCostoSbloccoPersonaggio();
        int costoPersonaggio = personaggio.getCostoSbloccoPersonaggio();

        holder.textViewNomePersonaggio.setText(nomePersonaggio);
        Glide.with(context).asBitmap().apply(new RequestOptions().override(150, 150)).load(urlPersonaggio).into(holder.imageViewPersonaggio);
        holder.textViewCostoPersonaggio.setText(String.valueOf(costoPersonaggio));

        holder.llAcquistaPersonaggio.setOnClickListener(v -> acquistoPersonaggio(costoSbloccoPersonaggio, personaggio, idPersonaggio));
    }

    private void acquistoPersonaggio(int costoSbloccoPersonaggio, Personaggio personaggio, String idPersonaggio) {
        if (mController.isValutaSufficiente(costoSbloccoPersonaggio)) {

            setRichiestaAcquisto().setOnConfermaButtonClickListener(() -> {
                mController.updateSelezionePersonaggio(idPersonaggio);
                mController.updateValutaPaziente(costoSbloccoPersonaggio);

                refreshPersonaggi(personaggio);
                getAnimator().start();
                notifyDataSetChanged();
            });
        } else {
            showInfoDialog();
        }
    }

    private void showInfoDialog(){
        InfoDialog infoDialog = new InfoDialog(context,context.getString(R.string.valutaInsufficiente), context.getString(R.string.infoOk));
        infoDialog.setOnConfermaButtonClickListener(() -> {});
        infoDialog.show();
    }

    private RichiestaConfermaDialog setRichiestaAcquisto() {
        RichiestaConfermaDialog richiestaAcquisto = new RichiestaConfermaDialog(context, context.getString(R.string.acquistoPersonaggioTitle), context.getString(R.string.acquistoPersonaggioDescription));
        richiestaAcquisto.setOnAnnullaButtonClickListener(() -> {});
        richiestaAcquisto.show();

        return richiestaAcquisto;
    }

    private void refreshPersonaggi(Personaggio personaggio){
        personaggiAcquistabili.remove(personaggio);
        personaggiSbloccatiAdapter.addPersonaggioSbloccato(personaggio);
    }

    private Animator getAnimator(){
        ValueAnimator animator = ValueAnimator.ofInt(nestedScrollView.getScrollY(), 0);
        animator.setDuration(1250);
        animator.addUpdateListener(animation -> {
            int value = (Integer) animation.getAnimatedValue();
            nestedScrollView.scrollTo(0, value);
        });
        return animator;
    }

    @Override
    public int getItemCount() {
        return personaggiAcquistabili.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPersonaggio;
        private TextView textViewNomePersonaggio;
        private LinearLayout llAcquistaPersonaggio;
        private TextView textViewCostoPersonaggio;
        private LinearLayout llPersonaggioSelezionato; //DA NON VISUALIZZARE QUI
        private Button buttonPossiediPersonaggio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPersonaggio = itemView.findViewById(R.id.imageViewPersonaggio);
            textViewNomePersonaggio = itemView.findViewById(R.id.textViewNomePersonaggio);

            llPersonaggioSelezionato = itemView.findViewById(R.id.llPersonaggioSelezionato);
            buttonPossiediPersonaggio = itemView.findViewById(R.id.buttonPossiediPersonaggio);
            llAcquistaPersonaggio = itemView.findViewById(R.id.llAcquistaPersonaggio);
            textViewCostoPersonaggio = itemView.findViewById(R.id.textViewCostoPersonaggio);

            llPersonaggioSelezionato.setVisibility(View.GONE);
            buttonPossiediPersonaggio.setVisibility(View.GONE);
        }
    }

}
