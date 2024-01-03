package pl.edu.pw.odasprojekt.utils;

import pl.edu.pw.odasprojekt.model.dtos.PasswordFragmentDto;

import java.util.HashSet;
import java.util.regex.Pattern;

public class ValidatorUtils {
    public static boolean verifyClientNumber(String clientNumber) {
        if (clientNumber == null || clientNumber.length() != 13) {
            return false;
        }

        return Pattern.compile("[0-9]{13}").matcher(clientNumber).find();
    }

    public static boolean verifyPasswordFragments(PasswordFragmentDto[] passwordFrags) {
        if (passwordFrags == null) {
            return false;
        }

        var pattern = Pattern.compile("[0-9a-zA-Z@!$% ]");
        var indexes = new HashSet<Integer>();

        for (var frag : passwordFrags) {
            if (frag == null)
                return false;

            if (frag.getIndex() < 0 || frag.getIndex() > 15)
                return false;

            if (!pattern.matcher(String.valueOf(frag.getValue())).find()) {
                return false;
            }

            indexes.add(frag.getIndex());
        }

        return indexes.size() == 3;
    }
}
