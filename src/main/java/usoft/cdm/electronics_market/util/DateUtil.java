package usoft.cdm.electronics_market.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateUpFile = new SimpleDateFormat("ddMMyyyyhhmmss");

    public static SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");

    public static Date today() {

        return new Date();
    }

    public static String todayStr() {
        return sdf.format(today());
    }

    public static String todayStrTime() {
        return sdtf.format(today());
    }

    public static String dateUpFile() {
        return dateUpFile.format(today());
    }

    public static String formattedDate(LocalDate date) {
        return date != null ? sdf.format(date) : todayStr();
    }


}
