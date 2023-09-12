package nl.wlagemaat.demo.vroem.mulder;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.exception.TechnicalMulderError;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class MulderService {

    /**
     * Determines if a message will reach Mulder based on the given technical error odds
     */
    public FineProcessingResult sendToMulder(FineDto fineDto) {
        var resultaat = FineProcessingResult.builder().transgressionNumber(fineDto.transgressionNumber());
        if (!doesPass(fineDto.mulderTechnicalErrorOdds())) {
            throw new TechnicalMulderError("Mulder not reached!");
        }
        resultaat.succeeded(true);
        return resultaat.build();
    }
}
