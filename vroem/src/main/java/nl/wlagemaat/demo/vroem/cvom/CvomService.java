package nl.wlagemaat.demo.vroem.cvom;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalMulderError;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.model.TransgressionProcessingResult;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class CvomService {

    /**
     * Determines if a message will reach Mulder based on the given technical error odds
     */
    public TransgressionProcessingResult sendToCvom(TransgressionDto transgressionDto) {
        var resultaat = TransgressionProcessingResult.builder().transgressionNumber(transgressionDto.transgressionNumber());
        if (!doesPass(transgressionDto.cvomTechnicalErrorOdds())) {
            throw new TechnicalMulderError("CVOM not reached!");
        }
        resultaat.succeeded(true);
        return resultaat.build();
    }
}
