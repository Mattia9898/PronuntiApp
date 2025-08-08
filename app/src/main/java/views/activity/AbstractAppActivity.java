package views.activity;


import models.domain.profili.Profilo;
import models.domain.profili.Paziente;
import models.domain.profili.Logopedista;
import models.domain.profili.Genitore;
import views.dialog.MissedConnectionDialog;
import viewsModels.InitialPaziente;
import viewsModels.InitialLogopedista;
import viewsModels.InitialGenitore;

import it.uniba.dib.pronuntiapp.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.CompletableFuture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.activity.OnBackPressedCallback;

import android.widget.Toast;
import android.util.Log;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.WindowManager;
import android.graphics.Color;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public abstract class AbstractAppActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    protected NavController navigationController;


    private Context getThisContext() {
        return this;
    }

    private boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isInternetConnection()) {
                Log.d("AbstractAppActivity.BroadcastReceiver()", "Connessione assente");

                MissedConnectionDialog missedConnectionDialog = new MissedConnectionDialog(getThisContext());
                missedConnectionDialog.setOnConfirmButtonClickListener(() -> restartApplication());
                runOnUiThread(missedConnectionDialog::show);
            }
        }
    };

    protected void setOnBackPressedCallback(int id) {

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            private boolean doubleTouchToExit = false;

            @Override
            public void handleOnBackPressed() {
                if (navigationController.getCurrentDestination().getId() == id && doubleTouchToExit) {
                    finishAffinity();
                } else if (navigationController.getCurrentDestination().getId() == id) {
                    doubleTouchToExit = true;
                    Toast.makeText(getApplicationContext(),getString(R.string.closeApp), Toast.LENGTH_SHORT).show();
                }
                else if (navigationController.navigateUp()) {
                }
                else {
                    navigationController.navigate(id);
                }
            }
        });
    }

    public void navigationWithProfile(Profilo profilo, Context context) {
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

    @Override
    public boolean onSupportNavigateUp() {
        if (navigationController.navigateUp()) {
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }

    private void restartApplication() {
        runOnUiThread(() -> {
            Intent intent = new Intent(getThisContext(), EntryActivity.class);
            startActivity(intent);
        });
    }


}
