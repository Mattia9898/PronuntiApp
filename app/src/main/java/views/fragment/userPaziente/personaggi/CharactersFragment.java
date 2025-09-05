package views.fragment.userPaziente.personaggi;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import viewsModels.pazienteViewsModels.PazienteViewsModels;
import viewsModels.pazienteViewsModels.controller.CharactersController;

import models.domain.profili.personaggio.Personaggio;

import views.fragment.AbstractNavigationBetweenFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CharactersFragment extends AbstractNavigationBetweenFragment {

    private RecyclerView RecyclerViewCharactersUnlocked;

    private RecyclerView RecyclerViewCharactersToBuy;

    private NestedScrollView nestedScrollView;

    private PazienteViewsModels pazienteViewsModels;

    private CharactersController charactersController;

    private List<String> listStringCharactersUnlocked;

    private List<String> listStringCharactersToBuy;

    private List<Personaggio> listCharactersUnlocked;

    private List<Personaggio> listCharactersToBuy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_characters, container, false);

        this.pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.charactersController = pazienteViewsModels.getPersonaggiController();

        setToolBarNoTitle(view);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        RecyclerViewCharactersUnlocked = view.findViewById(R.id.RecyclerViewCharactersUnlocked);
        RecyclerViewCharactersToBuy = view.findViewById(R.id.RecyclerViewCharactersToBuy);
        RecyclerViewCharactersToBuy.setHasFixedSize(true);

        listStringCharactersUnlocked = new ArrayList<>();
        listStringCharactersToBuy = new ArrayList<>();
        listCharactersUnlocked = new ArrayList<>();
        listCharactersToBuy = new ArrayList<>();

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        RecyclerViewCharactersUnlocked.setItemAnimator(itemAnimator);
        RecyclerViewCharactersToBuy.setItemAnimator(itemAnimator);

        GridLayoutManager layoutManagerUnlocked = new GridLayoutManager(getContext(), 3);
        GridLayoutManager layoutManagerToBuy = new GridLayoutManager(getContext(), 3);
        RecyclerViewCharactersUnlocked.setLayoutManager(layoutManagerUnlocked);
        RecyclerViewCharactersToBuy.setLayoutManager(layoutManagerToBuy);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapters();
    }

    private void setAdapters() {
        Map<String, Integer> mapCharactersPatient = pazienteViewsModels.getPazienteLiveData().getValue().getPersonaggiSbloccati();
        List<Personaggio> listCharacters = pazienteViewsModels.getListaPersonaggiLiveData().getValue();

        setIdLists(mapCharactersPatient);
        listCharactersUnlocked = charactersController.getSortedListCharacters(listCharacters, listStringCharactersToBuy);
        listCharactersToBuy = charactersController.getSortedListCharacters(listCharacters, listStringCharactersUnlocked);

        CharactersSbloccatiAdapter charactersSbloccatiAdapter = new CharactersSbloccatiAdapter
                (getContext(), listCharactersUnlocked, charactersController);
        CharactersOttenibiliAdapter charactersOttenibiliAdapter = new CharactersOttenibiliAdapter
                (getContext(), listCharactersToBuy, charactersSbloccatiAdapter, nestedScrollView, charactersController);

        RecyclerViewCharactersToBuy.setAdapter(charactersOttenibiliAdapter);
        RecyclerViewCharactersUnlocked.setAdapter(charactersSbloccatiAdapter);
    }

    private void setFirstId(Map<String, Integer> mapCharactersPatient){
        for (Map.Entry<String, Integer> entry : mapCharactersPatient.entrySet()) {
            String key = entry.getKey();
            int value = Integer.parseInt(String.valueOf(entry.getValue()));
            if (value == 2) {
                listStringCharactersToBuy.add(key);
            }
        }
    }

    private void setIdLists(Map<String, Integer> mapCharactersPatient) {
        setFirstId(mapCharactersPatient);
        for (Map.Entry<String, Integer> entry : mapCharactersPatient.entrySet()) {
            String key = entry.getKey();
            int value = Integer.parseInt(String.valueOf(entry.getValue()));
            if (value == 1) {
                listStringCharactersToBuy.add(key);
            } else if (value == 0) {
                listStringCharactersUnlocked.add(key);
            }
        }
    }

}