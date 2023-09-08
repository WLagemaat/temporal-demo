package nl.wlagemaat.demo.vroem.rdw;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalRdwError;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.model.TransgressionProcessingResult;
import nl.wlagemaat.demo.vroem.mq.MQClient;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entiteiten.Transgression;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.helper.VroemUtilities.generateLicenseplate;
import static nl.wlagemaat.demo.vroem.helper.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class RdwService {

    TransgressionRepository transgressionRepository;
    MQClient mqClient;

    /**
     * Bekijkt adv kansberekening of een aanlevering een kenteken heeft.
     */
    public TransgressionProcessingResult determineLicenseplate(TransgressionDto transgressionDto){
        var resultaat = TransgressionProcessingResult.builder().transgressionNumber(transgressionDto.transgressionNumber());
        if(doesPass(transgressionDto.rdwOdds())){
            resultaat.succeeded(true);
            saveKenteken(transgressionDto);
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

    private void saveKenteken(TransgressionDto transgressionDto){
        Transgression transgression = transgressionRepository.findByOvertredingsnummer(transgressionDto.transgressionNumber());
        var kenteken = generateLicenseplate();
        transgression.setKenteken(kenteken);
        transgressionRepository.save(transgression);
    }

    private void createBasTask(TransgressionDto transgressionDto){
        mqClient.sendBasTask(transgressionDto.transgressionNumber());
    }
}
