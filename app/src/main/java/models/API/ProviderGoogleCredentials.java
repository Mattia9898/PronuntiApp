package models.API;


import com.google.auth.oauth2.GoogleCredentials;

import java.io.InputStream;

import android.util.Log;

import java.io.IOException;

import android.content.Context;

public final class ProviderGoogleCredentials {

    private static ProviderGoogleCredentials providerGoogleCredentials = null;

    private static GoogleCredentials googleCredentials = null;

    private static final String GOOGLE_CREDENTIALS_FILE_NAME = "google-cloud-credentials.json";


    private ProviderGoogleCredentials(Context context) {

        try {
            InputStream inputStream = context.getAssets().open(GOOGLE_CREDENTIALS_FILE_NAME);
            if (inputStream == null) {
                Log.e("GoogleCredentialsProvider", "Credentials file not found in assets: " + GOOGLE_CREDENTIALS_FILE_NAME);
                return;
            }
            googleCredentials = GoogleCredentials.fromStream(inputStream);
        } catch (IOException e) {
            Log.e("GoogleCredentialsProvider", "Error reading credentials from file", e);
        }
    }

    public GoogleCredentials getCredentials() {
        return googleCredentials;
    }

    public static ProviderGoogleCredentials getInstance(Context context) {
        if (providerGoogleCredentials == null) {
            providerGoogleCredentials = new ProviderGoogleCredentials(context);
        }
        return providerGoogleCredentials;
    }


}