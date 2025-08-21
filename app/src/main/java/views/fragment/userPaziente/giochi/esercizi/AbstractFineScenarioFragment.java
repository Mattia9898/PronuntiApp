package views.fragment.userPaziente.giochi.esercizi;


import views.fragment.AbstractNavigazioneFragment;

import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.scenariGioco.ScenarioGioco;


public class AbstractFineScenarioFragment extends AbstractNavigazioneFragment {

    protected boolean checkEndScenery(ScenarioGioco gioco) {

        for (EsercizioRealizzabile esercizioRealizzabile : gioco.getlistEsercizioRealizzabile()) {

            if (esercizioRealizzabile.getRisultatoEsercizio() == null)
                return false;
        }

        return true;
    }

}
