package nl.wlagemaat.demo.vroem.mulder;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalMulderError;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.model.TransgressionProcessingResult;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.helper.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class MulderService {

    /**
     * Determines if a message will reach Mulder based on the given technical error odds
     */
    public TransgressionProcessingResult sendToMulder(TransgressionDto transgressionDto) {
        var resultaat = TransgressionProcessingResult.builder().transgressionNumber(transgressionDto.transgressionNumber());
        if (!doesPass(transgressionDto.mulderTechnicalErrorOdds())) {
            throw new TechnicalMulderError("Mulder not reached!");
        }
        resultaat.succeeded(true);
        return resultaat.build();
    }
}
