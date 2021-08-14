package Helpers;

import java.util.Random;

public class DataHelper {

    public static String generateRandomEmail(){
        return String.format("%s@testemail.com", generateRandomString(7));
    }



    private static String generateRandomString(int targetStringLength){
        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for(int i = 0; i < targetStringLength; i++){
            int randomLimitInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char)randomLimitInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }
}
