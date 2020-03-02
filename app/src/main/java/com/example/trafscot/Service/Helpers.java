package com.example.trafscot.Service;

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
    public String getRoadworksImpact(int days){
        String colour = "";
        if (days < 5){
            colour = "green";
        } else if (days > 6 && days < 10 ){
            colour = "yellow";
        } else if (days > 11) {
            colour = "red";
        }
       return colour;
    }
}
