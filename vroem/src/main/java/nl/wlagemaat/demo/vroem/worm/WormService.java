package nl.wlagemaat.demo.vroem.worm;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalRdwError;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.model.TransgressionProcessingResult;
import nl.wlagemaat.demo.vroem.mq.MQClient;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class WormService {

    TransgressionRepository transgressionRepository;
    MQClient mqClient;

    /**
     * Determines the person that was driving
     */
    public TransgressionProcessingResult determinePersonConcerned(final TransgressionDto transgressionDto){
        if(!doesPass(transgressionDto.wormTechnicalErrorOdds())){
            throw new TechnicalRdwError("WORM not reachable!");
        }
        createWormTask(transgressionDto);
        var resultaat = TransgressionProcessingResult.builder().transgressionNumber(transgressionDto.transgressionNumber());
        resultaat.succeeded(true);
        return resultaat.build();
    }

    public void finishWormTask(final String transgressionNumber, final String personConcerned){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionNumber);
        transgression.setPersonConcerned(personConcerned);
        transgressionRepository.save(transgression);
    }

    private void createWormTask(TransgressionDto transgressionDto){
        mqClient.sendWormTask(transgressionDto.transgressionNumber());
    }
}
