package com.example.solarprototype.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.solarprototype.BarChartOperations.WeeklyDetails;
import com.example.solarprototype.BarChartOperations.monthlydetails;
import com.example.solarprototype.R;
import com.example.solarprototype.RequestedValues.WeeklyValues;
import com.example.solarprototype.SolarApi;
import com.example.solarprototype.StoredValues;
import com.example.solarprototype.SummaryOfDay;
import com.github.mikephil.charting.charts.BarChart;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == EnergyREQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            String selectedDate = data.getStringExtra("selectedDate");
            // set the values
            switch(selectweek.getText().toString())
            {
                case "Select Desired Week":
                    createWeekBarGraph("Weekly analysis",selectedDate);
                    break;
                case "Select Desired Month":
                    createMonthBargraph("Monthly analysis",selectedDate);
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

        barselectedvalue = v.findViewById(R.id.barselectedvalue);
        Barselectedavgvalue = v.findViewById(R.id.barselectedavgvalue);
        BarselectedY = v.findViewById(R.id.barselectedY);

        barChart = v.findViewById(R.id.barchart);
        barChart.setScaleEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        //changegraph(0);
        //createWeekBarGraph("Weekly analysis","2020-03-19");

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                BarselectedY.setText("Data for "+barChart.getXAxis().getValues().get(e.getXIndex())+":");

                final String selectedValue=String.valueOf(e.getVal());
                barselectedvalue.setText("Power generated : "+selectedValue);

                float avgvalue = Float.parseFloat(selectedValue)/49.7f;
                Barselectedavgvalue.setText("Units/kWP : "+avgvalue);

            }

            @Override
            public void onNothingSelected() {
                BarselectedY.setText("");
                barselectedvalue.setText("");
                Barselectedavgvalue.setText("");
            }
        });


        tabLayout = v.findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#ffffff"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                barChart.clear();
                changegraph(tab.getPosition());
                BarselectedY.setText("");
                barselectedvalue.setText("");
                Barselectedavgvalue.setText("");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        selectweek = v.findViewById(R.id.setweekbutton);
        selectweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(EnergyFragment.this,EnergyREQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");

            }
        });


        return v;

    }

    private void changegraph(int pos)
    {
        switch(pos)
        {
            case 0:
                //createRandomBarGraph("2020/05/01","2020/05/07","Weekly analysis");
                //createWeekBarGraph("Weekly analysis","2020-03-19");
                selectweek.setText("Select Desired Week");
                selectweek.setVisibility(View.VISIBLE);
                break;
            case 1:
                //createRandomBarGraph("2020/05/01","2020/06/01","Monthly analysis");
                selectweek.setText("Select Desired Month");
                selectweek.setVisibility(View.VISIBLE);
                break;
            case 2:
                createRandomBarGraph("2020/05/01","2020/05/13","Yearly analysis");
                selectweek.setText("Select desired year");
                selectweek.setVisibility(View.VISIBLE);
                break;
            case 3:
                createRandomBarGraph("2020/05/01","2020/05/02","Lifetime");
                selectweek.setVisibility(View.INVISIBLE);
                break;
        }
    }


    public void createWeekBarGraph(String description,String date)
    {
        barChart.clear();
        ArrayList<String> selectedweek = WeeklyDetails.GetWeekdates(date);
        String startdate = selectedweek.get(0);
        String enddate = selectedweek.get(selectedweek.size()-1);
        getWeeklyValues(getContext(),description,startdate,enddate);
        Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();
    }

    private void getWeeklyValues(final Context context, String description, final String startdate, String enddate)
    {
        Toast.makeText(context,"Receiving values and creating graph.....",Toast.LENGTH_SHORT).show();
        Call<WeeklyValues> weeklyValues = SolarApi.getService().getValuesofWeek(startdate,enddate,SolarApi.apikey,SolarApi.user_id);
        final String descr = description;
        weeklyValues.enqueue(new Callback<WeeklyValues>() {
            @Override
            public void onResponse(Call<WeeklyValues> call, Response<WeeklyValues> response) {

                if(response.body() instanceof WeeklyValues) {

                    WeeklyValues values = response.body();

                    ArrayList<String> weekdays = null;

                    weekdays = WeeklyDetails.GetWeekdates(startdate);

                    barEntries = new ArrayList<>();
                    List<Integer> valuesofweek = values.getProduction();
                    for (int j = 0; j < weekdays.size(); j++) {
                        float entryvalue = ((float) valuesofweek.get(j)) / 1000.0f;
                        barEntries.add(new BarEntry(entryvalue, j));
                    }
                    BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
                    BarData barData = new BarData(weekdays, barDataSet);
                    barChart.setData(barData);
                    barChart.setDescription(descr);
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


    public void createMonthBargraph(String description,String date)
    {
        barChart.clear();
        ArrayList<String> selectedMonth = monthlydetails.GetMonthDates(date);
        String startdate = selectedMonth.get(0);
        String enddate = selectedMonth.get(selectedMonth.size()-1);
        getMonthlyValues(getContext(),description,startdate,enddate);
        Toast.makeText(getContext(),"Bar graph created, Please tap the display if not visible",Toast.LENGTH_SHORT).show();
    }

    public void getMonthlyValues(final Context context, String description, final String startdate, String enddate)
    {
        Toast.makeText(context,"Receiving values and creating graph.....",Toast.LENGTH_SHORT).show();
        final Call<WeeklyValues> monthlyValues = SolarApi.getService().getValuesofWeek(startdate,enddate,SolarApi.apikey,SolarApi.user_id);
        final String descr = description;

        monthlyValues.enqueue(new Callback<WeeklyValues>() {
            @Override
            public void onResponse(Call<WeeklyValues> call, Response<WeeklyValues> response) {

                if(response.body() instanceof WeeklyValues) {

                    WeeklyValues values = response.body();

                    ArrayList<String> daysOfMonth = null;

                    daysOfMonth = monthlydetails.GetMonthDates(startdate);

                    barEntries = new ArrayList<>();
                    List<Integer> valuesofweek = values.getProduction();
                    for (int j = 0; j < daysOfMonth.size(); j++) {
                        float entryvalue = ((float) valuesofweek.get(j)) / 1000.0f;
                        barEntries.add(new BarEntry(entryvalue, j));
                    }
                    BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
                    BarData barData = new BarData(daysOfMonth, barDataSet);
                    barChart.setData(barData);
                    barChart.setDescription(descr);
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

        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfToday(date,SolarApi.apikey,SolarApi.user_id);
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



}
