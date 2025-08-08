package views.fragment.autenticazione;

import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import it.uniba.dib.pronuntiapp.R;

import models.autenticazione.Autenticazione;

import models.autenticazione.AutenticazioneSharedPreferences;

import views.activity.AutenticazioneActivity;

import views.dialog.RichiestaConfermaDialog;

public class LogoutFragment extends Fragment {

    private View bottoneLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        bottoneLogout = view.findViewById(R.id.buttonLogout);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottoneLogout.setOnClickListener(v -> richiestaLogout());

    }

    private void richiestaLogout() {
        RichiestaConfermaDialog dialog = new RichiestaConfermaDialog(getContext(),getString(R.string.logoutTitle), getString(R.string.logoutRichiesta));
        dialog.setOnConfirmButtonClickListener(this::eseguiLogout);
        dialog.setOnCancelButtonClickListener(() -> {});
        dialog.show();
    }

    private void eseguiLogout() {
        Autenticazione auth = new Autenticazione();
        AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(getContext());

        auth.logout();
        autenticazioneSharedPreferences.clearCredenziali();

        Intent intent = new Intent(getActivity(), AutenticazioneActivity.class);
        startActivity(intent);
    }

}
