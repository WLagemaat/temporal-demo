package nl.wlagemaat.demo.vroem.service.cvom;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.exception.TechnicalCVOMError;
import nl.wlagemaat.demo.vroem.exception.TechnicalMulderError;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
public class CvomService {

    /**
     * Determines if a message will reach CVOM based on the given technical error odds
     */
    public FineProcessingResult sendToCvom(FineDto fineDto) {
        var resultaat = FineProcessingResult.builder().transgressionNumber(fineDto.transgressionNumber());
        if (doesPass("CVOMTechnicalError", fineDto.cvomTechnicalErrorOdds())) {
            throw new TechnicalCVOMError("CVOM not reached!");
        }
        resultaat.succeeded(true);
        return resultaat.build();
    }
}
