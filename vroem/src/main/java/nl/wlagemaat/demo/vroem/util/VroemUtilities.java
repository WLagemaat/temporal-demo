package nl.wlagemaat.demo.vroem.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
@Slf4j
public class VroemUtilities {

    private static final Faker faker = new Faker();

    /**
     * Randomized in percentages
     * @param kans 1-100 %
     * @return true als het succeeded is
     */
    public static boolean doesPass(Integer kans){
        int dice = ThreadLocalRandom.current().nextInt(100);
        log.info("doesPass odds:{}, diced: {} ",kans,dice);
        return dice <= kans;
    }

    /**
     * Genereer een kenteken in het formaat: DD-44-DD
     */
    public static String generateLicensePlate(){
        return faker.bothify("??-##-??").toUpperCase();
    }

    /**
     * Genereer een kenteken in het formaat: DD-44-DD
     */
    public static String generateTransgressionNumber(){
        return faker.bothify("TEST-######-??").toUpperCase();
    }

}
