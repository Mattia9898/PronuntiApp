package views.fragment.userPaziente.giochi.scenari;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import views.fragment.AbstractNavigationFragment;


// classe che carica il fragment di assenza di giochi disponibili
public class NessunGiocoDisponibileFragment extends AbstractNavigationFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nessun_gioco_disponibile,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
