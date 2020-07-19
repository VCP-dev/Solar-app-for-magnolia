package com.example.solarprototype.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.solarprototype.BarChartOperations.WeeklyDetails;
import com.example.solarprototype.BarChartOperations.hourlydetails;
import com.example.solarprototype.BarChartOperations.monthlydetails;
import com.example.solarprototype.BarChartOperations.yearlydetails;
import com.example.solarprototype.MainActivity;
import com.example.solarprototype.R;
import com.example.solarprototype.RequestedValues.HourlyValues;
import com.example.solarprototype.RequestedValues.Interval;
import com.example.solarprototype.RequestedValues.WeeklyValues;
import com.example.solarprototype.SolarApi;
import com.example.solarprototype.SplashScreenActivity;
import com.example.solarprototype.StoredValues;
import com.example.solarprototype.SummaryOfDay;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EnergyFragment extends Fragment {


    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;
    TabLayout tabLayout;
    Button selectweek;


    TextView barselectedvalue;
    TextView Barselectedavgvalue;
    TextView BarselectedY;
    TextView totalpower;


    Spinner monthselector;
    Spinner yearselector;


    public static final String tag = "EnergyFragment";



    float totalvalue;



    int yeartocheckwith;



    public static final int EnergyREQUEST_CODE = 22;    ///  Used to identify the result

    private OnFragmentInteractionListener mListener;

    public static EnergyFragment newInstance() {
        EnergyFragment fragment = new EnergyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public EnergyFragment()
    {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == EnergyREQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            String selectedDate = data.getStringExtra("selectedDate");
            totalpower.setText("No data...");
            BarselectedY.setText("");
            barselectedvalue.setText("No data...");
            Barselectedavgvalue.setText("No data...");
            // set the values
            switch(selectweek.getText().toString())
            {

                case "Generate graph for Desired Day":
                    createhourlygraph("Hourly analysis of "+selectedDate,selectedDate);
                    break;

                /*case "Select Desired Week":
                    createWeekBarGraph("Weekly analysis",selectedDate);
                    break;*/

               /* case "Select Desired Month":
                    createMonthBargraph("Monthly analysis",selectedDate);
                    break;*/

                default:
                    break;
            }

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    public String returncurrentdate()
    {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String curdate = dateFormat.format(calendar.getTime());
        return curdate;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_energy,container,false);

        totalvalue = 0;

        selectweek = v.findViewById(R.id.setweekbutton);

        totalpower = v.findViewById(R.id.totalpower);
        barselectedvalue = v.findViewById(R.id.barselectedvalue);
        Barselectedavgvalue = v.findViewById(R.id.barselectedavgvalue);
        BarselectedY = v.findViewById(R.id.barselectedY);

        monthselector = v.findViewById(R.id.spinnermonth);
        yearselector = v.findViewById(R.id.spinneryear);

        barChart = v.findViewById(R.id.barchart);
        barChart.setScaleEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);


        monthselector.setVisibility(View.GONE);
        yearselector.setVisibility(View.GONE);

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        //changegraph(0);
        //createWeekBarGraph("Weekly analysis","2020-03-19");

        //     createWeekBarGraph("Weekly analysis",returncurrentdate());


        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                float totalpowerproduced; //= totalvalue;

                    totalpowerproduced=totalvalue;

                String name = typeoftab(tabLayout.getSelectedTabPosition());
                if(name=="hour")
                {
                    name="day";
                }
                totalpower.setText("Power produced during entire "+(name)+" = "+totalpowerproduced + " kWh");//totalpower.setText("Total power produced : "+totalpowerproduced);

                BarselectedY.setText("Details for "+barChart.getXAxis().getValues().get(e.getXIndex())+":");//BarselectedY.setText("Data for "+barChart.getXAxis().getValues().get(e.getXIndex())+":");

                final String selectedValue=String.valueOf(e.getVal());
                barselectedvalue.setText(selectedValue+" kWh");//barselectedvalue.setText("Power generated : "+selectedValue);

                float avgvalue;

                switch (name){

                    case "day":
                    case "month":
                        avgvalue = Float.parseFloat(selectedValue)/49.7f;
                        Barselectedavgvalue.setText(""+avgvalue);//Barselectedavgvalue.setText("Units/kWP : "+avgvalue);
                        break;

                    case "year":
                        int numberofdays = numberofdaysofmonth(barChart.getXAxis().getValues().get(e.getXIndex()),yeartocheckwith);
                        avgvalue = Float.parseFloat(selectedValue)/(49.7f*numberofdays);
                        Barselectedavgvalue.setText(""+avgvalue);//Barselectedavgvalue.setText("Units/kWP : "+avgvalue);
                        break;
                }
             //   float avgvalue = Float.parseFloat(selectedValue)/49.7f;
             //   Barselectedavgvalue.setText(""+avgvalue);//Barselectedavgvalue.setText("Units/kWP : "+avgvalue);

            }

            @Override
            public void onNothingSelected() {
                totalpower.setText("No data...");
                BarselectedY.setText("No date selected...");
                barselectedvalue.setText("No data...");
                Barselectedavgvalue.setText("No data...");
            }
        });


        tabLayout = v.findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#ffffff"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                barChart.clear();
                changegraph(tab.getPosition());
                totalpower.setText("No data...");
                BarselectedY.setText("No date selected...");
                barselectedvalue.setText("No data...");
                Barselectedavgvalue.setText("No data...");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        selectweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (typeoftab(tabLayout.getSelectedTabPosition()))
                {
                    case "hour":
                       {
                           AppCompatDialogFragment newFragment = new DatePickerFragment();
                           // set the targetFragment to receive the results, specifying the request code
                           newFragment.setTargetFragment(EnergyFragment.this,EnergyREQUEST_CODE);
                           // show the datePicker
                           newFragment.show(fm, "datePicker");

                       }
                        break;
                    case "month":
                       {
                           String month = monthselector.getSelectedItem().toString();
                           String year = yearselector.getSelectedItem().toString();
                           String date = year+"-"+monthnumber(month)+"-"+"02";

                           createMonthBargraph("Monthly analysis of "+month+" "+year,date);

                       }
                        break;
                    case "year":
                       {

                           //DatePickerDialog dialog = new DatePickerDialog(EnergyFragment.this);
                           int year = Integer.parseInt(yearselector.getSelectedItem().toString());
                           String startdate,endate;


                           if(year == Integer.parseInt(MainActivity.returnapivalue("system start year",getContext())))    ///    for starting year of system
                           {
                               startdate = MainActivity.returnapivalue("system start date",getContext());

                               ArrayList<String> yeardates = yearlydetails.datesofyear(year);


                               endate = yeardates.get(yeardates.size()-2);

                               createyearlygraph("analysis of "+year,startdate,endate,yeardates);

                               //Toast.makeText(getContext(), "starting year of system", Toast.LENGTH_SHORT).show();
                           }
                           else if(year==Calendar.getInstance().get(Calendar.YEAR))       ///      for current year
                           {
                               ArrayList<String> yeardates = yearlydetails.datesofyear(year);

                               startdate = yeardates.get(0);
                               endate = returncurrentdate();

                               createyearlygraph("analysis of "+year,startdate,endate,yeardates);

                               //Toast.makeText(getContext(), "Current year", Toast.LENGTH_SHORT).show();
                           }
                           else      ///     any other year
                           {

                               ArrayList<String> yeardates = yearlydetails.datesofyear(year);

                               startdate = yeardates.get(0);
                               endate = yeardates.get(yeardates.size()-2);

                               createyearlygraph("analysis of "+year,startdate,endate,yeardates);

                               //Toast.makeText(getContext(), "Some other year", Toast.LENGTH_SHORT).show();
                           }

                       }
                        break;
                }

                /*
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(EnergyFragment.this,EnergyREQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
                */

            }
        });


        return v;

    }



    // for number of days of a month in a year
    int numberofdaysofmonth(String monthNUM,int year){

        int month = monthnum(monthNUM);
        int date=1;
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,date);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    int monthnum(String month)
    {
        int monthnum=0;
        switch(month)
        {
            case "Jan":
                monthnum = Calendar.JANUARY;
                break;
            case "Feb":
                monthnum = Calendar.FEBRUARY;
                break;
            case "March":
                monthnum = Calendar.MARCH;
                break;
            case "April":
                monthnum = Calendar.APRIL;
                break;
            case "May":
                monthnum = Calendar.MAY;
                break;
            case "June":
                monthnum = Calendar.JUNE;
                break;
            case "July":
                monthnum = Calendar.JULY;
                break;
            case "Aug":
                monthnum = Calendar.AUGUST;
                break;
            case "Sep":
                monthnum = Calendar.SEPTEMBER;
                break;
            case "Oct":
                monthnum = Calendar.OCTOBER;
                break;
            case "Nov":
                monthnum = Calendar.NOVEMBER;
                break;
            case "Dec":
                monthnum = Calendar.DECEMBER;
                break;
        }
        return monthnum;

    }




    public String monthnumber(String month)
    {
        String monthnum = null;
        switch(month)
        {
            case "Jan":
                monthnum = "01";
                break;
            case "Feb":
                monthnum = "02";
                break;
            case "March":
                monthnum = "03";
                break;
            case "April":
                monthnum = "04";
                break;
            case "May":
                monthnum = "05";
                break;
            case "June":
                monthnum = "06";
                break;
            case "July":
                monthnum = "07";
                break;
            case "Aug":
                monthnum = "08";
                break;
            case "Sep":
                monthnum = "09";
                break;
            case "Oct":
                monthnum = "10";
                break;
            case "Nov":
                monthnum = "11";
                break;
            case "Dec":
                monthnum = "12";
                break;
        }
        return monthnum;
    }

    private String typeoftab(int pos)
    {
        String res="";
        switch (pos)
        {
            case 0:
                res="hour";
                break;
            /*case 0:
                res="week";
                break;*/
            case 1:
                res="month";
                break;

            case 2:
                res="year";
                break;
        }
        return res;
    }

    private void changegraph(int pos)
    {

        switch(pos)
        {
            case 0:
                selectweek.setText("Generate graph for Desired Day");
                selectweek.setVisibility(View.VISIBLE);
                setspinners(false,false);
                break;
            /*case 0:
                selectweek.setText("Select Desired Week");
                selectweek.setVisibility(View.VISIBLE);
                //     createWeekBarGraph("Weekly analysis",returncurrentdate());
                break;*/
            case 1:
                selectweek.setText("Generate graph for Desired Month");
                selectweek.setVisibility(View.VISIBLE);
                setspinners(true,true);
                //     createMonthBargraph("Monthly analysis",returncurrentdate());
                break;

            case 2:
                selectweek.setText("Generate graph for Desired Year");
                selectweek.setVisibility(View.VISIBLE);
                setspinners(false,true);
                break;
            /*case 2:
                createRandomBarGraph("2020/05/01","2020/05/13","Yearly analysis");
                selectweek.setText("Select desired year");
                selectweek.setVisibility(View.VISIBLE);
                break;
            case 3:
                createRandomBarGraph("2020/05/01","2020/05/02","Lifetime");
                selectweek.setVisibility(View.INVISIBLE);
                break;*/
        }
    }




    ///  --------------------------------  hourly bar graph functions  --------------------------------


    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void createhourlygraph(String description, String date)
    {

            barChart.clear();
            ArrayList<String> selecteddayhours = hourlydetails.hoursofdayepochfivemindiff(date);
            String starttime = selecteddayhours.get(0);
            String endtime;
        /*int index = checkwithcurrenttime(selecteddayhours);
        if(index!=-1)
        {
            endtime = selecteddayhours.get(index-1);
        }
        else
        {*/
            endtime = selecteddayhours.get(selecteddayhours.size() - 1);
            //}
            //Toast.makeText(getContext(), "starttime: " + starttime + " || endtime: " + endtime, Toast.LENGTH_LONG).show();
            getHourlyValues(getContext(), description, starttime, endtime);

        //Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();
    }


    private void getHourlyValues(final Context context, String description, final String starttime, final String endtime)
    {
        Toast.makeText(context,"Making a request for values, please wait",Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), "starttime: " + starttime + " || endtime: " + endtime, Toast.LENGTH_LONG).show();
        Call<HourlyValues> hourlyValues = SolarApi.getService().getValuesOfEachHour(MainActivity.returnapivalue("system_id",context),starttime,endtime,"iso8601",MainActivity.returnapivalue("user_id",context),MainActivity.returnapivalue("apikey",context));
        final String descr = description;
        hourlyValues.enqueue(new Callback<HourlyValues>() {
            @Override
            public void onResponse(Call<HourlyValues> call, Response<HourlyValues> response) {
                if(response.body() instanceof HourlyValues)
                {
                    Toast.makeText(context,"Receiving values and creating graph.....",Toast.LENGTH_SHORT).show();

                    totalvalue = 0;

                    HourlyValues values = response.body();

                    //ArrayList<String> HourList = selecteddayhours;
                    ArrayList<String> timelist = new ArrayList<>();

                    barEntries = new ArrayList<>();

                    List<Interval> selecteddayintervals = response.body().getIntervals();

                    for(int i=0;i<selecteddayintervals.size();i++)
                    {
                          /////   display enwh divided by 1000
                        try {
                            float entryvalue = ((float) selecteddayintervals.get(i).getEnwh()) / 1000.0f;
                            totalvalue += entryvalue;
                            barEntries.add(new BarEntry(entryvalue, i));
                            timelist.add(timeofday(selecteddayintervals.get(i).getEndAt()));
                        }
                        catch(Exception e)
                        {
                            Log.println(Log.ASSERT,"Caught exception:",e.getMessage().toString());
                        }
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
                    BarData barData = new BarData(timelist, barDataSet);
                    barChart.setData(barData);
                    barChart.setDescription(descr);
                    barChart.fitScreen();
                    totalpower.setText("No data...");
                    BarselectedY.setText("No date selected...");
                    barselectedvalue.setText("No data...");
                    Barselectedavgvalue.setText("No data...");

                    Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.noresponsedialog_design);

                    TextView noresponsetext = dialog.findViewById(R.id.noresponsetext);
                    noresponsetext.setText("Data does not exist for selected Day...");
                    dialog.show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<HourlyValues> call, Throwable t) {
                Toast.makeText(context,"Error occured, Could not get hourly values",Toast.LENGTH_SHORT).show();
                //Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.noresponsedialog_design);

                TextView noresponsetext = dialog.findViewById(R.id.noresponsetext);
                noresponsetext.setText("Message:"+t.getMessage());
                dialog.show();
            }
        });
    }


    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    int checkwithcurrenttime(ArrayList<String> hoursofday)
    {
        //long currentepochsecond = Instant.now().getEpochSecond();
        long currentepochsecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        int index=-1;
        for(int i=0;i<hoursofday.size();i++)
        {
            if(currentepochsecond<Long.valueOf(hoursofday.get(i)))
            {
                index=i;
                break;
            }
        }
        return index;
    }*/


    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    long converttoepoch(String unsplittime)
    {
        String[] arr = unsplittime.split("\\+");
        String time = arr[0];
        return Instant.parse(time).getEpochSecond();
    }*/


    String timeofday(String time)
    {
        String[] arr1 = time.split("\\+");
        String[] arr2 = arr1[0].split("T");
        return arr2[1];
    }






    ///   --------------------------------   weekly bar graph functions   --------------------------------


    public void createWeekBarGraph(String description,String date)
    {
        barChart.clear();
        ArrayList<String> selectedweek = WeeklyDetails.GetWeekdates(date);
        String startdate = selectedweek.get(0);
        String enddate;
        if(selectedweek.contains(returncurrentdate())){
            enddate = returncurrentdate();
        }
        else {
            enddate = selectedweek.get(selectedweek.size() - 1);
        }
        getWeeklyValues(getContext(),description,startdate,enddate);
        //Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();
    }

    private void getWeeklyValues(final Context context, String description, final String startdate, final String enddate)
    {
        Toast.makeText(context,"Making a request for values, please wait",Toast.LENGTH_SHORT).show();
        Call<WeeklyValues> weeklyValues = SolarApi.getService().getValuesofWeek(MainActivity.returnapivalue("system_id",context),startdate,enddate,MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));
        final String descr = description;
        weeklyValues.enqueue(new Callback<WeeklyValues>() {
            @Override
            public void onResponse(Call<WeeklyValues> call, Response<WeeklyValues> response) {

                if(response.body() instanceof WeeklyValues) {

                    Toast.makeText(context,"Receiving values and creating graph.....",Toast.LENGTH_SHORT).show();

                    totalvalue = 0;

                    WeeklyValues values = response.body();

                    ArrayList<String> weekdays = null;

                    weekdays = WeeklyDetails.GetWeekdates(startdate,enddate);

                    barEntries = new ArrayList<>();
                    List<Integer> valuesofweek = values.getProduction();
                    if(enddate!=returncurrentdate()) {
                        for (int j = 0; j < weekdays.size(); j++) {
                            try {
                                float entryvalue = ((float) valuesofweek.get(j)) / 1000.0f;
                                totalvalue += entryvalue;
                                barEntries.add(new BarEntry(entryvalue, j));
                            }
                            catch(Exception e)
                            {
                                Log.println(Log.ASSERT,"Caught exception:",e.getMessage().toString());
                            }
                        }
                    }
                    else{
                        for (int j = 0; j < weekdays.size()-1; j++) {
                            float entryvalue = ((float) valuesofweek.get(j)) / 1000.0f;
                            totalvalue += entryvalue;
                            barEntries.add(new BarEntry(entryvalue, j));
                        }
                    }
                    BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
                    BarData barData = new BarData(weekdays, barDataSet);
                    barChart.setData(barData);
                    barChart.setDescription(descr);
                    barChart.fitScreen();
                    totalpower.setText("No data...");
                    BarselectedY.setText("No date selected...");
                    barselectedvalue.setText("No data...");
                    Barselectedavgvalue.setText("No data...");

                    Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.noresponsedialog_design);

                    TextView noresponsetext = dialog.findViewById(R.id.noresponsetext);
                    noresponsetext.setText("Data does not exist for selected Week.....");
                    dialog.show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<WeeklyValues> call, Throwable t) {
                Toast.makeText(context,"Error occured, Could not get weekly values",Toast.LENGTH_SHORT).show();
            }
        });
    }



    ///   --------------------------------   monthly bar graph functions   --------------------------------



    public void createMonthBargraph(String description,String date)
    {
        barChart.clear();
        ArrayList<String> selectedMonth = monthlydetails.GetMonthDates(date);
        String startdate = selectedMonth.get(0);
        String enddate;
        if(selectedMonth.contains(returncurrentdate())){
            enddate = returncurrentdate();
        }
        else{
            enddate = selectedMonth.get(selectedMonth.size()-1);
        }

        getMonthlyValues(getContext(),description,startdate,enddate);
        //Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();
    }

    public void getMonthlyValues(final Context context, String description, final String startdate, final String enddate)
    {
        Toast.makeText(context,"Making a request for values, please wait",Toast.LENGTH_SHORT).show();
        final Call<WeeklyValues> monthlyValues = SolarApi.getService().getValuesofWeek(MainActivity.returnapivalue("system_id",context),startdate,enddate,MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));
        final String descr = description;

        monthlyValues.enqueue(new Callback<WeeklyValues>() {
            @Override
            public void onResponse(Call<WeeklyValues> call, Response<WeeklyValues> response) {

                if(response.body() instanceof WeeklyValues) {

                    Toast.makeText(context,"Receiving values and creating graph.....",Toast.LENGTH_SHORT).show();

                    totalvalue = 0;

                    WeeklyValues values = response.body();

                    ArrayList<String> daysOfMonth = null;

                    daysOfMonth = monthlydetails.GetMonthDates(startdate,enddate);

                    barEntries = new ArrayList<>();
                    List<Integer> valuesofweek = values.getProduction();
                    if(enddate!=returncurrentdate()) {                    ///     checking to see if enddate is the current date
                        for (int j = 0; j < daysOfMonth.size(); j++) {
                            try {
                                float entryvalue = ((float) valuesofweek.get(j)) / 1000.0f;
                                totalvalue += entryvalue;
                                barEntries.add(new BarEntry(entryvalue, j));
                            }
                            catch(Exception e)
                            {
                                Log.println(Log.ASSERT,"Caught exception:",e.getMessage().toString());
                            }
                        }
                    }
                    else{
                        for (int j = 0; j < daysOfMonth.size()-1; j++) {
                            float entryvalue = ((float) valuesofweek.get(j)) / 1000.0f;
                            totalvalue += entryvalue;
                            barEntries.add(new BarEntry(entryvalue, j));
                        }
                    }
                    BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
                    BarData barData = new BarData(daysOfMonth, barDataSet);
                    barChart.setData(barData);
                    barChart.setDescription(descr);
                    barChart.fitScreen();
                    totalpower.setText("No data...");
                    BarselectedY.setText("No date selected...");
                    barselectedvalue.setText("No data...");
                    Barselectedavgvalue.setText("No data...");

                    Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.noresponsedialog_design);

                    TextView noresponsetext = dialog.findViewById(R.id.noresponsetext);
                    noresponsetext.setText("Data does not exist for selected month.....");
                    dialog.show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<WeeklyValues> call, Throwable t) {
                Toast.makeText(context,"Error occured, Could not get monthly values",Toast.LENGTH_SHORT).show();
            }
        });

    }






    ///   --------------------------------   yearly bar graph functions   --------------------------------



    public void createyearlygraph(String description,String startdate,String enddate,ArrayList<String> yeardates)
    {
        yeartocheckwith = Integer.parseInt(yearselector.getSelectedItem().toString());
        barChart.clear();
        getyearlyvalues(getContext(),description,startdate,enddate,yeardates);
    }


    public void getyearlyvalues(final Context context, String description, final String startdate, final String enddate, final ArrayList<String> yeardates)
    {
        Toast.makeText(context,"Making a request for values, please wait",Toast.LENGTH_SHORT).show();
        final Call<WeeklyValues> yearlyValues = SolarApi.getService().getValuesofWeek(MainActivity.returnapivalue("system_id",context),startdate,enddate,MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));
        final String descr = description;

        yearlyValues.enqueue(new Callback<WeeklyValues>() {
            @Override
            public void onResponse(Call<WeeklyValues> call, Response<WeeklyValues> response) {

                if(response.body() instanceof WeeklyValues) {

                    Toast.makeText(context,"Receiving values and creating graph.....",Toast.LENGTH_SHORT).show();

                    WeeklyValues values = response.body();

                    totalvalue = 0;

                    List<Integer> valuesforgivenyear = values.getProduction();

                    String[] arrayofmonths = {"Jan","Feb","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};

                    ArrayList<String> months = converttoarrayliststring(arrayofmonths);

                    ArrayList<Float> valuesforeachmonthinyear = converttoarraylistfloat(new float[]{0,0,0,0,0,0,0,0,0,0,0,0});

                    barEntries = new ArrayList<>();


                    float totalformonth=0;

                    int i=0;

                    int j=startingmonth(startdate);

                    if(j!=0)         //// for the starting year in which the preceeding months had no power generation
                    {
                        for (int k = 0; k < j; k++) {
                            barEntries.add(new BarEntry(0, k));
                        }
                    }


                    for (String date : yeardates)         // using yeardates separate the values obtained into each month dividing by ---
                    {


                            if (date == "---") {
                                try {
                                    valuesforeachmonthinyear.set(j, totalformonth);
                                    barEntries.add(new BarEntry(valuesforeachmonthinyear.get(j), j));
                                    j += 1;
                                    totalvalue += totalformonth;
                                    totalformonth = 0;
                                } catch (Exception ex) {
                                    Log.println(Log.ASSERT, "value of j", "j=" + j);
                                }
                            }
                            else {
                                try {
                                    totalformonth += valuesforgivenyear.get(i) / 1000f;
                                } catch (Exception ex) {
                                    Log.println(Log.ASSERT, "value of i", "i=" + i);
                                    Log.println(Log.ASSERT, "during adding values", ex.getMessage());
                                }
                                i += 1;
                            }

                    }



                    BarDataSet barDataSet = new BarDataSet(barEntries, "Months");

                    BarData barData = new BarData(months, barDataSet);


                    barChart.setData(barData);
                    barChart.setDescription(descr);
                    barChart.fitScreen();
                    totalpower.setText("No data...");
                    BarselectedY.setText("No date selected...");
                    barselectedvalue.setText("No data...");
                    Barselectedavgvalue.setText("No data...");

                    Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();


                }
                else{

                    ///   this portion of code should technically never occur but just in case, to check if the object recieved isn't of the required type

                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.noresponsedialog_design);

                    TextView noresponsetext = dialog.findViewById(R.id.noresponsetext);
                    noresponsetext.setText("Data does not exist for selected year.....");
                    dialog.show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<WeeklyValues> call, Throwable t) {
                Toast.makeText(context,"Error occured, Could not get yearly values",Toast.LENGTH_SHORT).show();
            }
        });
    }


    ArrayList<Float> converttoarraylistfloat(float arr[])
    {
        ArrayList<Float> array_list = new ArrayList<Float>();

        for(int i=0;i<arr.length;i++)
        {
            array_list.add(arr[i]);
        }

        return array_list;
    }


    ArrayList<String> converttoarrayliststring(String arr[])
    {
        ArrayList<String> array_list = new ArrayList<String>();

        for(int i=0;i<arr.length;i++)
        {
            array_list.add(arr[i]);
        }

        return array_list;
    }

    int startingmonth(String startingdate)
    {
        Date startdate=null;
        try {
            startdate = new SimpleDateFormat("yyyy-MM-dd").parse(startingdate);
        }
        catch (Exception ex){

        }
        Calendar givenyear = Calendar.getInstance();
        givenyear.setTime(startdate);
        return givenyear.get(Calendar.MONTH);
    }













    ///   --------------------------------   base functions used for creating graphs     --------------------------------



    public void createRandomBarGraph(String date1,String date2,String description)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
            Date Date1 = simpleDateFormat.parse(date1);
            Date Date2 = simpleDateFormat.parse(date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(Date1);
            mDate2.setTime(Date2);

            dates = new ArrayList<>();
            dates = getList(mDate1,mDate2);

            barEntries = new ArrayList<>();
            float max=0f;
            float value=0f;
            random = new Random();
            for(int j=0;j<dates.size();j++)
            {
                max = 100f;
                value = random.nextFloat()*max;
                barEntries.add(new BarEntry(value,j));
            }

        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");
        BarData barData = new BarData(dates,barDataSet);
        barChart.setData(barData);
        barChart.setDescription(description);

    }


    public void createBarGraphWEEK(String description)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        /*try
        {*/
            dates = new ArrayList<>();
            barEntries = new ArrayList<>();
            float max=0f;
            float value=0f;
            random = new Random();
            for(int j=0;j<dates.size();j++)
            {
                max = 100f;
                value = random.nextFloat()*max;
                barEntries.add(new BarEntry(value,j));
            }

        /*}
        catch(ParseException e)
        {
            e.printStackTrace();
        }*/

        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");
        BarData barData = new BarData(dates,barDataSet);
        barChart.setData(barData);
        barChart.setDescription(description);

    }




    public ArrayList<String> getList(Calendar startDate,Calendar endDate)
    {
        ArrayList<String> list = new ArrayList<>();
        while(startDate.compareTo(endDate)<=0)
        {
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld)
    {
        String curdate = cld.get(Calendar.YEAR)+"/"+cld.get(Calendar.MONTH)+"/"+cld.get(Calendar.DAY_OF_MONTH);
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curdate);
            curdate = new SimpleDateFormat("yyyy/MM/dd").format(date);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        return curdate;
    }



    private void power_for_day(final Context context,String date)               //////   for whatever day is passed
    {

        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfToday(MainActivity.returnapivalue("system_id",context),date,MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));
        summaryOfDay.enqueue(new Callback<SummaryOfDay>() {
            @Override
            public void onResponse(Call<SummaryOfDay> call, Response<SummaryOfDay> response) {

                SummaryOfDay today = response.body();
                float todaysenergy = (float)today.getEnergyToday();
                float valueoftoday = todaysenergy/1000f;

                //Integer k = today.getEnergyToday()/1000;
                //Integer r = today.getEnergyToday()%1000;
            }


            @Override
            public void onFailure(Call<SummaryOfDay> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
            }


        });
    }



    /////      --------------------------------    function for spinners    --------------------------------

    void setspinners(boolean monthlyspinneractive,boolean yearlyspinneractive)
    {
        monthselector.setVisibility((monthlyspinneractive)?View.VISIBLE:View.GONE);
        yearselector.setVisibility((yearlyspinneractive)?View.VISIBLE:View.GONE);

        String[] arrayofmonths = {"Jan","Feb","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};

        Calendar calendar=Calendar.getInstance();
        int currentyear = calendar.get(Calendar.YEAR);

        ArrayList<String> listofyears = new ArrayList<>();

        for(int i=2019;i<=currentyear;i++)
        {
            listofyears.add(""+i);
        }

        String[] arrayofyears = new String[listofyears.size()];

        for (int i=0;i<listofyears.size();i++)
        {
            arrayofyears[i]=listofyears.get(i);
        }

        ArrayAdapter yearlyadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arrayofyears);
        yearlyadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearselector.setAdapter(yearlyadapter);

        ArrayAdapter monthlyadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arrayofmonths);
        monthlyadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthselector.setAdapter(monthlyadapter);


        if(monthlyspinneractive)
        {
            monthselector.setVisibility(View.VISIBLE);
        }
        else
        {
            monthselector.setVisibility(View.GONE);
        }
        if(yearlyspinneractive)
        {
            yearselector.setVisibility(View.VISIBLE);
        }
        else
        {
            yearselector.setVisibility(View.GONE);
        }
    }




}
