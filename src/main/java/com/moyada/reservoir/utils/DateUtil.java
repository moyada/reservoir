package com.moyada.reservoir.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DateUtil {

    public static Timestamp now() {
        return Timestamp.from(Instant.now());
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
