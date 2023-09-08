package nl.wlagemaat.demo.vroem.intake;

import nl.wlagemaat.demo.vroem.model.AanleveringDto;
import nl.wlagemaat.demo.vroem.model.OvertredingVerwerkingsResultaat;
import nl.wlagemaat.demo.vroem.repository.entiteiten.Overtreding;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.helper.KansBerekening.isKansGeslaagd;

@Service
public class BonAanleveringService {

    /**
     * Bekijkt adv kansberekening of een aanlevering valide is.
     */
    public OvertredingVerwerkingsResultaat isValideAanlevering(AanleveringDto aanleveringDto){
        var resultaat = OvertredingVerwerkingsResultaat.builder().overtredingsNummer(aanleveringDto.overtredingsnummer());
        if(isKansGeslaagd(aanleveringDto.validatieKans())){
            resultaat.geslaagd(true);
            bewaarOvertreding(aanleveringDto);
        } else {
            resultaat.geslaagd(false).errorMessage("Invalide aanlevering");
        }
        return resultaat.build();
    }

    private void bewaarOvertreding(AanleveringDto aanleveringDto){
        Overtreding overtreding = new Overtreding();
        overtreding.setOvertredingsnummer(aanleveringDto.overtredingsnummer());
        overtreding.setMulder(aanleveringDto.isMulder());

        // todo opslaan
    }
}
