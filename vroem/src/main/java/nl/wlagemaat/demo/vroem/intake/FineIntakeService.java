package nl.wlagemaat.demo.vroem.intake;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import nl.wlagemaat.demo.vroem.util.VroemUtilities;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class FineIntakeService {

    private final TransgressionRepository transgressionRepository;

    /**
     * Bekijkt adv kansberekening of een aanlevering valide is.
     */
    public FineProcessingResult validate(FineDto fineDto){
        var result = FineProcessingResult.builder();
        if(doesPass(fineDto.validOdds())){
            result.succeeded(true);
            result.transgressionNumber(saveTransgression(fineDto));
        } else {
            result.succeeded(false).errorMessage("invalid fine");
        }
        return result.build();
    }

    private String saveTransgression(FineDto fineDto){
        Transgression transgression = new Transgression();
        transgression.setTransgressionNumber(VroemUtilities.generateTransgressionNumber());
        transgression.setMulder(fineDto.isMulder());
        transgressionRepository.save(transgression);
        return transgression.getTransgressionNumber();
    }
}
