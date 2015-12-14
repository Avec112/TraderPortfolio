package no.avec.traderportfolio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by avec on 14/12/15.
 */

public class Utils {



    public static Date parseDate(String date, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(date);
    }

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
}
