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
     * @param odds 1-100 %
     * @return true als het succeeded is
     */
    public static boolean doesPass(String reason, Integer odds){
        int dice = ThreadLocalRandom.current().nextInt(100);
        log.info("{} odds:{}, diced: {} ", reason, odds, dice);
        return dice <= odds;
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
