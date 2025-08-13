package views.fragment.profilo;


import views.fragment.AbstractNavigazioneFragment;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.Fragment;


public abstract class AsbtractProfiloFragment extends AbstractNavigazioneFragment {

    protected TextView usernameProfile;

    protected TextInputEditText name;

    protected TextInputEditText surname;

    protected TextInputEditText email;


    abstract void setData();

    abstract void editProfile();

    abstract void confirmEditProfile();


}

