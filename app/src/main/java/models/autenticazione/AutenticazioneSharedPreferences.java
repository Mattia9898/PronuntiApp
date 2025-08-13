package models.autenticazione;


import android.content.SharedPreferences;

import android.content.Context;

public class AutenticazioneSharedPreferences {

    private static final String USER_CREDENTIALS = "UserCredentials";

    private static final String EMAIL = "email";

    private static final String PASSWORD = "password";

    private SharedPreferences sharedPreferences;


    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public AutenticazioneSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
    }

    public void saveCredentials(String email, String password) {
        SharedPreferences.Editor SharedPreferences = sharedPreferences.edit();
        SharedPreferences.putString(EMAIL, email);
        SharedPreferences.putString(PASSWORD, password);
        SharedPreferences.apply();
    }

    public void clearCredentials() {
        SharedPreferences.Editor SharedPreferences = sharedPreferences.edit();
        SharedPreferences.remove(EMAIL);
        SharedPreferences.remove(PASSWORD);
        SharedPreferences.apply();
    }

}
