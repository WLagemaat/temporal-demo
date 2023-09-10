package nl.wlagemaat.demo.vroem.rdw;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalRdwError;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.model.TransgressionProcessingResult;
import nl.wlagemaat.demo.vroem.mq.MQClient;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.generateLicensePlate;
import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class RdwService {

    TransgressionRepository transgressionRepository;
    MQClient mqClient;

    /**
     * Determines if the licenseplate is known or that a BAS-TASK has to be created
     */
    public TransgressionProcessingResult determineLicenseplate(final TransgressionDto transgressionDto){
        var resultaat = TransgressionProcessingResult.builder().transgressionNumber(transgressionDto.transgressionNumber());
        if(doesPass(transgressionDto.rdwOdds())){
            resultaat.succeeded(true);
            saveLicensePlate(transgressionDto);
        } else {
            createBasTask(transgressionDto);
            resultaat.isManualTask(true);
            resultaat.succeeded(true);

        }
        if(!doesPass(transgressionDto.rdwTechnicalErrorOdds())){
            throw new TechnicalRdwError("RDW not reachable!");
        }
        return resultaat.build();
    }

    public void finishBasTask(final String transgressionNumber, final String licensePlate){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionNumber);
        transgression.setLicensePlate(licensePlate);
        transgressionRepository.save(transgression);
    }

    private void saveLicensePlate(final TransgressionDto transgressionDto){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(transgressionDto.transgressionNumber());
        var licensePlate = generateLicensePlate();
        transgression.setLicensePlate(licensePlate);
        transgressionRepository.save(transgression);
    }

    private void createBasTask(TransgressionDto transgressionDto){
        mqClient.sendBasTask(transgressionDto.transgressionNumber());
    }
}
