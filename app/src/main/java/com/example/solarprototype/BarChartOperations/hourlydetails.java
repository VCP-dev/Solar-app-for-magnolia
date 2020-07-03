package com.example.solarprototype.BarChartOperations;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.ArrayList;

public class hourlydetails {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long returntimeinepoch()
    {
        return Instant.now().getEpochSecond();
    }



    public static ArrayList<String> hoursofdayiso8601(String day)
    {
        ArrayList<String> hours= new ArrayList<String>();
        for(int i=0;i<=24;i++)
        {
            try {
                String hour = hournum(i);
                String item = ""+day + "T" + hour +":00:00";//tsToSec8601(day + "T" + hour +":00:00");
                hours.add(item);
            }
            catch(NullPointerException ex)
            {
                System.out.println(ex.getCause());
            }
        }
        return hours;
    }



    public static ArrayList<String> hoursofdayepoch(String day)
    {
        ArrayList<String> hours= new ArrayList<String>();
        for(int i=0;i<=24;i++)
        {
            try {
                String hour = hournum(i);
                String item = ""+tsToSec8601(day + "T" + hour +":00:00");
                hours.add(item);
            }
            catch(NullPointerException ex)
            {
                System.out.println(ex.getCause());
            }
        }
        return hours;
    }



    public static ArrayList<String> hoursofdayepochfivemindiff(String day)
    {
        ArrayList<String> hours= new ArrayList<String>();
        for(int i=0;i<=24;i++)
        {
            try {
                String hour = hournum(i);
                //String item = ""+tsToSec8601(day + "T" + hour +":00:00");
                if(i<24)
                {
                    for (int j = 0; j <= 55; j += 5) {
                        String min = hournum(j);
                        String item = "" + tsToSec8601(day + "T" + hour + ":" + min + ":00");
                        hours.add(item);
                    }
                }
                else
                {
                    String min = hournum(0);
                    String item = "" + tsToSec8601(day + "T" + hour + ":" + min + ":00");
                    hours.add(item);
                    break;
                }
                //hours.add(item);
            }
            catch(NullPointerException ex)
            {
                System.out.println(ex.getCause());
            }
        }
        return hours;
    }



    public static ArrayList<String> hoursofdayiso8601fivemindiff(String day)
    {
        ArrayList<String> hours= new ArrayList<String>();
        for(int i=0;i<=24;i++)
        {
            try
            {
                String hour = hournum(i);
                //String item = ""+day + "T" + hour +":00:00";//tsToSec8601(day + "T" + hour +":00:00");
                if(i<24)
                {
                    for (int j = 0; j <= 55; j += 5) {
                        String min = hournum(j);
                        String item = "" + day + "T" + hour + ":" + min + ":00";
                        hours.add(item);
                    }
                }
                else
                {
                    String min = hournum(0);
                    String item = "" + day + "T" + hour + ":" + min + ":00";
                    hours.add(item);
                    break;
                }
                //hours.add(item);
            }
            catch(NullPointerException ex)
            {
                System.out.println(ex.getCause());
            }
        }
        return hours;
    }


    static String hournum(int hour)   /// used for both minutes and hours
    {
        String res=null;
        res = (hour<10)?("0"+hour):(""+hour);
        return res;
    }



    public static int tsToSec8601(String timestamp){
        //if(timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int)(epoch/1000);
        } catch(ParseException e) {
            System.out.println("Could not parse cus "+e.getMessage());
            return 0;
        }

    }

}
