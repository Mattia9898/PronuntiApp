package views.fragment.autenticazione;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.FrameLayout;

import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.CompletableFuture;

import it.uniba.dib.pronuntiapp.R;

import models.autenticazione.AutenticazioneSharedPreferences;

import models.domain.profili.Profilo;

import views.fragment.AbstractNavigazioneFragment;

import viewsModels.autenticazioneViewsModels.LoginViewsModels;

import views.activity.AbstractAppActivity;

import views.dialog.InfoDialog;

import views.fragment.CaricamentoFragment;

// classe fondamentale per l'autenticazione rapida dell'app, consentendo agli utenti
// di accedere rapidamente con profili predefiniti
public class AutenticazioneRapidaFragment extends AbstractNavigazioneFragment {

    private static final String EMAIL_LOGOPEDISTA_PREDEFINITO = "default@logopedista.it";

    private static final String PASSWORD_LOGOPEDISTA_PREDEFINITO = "logopedista";

    private static final String EMAIL_GENITORE_PREDEFINITO = "default@genitore.it";

    private static final String PASSWORD_GENITORE_PREDEFINITO = "genitore";

    private static final String EMAIL_PAZIENTE_PREDEFINITO = "default@paziente.it";

    private static final String PASSWORD_PAZIENTE_PREDEFINITO = "paziente";

    private FrameLayout frameLayout;

    private LoginViewsModels mLoginViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_autenticazione_rapida, container, false);
        this.mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewsModels.class);
        this.frameLayout = view.findViewById(R.id.layoutAvvioRapido);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_logopedista).setOnClickListener(v -> {
            loginActivityProfilo(EMAIL_LOGOPEDISTA_PREDEFINITO, PASSWORD_LOGOPEDISTA_PREDEFINITO);
        });

        view.findViewById(R.id.button_genitore).setOnClickListener(v -> {
            loginActivityProfilo(EMAIL_GENITORE_PREDEFINITO, PASSWORD_GENITORE_PREDEFINITO);
        });

        view.findViewById(R.id.button_paziente).setOnClickListener(v -> {
            loginActivityProfilo(EMAIL_PAZIENTE_PREDEFINITO, PASSWORD_PAZIENTE_PREDEFINITO);
        });

    }

    private void loginActivityProfilo(String email, String password) {
        avviaSchermataCaricamento();

        CompletableFuture<Boolean> futureIsLoginCorrect = mLoginViewModel.verificaLogin(email, password);
        futureIsLoginCorrect.thenAccept(isLoginCorrect -> {
            if (!isLoginCorrect) {
                InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.erroreLoginCredenziali), getString(R.string.tastoRiprova));
                infoDialog.show();
                infoDialog.setOnConfirmButtonClickListener(null);
            }
            else {
                AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(requireActivity());
                autenticazioneSharedPreferences.salvaCredenziali(email, password);

                CompletableFuture<Profilo> futureProfilo = mLoginViewModel.login();
                futureProfilo.thenAccept(profilo -> {
                    Log.d("AvvioRapidoFragment.loginActivityProfilo()", "Profilo: " + profilo.toString());

                    getActivity().runOnUiThread(() -> ((AbstractAppActivity) getActivity()).navigationWithProfile(profilo, getActivity()));
                });
            }
        });
    }

    private void avviaSchermataCaricamento() {
        frameLayout.removeAllViews();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutAvvioRapido, new CaricamentoFragment())
                .commit();
    }

}
