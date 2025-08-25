package views.fragment.userPaziente.giochi.esercizi;


import views.fragment.AbstractNavigationFragment;

import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.scenariGioco.ScenarioGioco;


public class AbstractFineScenarioFragment extends AbstractNavigationFragment {

    protected boolean checkEndScenery(ScenarioGioco gioco) {

        for (EsercizioRealizzabile esercizioRealizzabile : gioco.getlistEsercizioRealizzabile()) {

            if (esercizioRealizzabile.getRisultatoEsercizio() == null)
                return false;
        }

        return true;
    }

}
