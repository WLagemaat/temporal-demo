package nl.wlagemaat.demo.vroem.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class VroemUtilities {

    private static final Faker faker = new Faker();

    /**
     * Randomized in percentages
     * @param kans 1-100 %
     * @return true als het succeeded is
     */
    public static boolean doesPass(Integer kans){
        int dice = ThreadLocalRandom.current().nextInt(100);
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
