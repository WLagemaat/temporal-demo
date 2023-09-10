package nl.wlagemaat.demo.vroem.sven;

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
public class SvenService {

    TransgressionRepository transgressionRepository;
    MQClient mqClient;

    /**
     * Determines if the concerned person has to be prosecuted
     */
    public TransgressionProcessingResult determineProsecution(final TransgressionDto transgressionDto){
        if(!doesPass(transgressionDto.svenTechnicalErrorOdds())){
            throw new TechnicalRdwError("SVEN not reachable!");
        }
        createSvenTask(transgressionDto);
        var resultaat = TransgressionProcessingResult.builder().transgressionNumber(transgressionDto.transgressionNumber());
        resultaat.succeeded(true);
        return resultaat.build();
    }

    public void finishSvenTask(final String transgressionNumber, final String prosecuted){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionNumber);
        transgression.setProsecuted(prosecuted.toLowerCase().contains("true"));
        transgressionRepository.save(transgression);
    }

    public void backToWorm(final String transgressionNumber){
        createWormTask(transgressionNumber);
    }

    private void createWormTask(final String transgressionNumber){
        mqClient.sendWormTask(transgressionNumber);
    }

    private void createSvenTask(TransgressionDto transgressionDto){
        mqClient.sendWormTask(transgressionDto.transgressionNumber());
    }
}
