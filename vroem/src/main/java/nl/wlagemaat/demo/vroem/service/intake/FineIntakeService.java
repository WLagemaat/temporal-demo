package nl.wlagemaat.demo.vroem.service.intake;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class FineIntakeService {

    private final TransgressionRepository transgressionRepository;

    /**
     * Based on the supplied odds, determines if the fine is valid
     */
    public FineProcessingResult validate(FineDto fineDto){
        var resultaat = FineProcessingResult.builder();
        if(doesPass("isValidFine" , fineDto.validOdds())){
            resultaat.succeeded(true);
        } else {
            resultaat.succeeded(false).errorMessage("Invalide aanlevering");
        }
        return resultaat.build();
    }

    /**
     * Stores the transgression in the database
     */
    public FineProcessingResult saveTransgression(FineDto fineDto){

        Transgression transgression = new Transgression();
        transgression.setTransgressionNumber(fineDto.transgressionNumber());
        transgression.setMulder(fineDto.isMulder());
        transgressionRepository.save(transgression);

        return FineProcessingResult.builder().transgressionNumber(transgression.getTransgressionNumber()).succeeded(true).build();
    }
}
