package com.example.solarprototype.BarChartOperations;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class frontpagedetails extends monthlydetails {



    public static String today;

    public static String yesterday;

    public static ArrayList<String> firstmonth = new ArrayList<String>();

    public static ArrayList<String> secondmonth = new ArrayList<String>();




    public static void daysoftwomonths(String currentdate)
    {
        if(firstmonth.size()<1 && secondmonth.size()<1) {
            Calendar startmonth = returncalendar(2020, Calendar.APRIL);
            Calendar endmonth = returncalendar(2020, Calendar.APRIL);
            ArrayList<String> daysOfMonth = new ArrayList<>();
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startmonth.setTime(date);
            endmonth.setTime(date);
            startmonth.set(Calendar.MONTH, startmonth.get(Calendar.MONTH) - 1);
            startmonth.set(Calendar.DAY_OF_MONTH, 1);
            endmonth.set(Calendar.MONTH, endmonth.get(Calendar.MONTH));
            endmonth.set(Calendar.DAY_OF_MONTH, 1);
            int firstmonthmaxDay = startmonth.getActualMaximum(Calendar.DAY_OF_MONTH);
            int secondmonthmaxDay = endmonth.getActualMaximum(Calendar.DAY_OF_MONTH);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < firstmonthmaxDay; i++) {
                startmonth.set(Calendar.DAY_OF_MONTH, i + 1);
                daysOfMonth.add(df.format(startmonth.getTime()));
            }

            daysOfMonth.add("----------");

            for (int i = 0; i < secondmonthmaxDay; i++) {
                endmonth.set(Calendar.DAY_OF_MONTH, i + 1);
                daysOfMonth.add(df.format(endmonth.getTime()));
            }

            separatemonths(daysOfMonth);
            todayandyesterday(currentdate);
        }
    }


    public static void separatemonths(ArrayList<String> twomonthlist)
    {
        int midpos = twomonthlist.indexOf("----------");

        for(int i=0;i<midpos;i++)
        {
            firstmonth.add(twomonthlist.get(i));
        }

        for(int i=midpos+1;i<twomonthlist.size();i++)
        {
            secondmonth.add(twomonthlist.get(i));
        }
    }


    public static void todayandyesterday(String currentdate)
    {
        Calendar calendar = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(date);
        today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE,-1);
        yesterday = dateFormat.format(calendar.getTime());
    }


}
