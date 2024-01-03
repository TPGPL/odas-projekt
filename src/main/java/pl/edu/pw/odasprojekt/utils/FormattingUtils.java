package pl.edu.pw.odasprojekt.utils;

public class FormattingUtils {
    public static String anonymizeData(String data, boolean shouldDisplay) {
        if (shouldDisplay) {
            return data;
        }

        var anonData = new StringBuilder();
        int maxLength = Math.min(3, data.length() / 2);

        for (int i = 0; i < maxLength; i++) {
            anonData.append(data.charAt(i));
        }

        anonData.append("******");

        return anonData.toString();
    }

    public static String formatCardNumber(String cardNumber) {
        int spacing = 4;
        var spacedNumber = new StringBuilder();

        for (int i = 0; i < cardNumber.length(); i++) {
            if (i % spacing == 0) {
                spacedNumber.append(" ");
            }

            spacedNumber.append(cardNumber.charAt(i));
        }

        return spacedNumber.toString();
    }
}
