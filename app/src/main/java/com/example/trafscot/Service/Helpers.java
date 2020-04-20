package com.example.trafscot.Service;


import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.example.trafscot.R;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Helpers {
    /**
     * Call by getDateDiff(date1,date2,TimeUnit.MINUTES);
     * @param date1
     * @param date2
     * @param timeUnit
     * @return
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }


}
