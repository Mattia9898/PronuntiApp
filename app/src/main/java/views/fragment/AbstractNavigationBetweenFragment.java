package views.fragment;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public abstract class AbstractNavigationBetweenFragment extends Fragment {

    protected NavController navigationController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationController = Navigation.findNavController(view);
    }


    // used by other classes for navigation between fragments
    public void navigationTo(int idAction, Bundle bundle) {
        if (getActivity() != null) {
            navigationController.navigate(idAction, bundle);
        }
    }

    // used by other classes for navigation between fragments
    public void navigationTo(int idAction) {
        if (getActivity() != null) {
            navigationController.navigate(idAction);
        }
    }

    // toolbar without title
    public void setToolBarNoTitle(View view){
        Toolbar toolbar = view.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // toolbar with title
    public void setToolBar(View view, String titleToolBar){
        Toolbar toolbar = view.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(titleToolBar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
