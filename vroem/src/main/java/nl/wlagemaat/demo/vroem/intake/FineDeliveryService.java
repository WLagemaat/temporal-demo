package nl.wlagemaat.demo.vroem.intake;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.model.TransgressionProcessingResult;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entiteiten.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.helper.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class FineDeliveryService {

    TransgressionRepository transgressionRepository;

    /**
     * Bekijkt adv kansberekening of een aanlevering valide is.
     */
    public TransgressionProcessingResult validate(TransgressionDto transgressionDto){
        var resultaat = TransgressionProcessingResult.builder().transgressionNumber(transgressionDto.transgressionNumber());
        if(doesPass(transgressionDto.validOdds())){
            resultaat.succeeded(true);
            saveTransgression(transgressionDto);
        } else {
            resultaat.succeeded(false).errorMessage("Invalide aanlevering");
        }
        return resultaat.build();
    }

    private void saveTransgression(TransgressionDto transgressionDto){
        Transgression transgression = new Transgression();
        transgression.setOvertredingsnummer(transgressionDto.transgressionNumber());
        transgression.setMulder(transgressionDto.isMulder());
        transgressionRepository.save(transgression);
    }
}
