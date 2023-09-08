package nl.wlagemaat.demo.vroem.rdw;

import nl.wlagemaat.demo.vroem.exception.TechnischeRdwError;
import nl.wlagemaat.demo.vroem.model.AanleveringDto;
import nl.wlagemaat.demo.vroem.model.OvertredingVerwerkingsResultaat;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.helper.KansBerekening.genereerKenteken;
import static nl.wlagemaat.demo.vroem.helper.KansBerekening.isKansGeslaagd;

@Service
public class RdwService {

    /**
     * Bekijkt adv kansberekening of een aanlevering een kenteken heeft.
     */
    public OvertredingVerwerkingsResultaat bepaalKenteken(AanleveringDto aanleveringDto){
        var resultaat = OvertredingVerwerkingsResultaat.builder().overtredingsNummer(aanleveringDto.overtredingsnummer());
        if(isKansGeslaagd(aanleveringDto.rdwKans())){
            resultaat.geslaagd(true);
        } else {
            // todo create BASTAAK
        }
        if(!isKansGeslaagd(aanleveringDto.rdwTechnischKans())){
            throw new TechnischeRdwError("RDW niet bereikbaar!");
        }
        return resultaat.build();
    }

    private void bewaarKenteken(AanleveringDto aanleveringDto){
        var kenteken = genereerKenteken();

    }
}
