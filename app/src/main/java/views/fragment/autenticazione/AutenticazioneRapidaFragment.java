package views.fragment.autenticazione;


import it.uniba.dib.pronuntiapp.R;

import androidx.lifecycle.ViewModelProvider;

import models.autenticazione.AutenticazioneSharedPreferences;
import models.domain.profili.Profilo;
import views.dialog.InfoDialog;
import views.fragment.StartingLoadingFragment;
import views.activity.AbstractAppActivity;
import views.fragment.AbstractNavigationBetweenFragment;

import java.util.concurrent.CompletableFuture;

import viewsModels.autenticazioneViewsModels.LoginViewsModels;

import android.util.Log;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.os.Bundle;


// classe utile per l'autenticazione rapida dell'app, consentendo di accedere con profili di default
public class AutenticazioneRapidaFragment extends AbstractNavigationBetweenFragment {


    private FrameLayout frameLayout;

    private LoginViewsModels loginViewsModels;

    private static final String EMAIL_LOGOPEDISTA_DEFAULT = "test@logopedista.it";

    private static final String PASSWORD_LOGOPEDISTA_DEFAULT = "testlogopedista";

    private static final String EMAIL_GENITORE_DEFAULT = "test@genitore.it";

    private static final String PASSWORD_GENITORE_DEFAULT = "testgenitore";

    private static final String EMAIL_PAZIENTE_DEFAULT = "test@paziente.it";

    private static final String PASSWORD_PAZIENTE_DEFAULT = "testpaziente";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_autenticazione_rapida, container, false);

        this.loginViewsModels = new ViewModelProvider(requireActivity()).get(LoginViewsModels.class);
        this.frameLayout = view.findViewById(R.id.layoutAvvioRapido);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_logopedista).setOnClickListener(v -> {
            loginActivityProfile(EMAIL_LOGOPEDISTA_DEFAULT, PASSWORD_LOGOPEDISTA_DEFAULT);
        });

        view.findViewById(R.id.button_genitore).setOnClickListener(v -> {
            loginActivityProfile(EMAIL_GENITORE_DEFAULT, PASSWORD_GENITORE_DEFAULT);
        });

        view.findViewById(R.id.button_paziente).setOnClickListener(v -> {
            loginActivityProfile(EMAIL_PAZIENTE_DEFAULT, PASSWORD_PAZIENTE_DEFAULT);
        });

    }

    private void loginActivityProfile(String email, String password) {

        startLoadingScreen();
        CompletableFuture<Boolean> loginCompletableFuture = loginViewsModels.verificaLogin(email, password);

        loginCompletableFuture.thenAccept(isLoginCorrect -> {
            if (!isLoginCorrect) {
                InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.erroreLoginCredenziali), getString(R.string.tastoRiprova));
                infoDialog.show();
                infoDialog.setOnConfirmButtonClickListener(null);
            }
            else {
                AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(requireActivity());
                autenticazioneSharedPreferences.saveCredentials(email, password);

                CompletableFuture<Profilo> profileCompletableFuture = loginViewsModels.login();
                profileCompletableFuture.thenAccept(profilo -> {
                    Log.d("AvvioRapidoFragment.loginActivityProfilo()", "Profilo: " + profilo.toString());
                    getActivity().runOnUiThread(() -> ((AbstractAppActivity) getActivity()).navigationWithProfile(profilo, getActivity()));
                });
            }
        });
    }

    private void startLoadingScreen() {

        frameLayout.removeAllViews();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutAvvioRapido, new StartingLoadingFragment())
                .commit();
    }

}
