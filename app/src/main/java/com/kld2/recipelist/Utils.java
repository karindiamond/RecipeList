package com.kld2.recipelist;

/**
 * Created by Karin on 2/24/2018.
 */

public class Utils {

    public static String timeToString(int totalMinutes) {
        if (totalMinutes == 0) {
            return "---";
        }
        String result = "";
        int hours = totalMinutes / 60;
        if (hours > 0) {
            result += hours + " hour";
            if (hours > 1) {
                result += "s";
            }
        }
        int minutes = totalMinutes % 60;
        if (minutes > 0) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += minutes + " minute";
            if (minutes > 1) {
                result += "s";
            }
        }
        return result;
    }
}
