package nl.wlagemaat.demo.vroem.rdw;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalRdwError;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.model.ValidatedFineDto;
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
    private final MQClient mqClient;

    /**
     * Determines if the licenseplate is known or that a BAS-TASK has to be created
     */
    public FineProcessingResult determineLicenseplate(final ValidatedFineDto validatedFineDto){
        var resultaat = FineProcessingResult.builder().transgressionNumber(validatedFineDto.transgressionNumber());
        if(doesPass(validatedFineDto.fineInput().rdwOdds())){
            resultaat.succeeded(true);
            saveLicensePlate(validatedFineDto.transgressionNumber());
        } else {
            createBasTask(validatedFineDto.transgressionNumber());
            resultaat.isManualTask(true);
            resultaat.succeeded(true);

        }
        if(!doesPass(validatedFineDto.fineInput().rdwTechnicalErrorOdds())){
            throw new TechnicalRdwError("RDW not reachable!");
        }
        return resultaat.build();
    }

    public void finishBasTask(final String transgressionNumber, final String licensePlate){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionNumber);
        transgression.setLicensePlate(licensePlate);
        transgressionRepository.save(transgression);
    }

    private void saveLicensePlate(final String transgressionNumber){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionNumber);
        var licensePlate = generateLicensePlate();
        transgression.setLicensePlate(licensePlate);
        transgressionRepository.save(transgression);
    }

    private void createBasTask(final String transgressionNumber){
        mqClient.sendBasTask(transgressionNumber);
    }
}
