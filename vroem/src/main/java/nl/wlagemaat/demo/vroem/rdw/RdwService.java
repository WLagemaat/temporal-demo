package nl.wlagemaat.demo.vroem.rdw;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalRdwError;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.mq.MQClient;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.generateLicensePlate;
import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class RdwService {

    private final TransgressionRepository transgressionRepository;
//    MQClient mqClient;

    /**
     * Determines if the license-plate is known or that a BAS-TASK has to be created
     */
    public FineProcessingResult determineLicenseplate(final FineDto fineDto){
        var resultaat = FineProcessingResult.builder().transgressionNumber(fineDto.transgressionNumber());
        if(doesPass(fineDto.rdwOdds())){
            resultaat.succeeded(true);
            saveLicensePlate(fineDto);
        } else {
            resultaat.isManualTask(true);
            resultaat.succeeded(true);

        }
        if(doesPass(fineDto.rdwTechnicalErrorOdds())){
            throw new TechnicalRdwError("RDW not reachable!");
        }
        return resultaat.build();
    }

    public void finishBasTask(final String transgressionNumber, final String licensePlate){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionNumber);
        if(transgression == null){
            throw new IllegalArgumentException("Transgression not found");
        }
        transgression.setLicensePlate(licensePlate);
        transgressionRepository.save(transgression);
    }

    private void saveLicensePlate(final FineDto fineDto){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(fineDto.transgressionNumber());
        if(transgression == null){
            throw new IllegalArgumentException("Transgression not found");
        }
        var licensePlate = generateLicensePlate();
        transgression.setLicensePlate(licensePlate);
        transgressionRepository.save(transgression);
    }

//    private void createBasTask(final FineDto fineDto){
//        mqClient.sendBasTask(fineDto.transgressionNumber());
//    }
}
