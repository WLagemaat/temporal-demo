package nl.wlagemaat.demo.vroem.service.sven;

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
public class SvenService {

    TransgressionRepository transgressionRepository;

    /**
     * Determines if the concerned person has to be prosecuted
     */
    public FineProcessingResult determineProsecution(final FineDto fineDto){
        if(!doesPass("SvenTechnicalError", fineDto.svenTechnicalErrorOdds())){
            throw new TechnicalRdwError("SVEN not reachable!");
        }
        createSvenTask(fineDto);
        var resultaat = FineProcessingResult.builder().transgressionNumber(fineDto.transgressionNumber());
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
//        mqClient.sendWormTask(transgressionNumber);
    }

    private void createSvenTask(FineDto fineDto){
//        mqClient.sendWormTask(fineDto.transgressionNumber());
    }
}
