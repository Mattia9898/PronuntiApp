package views.fragment;


import it.uniba.dib.pronuntiapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


// classe per il fragment di caricamento iniziale dell'app
public class StartingLoadingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_starting_loading, container, false);
        return view;
    }

}
