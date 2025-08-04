package views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.CompletableFuture;

import it.uniba.dib.pronuntiapp.R;
import models.domain.profili.Genitore;
import models.domain.profili.Logopedista;
import models.domain.profili.Paziente;
import models.domain.profili.Profilo;
import viewsModels.InitialGenitore;
import viewsModels.InitialLogopedista;
import viewsModels.InitialPaziente;
import views.dialog.MissedConnectionDialog;

public abstract class AbstractAppActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    protected NavController navcontroller;

    private Context getThisContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }


    protected void setOnBackPressedCallback(int id) {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            private boolean doubleBackToExit= false;
            @Override
            public void handleOnBackPressed() {
                if (navcontroller.getCurrentDestination().getId() == id && doubleBackToExit) {
                    finishAffinity();
                } else if (navcontroller.getCurrentDestination().getId() == id) {
                    doubleBackToExit = true;
                    Toast.makeText(getApplicationContext(),getString(R.string.closeApp) , Toast.LENGTH_SHORT).show();
                }
                else if (navcontroller.navigateUp()) {
                }
                else {
                    navcontroller.navigate(id);
                }
            }
        });
    }


    private boolean isConnessioneInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isConnessioneInternet()) {
                Log.d("AbstractAppActivity.BroadcastReceiver()", "Connessione assente");

                MissedConnectionDialog dialog = new MissedConnectionDialog(getThisContext());
                dialog.setOnConfermaButtonClickListener(() -> riavviaApplicazione());
                runOnUiThread(dialog::show);
            }
        }
    };

    private void riavviaApplicazione() {
        runOnUiThread(() -> {
            Intent restartIntent = new Intent(getThisContext(), EntryActivity.class);
            startActivity(restartIntent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navcontroller.navigateUp()) {
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }


    public void navigaConProfilo(Profilo profilo, Context context) {
        if (profilo instanceof Logopedista) {
            InitialLogopedista.buildIntentLogopedista((Logopedista) profilo, context).thenAccept(intent -> {
                startActivity(intent);
            });
        } else if (profilo instanceof Genitore) {
            InitialGenitore.buildIntentGenitore((Genitore) profilo, context).thenAccept(intent -> {
                startActivity(intent);
            });
        } else if (profilo instanceof Paziente) {
            InitialPaziente.buildIntentPaziente((Paziente) profilo, context).thenAccept(intent -> {
                startActivity(intent);
            });
        }
    }

}
