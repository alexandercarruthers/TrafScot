package com.example.trafscot.Service;

import android.graphics.Color;

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

    /**
     *
     * @param days
     * @return
     */
    public int getRoadworksImpact(int days){
        if (days < 5){
            return Color.GREEN;
        } else if (days > 6 && days < 10 ){
            return Color.YELLOW;
        } else if (days > 11) {
            return Color.RED;
        }
        return 0;
    }
}
