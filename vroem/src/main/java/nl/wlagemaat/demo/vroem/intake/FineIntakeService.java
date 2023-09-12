package nl.wlagemaat.demo.vroem.intake;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class FineIntakeService {

    TransgressionRepository transgressionRepository;

    /**
     * Bekijkt adv kansberekening of een aanlevering valide is.
     */
    public FineProcessingResult validate(FineDto fineDto){
        var resultaat = FineProcessingResult.builder().transgressionNumber(fineDto.transgressionNumber());
        if(doesPass(fineDto.validOdds())){
            resultaat.succeeded(true);
            saveTransgression(fineDto);
        } else {
            resultaat.succeeded(false).errorMessage("Invalide aanlevering");
        }
        return resultaat.build();
    }

    private void saveTransgression(FineDto fineDto){
        Transgression transgression = new Transgression();
        transgression.setTransgressionNumber(fineDto.transgressionNumber());
        transgression.setMulder(fineDto.isMulder());
        transgressionRepository.save(transgression);
    }
}
