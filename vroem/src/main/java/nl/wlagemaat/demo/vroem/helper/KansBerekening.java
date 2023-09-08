package nl.wlagemaat.demo.vroem.helper;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class KansBerekening {

    FakeValuesService fakeValuesService = new FakeValuesService(
            Locale.of("nl","NL"), new RandomService());
    /**
     * Randomized in percentages
     * @param kans 1-100 %
     * @return true als het geslaagd is
     */
    public static boolean isKansGeslaagd(Integer kans){
        int dice = ThreadLocalRandom.current().nextInt(100);
        return dice <= kans;
    }

    /**
     * Genereer een kenteken in het formaat: DD-44-DD
     */
    public static String genereerKenteken(){
        return fakeValuesService.bothify("??-##-??").toUpperCase();
    }
}
