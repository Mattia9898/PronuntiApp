package views.fragment.profilo;


import views.fragment.AbstractNavigationFragment;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;


public abstract class AsbtractProfiloFragment extends AbstractNavigationFragment {

    protected TextView usernameProfile;

    protected TextInputEditText name;

    protected TextInputEditText surname;

    protected TextInputEditText email;


    abstract void setData();

    abstract void editProfile();

    abstract void confirmEditProfile();


}

