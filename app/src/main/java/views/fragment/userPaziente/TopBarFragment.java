package views.fragment.userPaziente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

public class TopBarFragment extends Fragment {

    private LinearLayout topBarLayout;

    private ImageView imageViewPaziente;

    private TextView textViewUsernamePaziente;

    private TextView textViewPunteggio;

    private TextView coinsTextView;

    private PazienteViewsModels mPazienteViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_top_bar_paziente, container, false);

        this.mPazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        topBarLayout = view.findViewById(R.id.topBarPaziente);
        imageViewPaziente = view.findViewById(R.id.imageViewPaziente);
        textViewUsernamePaziente = view.findViewById(R.id.textViewUsernamePaziente);
        textViewPunteggio = view.findViewById(R.id.textViewPunteggio);
        coinsTextView = view.findViewById(R.id.coinsTextView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {

            String immagine = mPazienteViewsModels.getTexturePersonaggioSelezionatoLiveData().getValue();
            Glide.with(this).asBitmap().load(immagine).into(imageViewPaziente);

            textViewUsernamePaziente.setText(paziente.getUsername());
            textViewPunteggio.setText(String.valueOf(paziente.getPunteggioTot()));
            coinsTextView.setText(String.valueOf(paziente.getValuta()));
        });
    }

}

