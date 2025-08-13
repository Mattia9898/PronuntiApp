package views.fragment.autenticazione;


import it.uniba.dib.pronuntiapp.R;

import androidx.fragment.app.Fragment;

import views.dialog.RequestConfirmDialog;
import views.activity.AutenticazioneActivity;

import models.autenticazione.AutenticazioneSharedPreferences;
import models.autenticazione.Autenticazione;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.content.Intent;


public class LogoutFragment extends Fragment {

    private View buttonLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        buttonLogout = view.findViewById(R.id.buttonLogout);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        buttonLogout.setOnClickListener(v -> requestLogout());

    }

    private void requestLogout() {

        RequestConfirmDialog requestConfirmDialog = new RequestConfirmDialog(getContext(),getString(R.string.logoutTitle), getString(R.string.logoutRichiesta));
        requestConfirmDialog.setOnConfirmButtonClickListener(this::executeLogout);
        requestConfirmDialog.setOnCancelButtonClickListener(() -> {});
        requestConfirmDialog.show();

    }

    private void executeLogout() {

        Autenticazione autenticazione = new Autenticazione();
        AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(getContext());

        autenticazione.logout();
        autenticazioneSharedPreferences.clearCredentials();

        Intent intent = new Intent(getActivity(), AutenticazioneActivity.class);
        startActivity(intent);
    }

}
