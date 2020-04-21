package com.example.trafscot.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Alexander Carruthers - S1828301
 */
public class Helpers {
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
