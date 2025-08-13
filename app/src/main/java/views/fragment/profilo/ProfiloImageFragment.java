package views.fragment.profilo;


import it.uniba.dib.pronuntiapp.R;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.ActivityResultLauncher;

import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.util.Log;

import com.bumptech.glide.Glide;


public abstract class ProfiloImageFragment extends AsbtractProfiloFragment {

    protected ActivityResultLauncher<PickVisualMediaRequest> activityResultLauncher;

    protected Button buttonEditProfile;

    protected ImageView imageViewProfile;

    protected ImageView imageViewEditProfile;


    protected void setPickMedia() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
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

        buttonEditProfile.setOnClickListener(v -> editProfile());
    }

    @Override
    void confirmEditProfile() {

        setData();

        imageViewEditProfile.setVisibility(View.GONE);

        buttonEditProfile.setText(getString(R.string.modify_profile));
        buttonEditProfile.setOnClickListener(v -> editProfile());
    }

    protected void pickImage() {
        activityResultLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }


}
