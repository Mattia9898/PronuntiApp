package views.fragment.profilo;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;

import it.uniba.dib.pronuntiapp.R;


public abstract class ProfiloConImmagineFragment extends AsbtractProfiloFragment {
    protected ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    protected Button buttonModificaProfilo;
    protected ImageView imageViewProfile;
    protected ImageView imageViewEditProfile;


    protected void setPickMedia() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(imageViewProfile);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        buttonModificaProfilo.setOnClickListener(v -> modificaProfilo());
    }

    protected void pickImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    @Override
    void confermaModificaProfilo() {
        setData();

        imageViewEditProfile.setVisibility(View.GONE);

        buttonModificaProfilo.setText(getString(R.string.modify_profile));
        buttonModificaProfilo.setOnClickListener(v -> modificaProfilo());
    }

}
