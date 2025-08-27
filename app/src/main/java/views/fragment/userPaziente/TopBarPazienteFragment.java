package views.fragment.userPaziente;


import it.uniba.dib.pronuntiapp.R;

import com.bumptech.glide.Glide;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class TopBarPazienteFragment extends Fragment {

    private ImageView imagePatient;

    private TextView username;

    private TextView score;

    private TextView coins;

    private PazienteViewsModels mPazienteViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_bar_paziente, container, false);
        this.mPazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        imagePatient = view.findViewById(R.id.imagePatient);
        username = view.findViewById(R.id.username);
        score = view.findViewById(R.id.score);
        coins = view.findViewById(R.id.coins);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mPazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), patient -> {

            String image = mPazienteViewsModels.getTexturePersonaggioSelezionatoLiveData().getValue();
            Glide.with(this).asBitmap().load(image).into(imagePatient);

            username.setText(patient.getUsername());
            score.setText(String.valueOf(patient.getPunteggioTot()));
            coins.setText(String.valueOf(patient.getValuta()));
        });
    }

}

