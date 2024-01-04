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

    public static boolean verifyCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }

        return Pattern.compile("[0-9]{16}").matcher(cardNumber).find();
    }

    public static boolean verifyPaymentTitle(String title) {
        if (title == null || title.isEmpty() || title.length() > 99) {
            return false;
        }

        return Pattern.compile("[A-Za-z0-9!.,? ]{1,99}").matcher(title).find();
    }

    public static boolean verifyResetToken(String token) {
        if (token == null || token.isEmpty() || token.length() > 50) {
            return false;
        }

        return Pattern.compile("[0-9a-f-]{1,50}").matcher(token).find();
    }

    public static boolean verifyPassword(String password) {
        if (password == null || password.length() != 16) {
            return false;
        }

        return Pattern.compile("[a-zA-Z0-9@!%$ ]{16}").matcher(password).find();
    }

    // TODO: refactor this function to only verify a single fragment
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
