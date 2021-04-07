package fxOpenapu;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * Käsittelijä oppilaan poistamiseen tietokannasta. Ei toimi, eikä käytetä.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 7.2.2018
 *
 */
public class PoistaOppilasGUIController implements ModalControllerInterface<String> {
    
    /**
     * Peruuttaa muutokset ja sulkee ikkunan.
     */
    @FXML private void handlePeruuta() {
        Dialogs.showMessageDialog("Vielä ei osata peruuttaa");
    }
    
    /**
     * Käsittelee oppilaiden poiston.
     */
    @FXML private void handlePoistaOppilaat(){
        Dialogs.showMessageDialog("Vielä ei osata poistaa oppilaita");
    }

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
}
