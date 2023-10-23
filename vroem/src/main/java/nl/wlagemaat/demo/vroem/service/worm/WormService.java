package nl.wlagemaat.demo.vroem.service.worm;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.exception.TechnicalRdwError;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class WormService {

    TransgressionRepository transgressionRepository;

    /**
     * Determines the person that was driving
     */
    public FineProcessingResult determinePersonConcerned(final FineDto fineDto){
        if(!doesPass("WormTechnicalError", fineDto.wormTechnicalErrorOdds())){
            throw new TechnicalRdwError("WORM not reachable!");
        }
        createWormTask(fineDto);
        var resultaat = FineProcessingResult.builder().transgressionNumber(fineDto.transgressionNumber());
        resultaat.succeeded(true);
        return resultaat.build();
    }

    public void finishWormTask(final String transgressionNumber, final String personConcerned){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionNumber);
        transgression.setPersonConcerned(personConcerned);
        transgressionRepository.save(transgression);
    }

    private void createWormTask(FineDto fineDto){
//        mqClient.sendWormTask(fineDto.transgressionNumber());
    }
}
