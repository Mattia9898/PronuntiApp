package models.API.SpeechToTextAPI;


import com.google.auth.oauth2.GoogleCredentials;

import java.io.InputStream;

import android.util.Log;

import java.io.IOException;

import android.content.Context;

public final class ProviderCredenzialiGoogle {

    private static ProviderCredenzialiGoogle providerCredenzialiGoogle = null;

    private static GoogleCredentials credentials = null;

    private static final String GOOGLE_CREDENTIALS_FILE_NAME = "google-cloud-credentials.json";


    private ProviderCredenzialiGoogle(Context context) {

        try {
            InputStream inputStream = context.getAssets().open(GOOGLE_CREDENTIALS_FILE_NAME);
            if (inputStream == null) {
                Log.e("GoogleCredentialsProvider", "Credentials file not found in assets: " + GOOGLE_CREDENTIALS_FILE_NAME);
                return;
            }
            credentials = GoogleCredentials.fromStream(inputStream);
        } catch (IOException e) {
            Log.e("GoogleCredentialsProvider", "Error reading credentials from file", e);
        }
    }

    public GoogleCredentials getCredentials() {
        return credentials;
    }

    public static ProviderCredenzialiGoogle getInstance(Context context) {
        if (providerCredenzialiGoogle == null) {
            providerCredenzialiGoogle = new ProviderCredenzialiGoogle(context);
        }
        return providerCredenzialiGoogle;
    }


}