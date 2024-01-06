package pl.edu.pw.odasprojekt.utils;

import pl.edu.pw.odasprojekt.model.dtos.PasswordFragmentDto;

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

        return Pattern.compile("[a-zA-Z0-9@!%$#^&*+=_ ]{16}").matcher(password).find();
    }

    public static boolean verifyPasswordFragment(PasswordFragmentDto pass) {
        if (pass == null || pass.getIndex() < 0 || pass.getIndex() > 15) {
            return false;
        }

        return Pattern.compile("[a-zA-Z0-9@!%$#^&*+=_ ]").matcher(String.valueOf(pass.getValue())).find();
    }
}
