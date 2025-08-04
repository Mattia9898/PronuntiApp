package views.fragment.userPaziente.giochi.esercizi;

import models.domain.esercizi.EsercizioRealizzabile;

import models.domain.scenariGioco.ScenarioGioco;

import views.fragment.AbstractNavigazioneFragment;

public class AbstractFineScenarioEsercizioFragment extends AbstractNavigazioneFragment {


    protected boolean checkFineScenario(ScenarioGioco scenarioGioco) {

        for(EsercizioRealizzabile s : scenarioGioco.getEsercizi()) {
            if(s.getRisultatoEsercizio()== null)
                return false;
        }
        return true;
    }

}
