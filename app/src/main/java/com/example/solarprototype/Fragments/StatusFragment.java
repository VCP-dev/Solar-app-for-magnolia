package com.example.solarprototype.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.solarprototype.BarChartOperations.frontpagedetails;
import com.example.solarprototype.MainActivity;
import com.example.solarprototype.RequestedValues.PostData;
import com.example.solarprototype.R;
import com.example.solarprototype.RequestedValues.WeeklyValues;
import com.example.solarprototype.SolarApi;
import com.example.solarprototype.SplashScreenActivity;
import com.example.solarprototype.StoredValues;
import com.example.solarprototype.SummaryOfDay;
import com.example.solarprototype.RequestedValues.System;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatusFragment extends Fragment {


    TextView currentdate;
    TextView energyproduced;
    TextView unitsperkwp;
    TextView sysstatus;

    TextView yesterdaydate;
    TextView energyproducedyesterday;
    TextView unitsperkwpyesterday;

    TextView thismonthdate;
    TextView energyproducedthismonth;
    TextView unitsperkwpthismonth;

    TextView lastmonthdate;
    TextView energyproducedlastmonth;
    TextView unitsperkwplastmonth;

    Button getdetails;
    Button changedate;

    FloatingActionButton updatebutton;

    // for current date

    Calendar calendar,calender;
    DateFormat dateFormat,dateformat;
    String date;


    String returnedvaluestatus,returnedvalueenergyproduced,returnedvalueunitsperkwp,returnedenergyproducedyesterday,returnedunitsperkwpyesterday,returnedenergyproducedthismonth,returnedunitsperkwpthismonth,returnedenergyproducedlastmonth,returnedunitsperkwplastmonth;

    Boolean valuespresent;

    ColorStateList oldColors;



    Boolean energyupdatebuttonpressed;



    public static final int REQUEST_CODE = 11;    ///  Used to identify the result

    private OnFragmentInteractionListener mListener;

    public static StatusFragment newInstance() {
        StatusFragment fragment = new StatusFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public StatusFragment()
    {

    }

    public StatusFragment(String status,String energyproducedtoday,String unitsperkwptoday,String energyproducedyesterday,String unitsperkwpyesterday,String energyproducedthismonth,String unitsperkwpthismonth,String energyproducedlastmonth,String unitsperkwplastmonth)
    {
        if(status!=null)
        {
            returnedvaluestatus = status;
            valuespresent = true;
        }
        if(energyproducedtoday!=null)    // today
        {
            returnedvalueenergyproduced = energyproducedtoday;
            valuespresent = true;
        }
        if(unitsperkwptoday!=null)
        {
            returnedvalueunitsperkwp = unitsperkwptoday;
            valuespresent = true;
        }
        if(energyproducedyesterday!=null)    // yesterday
        {
            returnedenergyproducedyesterday = energyproducedyesterday;
            valuespresent = true;
        }
        if(unitsperkwpyesterday!=null)
        {
            returnedunitsperkwpyesterday = unitsperkwpyesterday;
            valuespresent = true;
        }
        if(energyproducedthismonth!=null)     //  this month
        {
            returnedenergyproducedthismonth = energyproducedthismonth;
            valuespresent = true;
        }
        if(unitsperkwpthismonth!=null)
        {
            returnedunitsperkwpthismonth = unitsperkwpthismonth;
            valuespresent = true;
        }
        if(energyproducedlastmonth!=null)     //  last month
        {
            returnedenergyproducedlastmonth = energyproducedlastmonth;
            valuespresent = true;
        }
        if(unitsperkwplastmonth!=null)
        {
            returnedunitsperkwplastmonth = unitsperkwplastmonth;
            valuespresent = true;
        }
        else
        {
            valuespresent = false;
        }
    }


    public String returncurrentdate()
    {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String curdate = dateFormat.format(calendar.getTime());
        return curdate;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            String selectedDate = data.getStringExtra("selectedDate");
            // set the values
            currentdate.setText("Details for  "+selectedDate+" :");
            sysstatus.setText("Retrieving system status....");
            energyproduced.setText("Retrieving details....");
            unitsperkwp.setText("Retrieving details....");
            sysstatus.setTextColor(oldColors);
            energyproduced.setTextColor(oldColors);
            unitsperkwp.setTextColor(oldColors);
            sysstatus.setTextSize(14);
            energyproduced.setTextSize(14);
            unitsperkwp.setTextSize(14);
            average_power_for_selected_day(getContext(),selectedDate);
            getdata(getContext());
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_status,container,false);

        energyupdatebuttonpressed = false;

        currentdate = v.findViewById(R.id.currentdate);
        energyproduced = v.findViewById(R.id.energyproduced);
        unitsperkwp = v.findViewById(R.id.unitsperkwp);
        sysstatus = v.findViewById(R.id.sysstatus);

        yesterdaydate = v.findViewById(R.id.yesterdaydate);
        energyproducedyesterday = v.findViewById(R.id.energyproducedyesterday);
        unitsperkwpyesterday = v.findViewById(R.id.unitsperkwpyesterday);

        thismonthdate = v.findViewById(R.id.thismonth);
        energyproducedthismonth = v.findViewById(R.id.energyproducedthismonth);
        unitsperkwpthismonth = v.findViewById(R.id.unitsperkwpthismonth);

        lastmonthdate = v.findViewById(R.id.lastmonth);
        energyproducedlastmonth = v.findViewById(R.id.energyproducedlastmonth);
        unitsperkwplastmonth = v.findViewById(R.id.unitsperkwplastmonth);

        //getdetails = v.findViewById(R.id.getbutton);
        //changedate = v.findViewById(R.id.datebutton);

        updatebutton = v.findViewById(R.id.floatingupdatebutton);

        calender = Calendar.getInstance();
        dateformat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateformat.format(calender.getTime());
        dateformat = new SimpleDateFormat("yyyy-dd-MM");

        //currentdate.setText("Details for  "+date+" :");
        frontpagedetails.todayandyesterday(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        currentdate.setText("Details for "+frontpagedetails.today+":");
        yesterdaydate.setText("Details for "+frontpagedetails.yesterday+":");
        StoredValues.Todaysdate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        StoredValues.Yesterdaysdate = frontpagedetails.yesterday;

        frontpagedetails.daysoftwomonths(StoredValues.Todaysdate);

        oldColors = energyproduced.getTextColors();

        //updatebutton.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5904A0")));

        updatebutton.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0BA8DA")));

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        if(valuespresent)
        {
            sysstatus.setText(returnedvaluestatus);
            sysstatus.setTextColor(Color.parseColor("#2AE016"));

            energyproduced.setText(returnedvalueenergyproduced);
            energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            energyproduced.setTextColor(Color.parseColor("#000000"));               ///    for today
            unitsperkwp.setText(returnedvalueunitsperkwp);
            unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            unitsperkwp.setTextColor(Color.parseColor("#000000"));


            energyproducedyesterday.setText(returnedenergyproducedyesterday);
            energyproducedyesterday.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            energyproducedyesterday.setTextColor(Color.parseColor("#000000"));               ///    for yesterday
            unitsperkwpyesterday.setText(returnedunitsperkwpyesterday);
            unitsperkwpyesterday.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            unitsperkwpyesterday.setTextColor(Color.parseColor("#000000"));


            energyproducedthismonth.setText(returnedenergyproducedthismonth);
            energyproducedthismonth.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            energyproducedthismonth.setTextColor(Color.parseColor("#000000"));               ///    for this month
            unitsperkwpthismonth.setText(returnedunitsperkwpthismonth);
            unitsperkwpthismonth.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            unitsperkwpthismonth.setTextColor(Color.parseColor("#000000"));


            energyproducedlastmonth.setText(returnedenergyproducedlastmonth);
            energyproducedlastmonth.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            energyproducedlastmonth.setTextColor(Color.parseColor("#000000"));               ///    for last month
            unitsperkwplastmonth.setText(returnedunitsperkwplastmonth);
            unitsperkwplastmonth.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            unitsperkwplastmonth.setTextColor(Color.parseColor("#000000"));

        }



        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(energyupdatebuttonpressed==false)
                {
                    energyupdatebuttonpressed = true;

                    sysstatus.setTextColor(oldColors);
                    sysstatus.setText("Retrieving system status....");



                    energyproduced.setText("Retrieving details....");
                    unitsperkwp.setText("Retrieving details....");
                    energyproduced.setTextColor(oldColors);                         ///  for curent day
                    unitsperkwp.setTextColor(oldColors);
                    energyproduced.setTextSize(14);
                    unitsperkwp.setTextSize(14);



                    energyproducedyesterday.setText("Retrieving details....");
                    unitsperkwpyesterday.setText("Retrieving details....");
                    energyproducedyesterday.setTextColor(oldColors);                          ///  for yesterday
                    unitsperkwpyesterday.setTextColor(oldColors);
                    energyproducedyesterday.setTextSize(14);
                    unitsperkwpyesterday.setTextSize(14);



                    energyproducedthismonth.setText("Retrieving details....");
                    unitsperkwpthismonth.setText("Retrieving details....");
                    energyproducedthismonth.setTextColor(oldColors);                          ///  for this month
                    unitsperkwpthismonth.setTextColor(oldColors);
                    energyproducedthismonth.setTextSize(14);
                    unitsperkwpthismonth.setTextSize(14);



                    energyproducedlastmonth.setText("Retrieving details....");
                    unitsperkwplastmonth.setText("Retrieving details....");
                    energyproducedlastmonth.setTextColor(oldColors);                         ///  for last month
                    unitsperkwplastmonth.setTextColor(oldColors);
                    energyproducedlastmonth.setTextSize(14);
                    unitsperkwplastmonth.setTextSize(14);


                    getvaluesfortwomonths(getContext(),frontpagedetails.firstmonth.get(0),StoredValues.Todaysdate);
                }

            }
        });




        ///   UNCOMMENT THIS FOR THOSE TWO BUTTONS IF THEY WILL BE USED


        /*getdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentdate.setText("Details for "+returncurrentdate()+" :");
                Toast.makeText(getContext(),"Updating energy produced today....",Toast.LENGTH_SHORT).show();
                sysstatus.setText("Retrieving system status....");
                energyproduced.setText("Retrieving details....");
                unitsperkwp.setText("Retrieving details....");
                sysstatus.setTextColor(oldColors);
                energyproduced.setTextColor(oldColors);
                unitsperkwp.setTextColor(oldColors);
                //sysstatus.setTextSize(14);
                energyproduced.setTextSize(14);
                unitsperkwp.setTextSize(14);
                average_power_per_day(getContext());
                getdata(getContext());
            }
        });*/



        ///   UNCOMMENT THIS FOR THOSE TWO BUTTONS IF THEY WILL BE USED



        /*changedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(StatusFragment.this,REQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });*/

        //average_power_per_day(this.getContext());
        //getdata(this.getContext());




        return v;

    }


    public void getvaluesfortwomonths(final Context context,String startdate, String enddate)
    {
        Snackbar.make(getView(),"Updating page with latest data....",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
        final Call<WeeklyValues> monthlyValues = SolarApi.getService().getValuesofWeek(MainActivity.returnapivalue("system_id",context),startdate,enddate,MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));

        monthlyValues.enqueue(new Callback<WeeklyValues>() {
            @Override
            public void onResponse(Call<WeeklyValues> call, Response<WeeklyValues> response) {
                if(response.body() instanceof WeeklyValues)
                {


                    WeeklyValues  ValuesTM = response.body();

                    List<Integer> ValuesofTwoMonths = ValuesTM.getProduction();

                    Integer  sumoflastmonth = 0;
                    for(int i=0;i<frontpagedetails.firstmonth.size();i++)
                    {
                        sumoflastmonth+=(ValuesofTwoMonths.get(i));
                    }
                    Integer lmk = sumoflastmonth/1000;
                    Integer lmr = sumoflastmonth%1000;
                    String lastmonthvaluestring = lmk+"."+lmr;              ////  energy produced last month
                    float lastmonthavgvalue = sumoflastmonth / 49.7f;
                    int lmka = (int)lastmonthavgvalue/1000;
                    int lmra = (int)lastmonthavgvalue%1000;
                    String lastmonthavgvaluestring = lmka+"."+lmra;         ////   units per kwp last month
                    StoredValues.energyproducedlastmonth = lastmonthvaluestring;
                    StoredValues.unitsperkwplastmonth = lastmonthavgvaluestring;



                    Integer sumofthismonth = 0;
                    for(int i=frontpagedetails.firstmonth.size();i<ValuesofTwoMonths.size();i++)
                    {
                        try {
                            sumofthismonth+=(ValuesofTwoMonths.get(i));
                        }
                        catch(Exception ex){
                            Log.println(Log.ASSERT,"Caught exception:",ex.getMessage().toString());
                        }
                    }
                    Integer tmk = sumofthismonth/1000;
                    Integer tmr = sumofthismonth%1000;
                    String thismonthvaluestring = tmk+"."+tmr;              ////   energy produced this month
                    float thismonthavgvalue = sumofthismonth/49.7f;
                    int tmka = (int)thismonthavgvalue/1000;
                    int tmra = (int)thismonthavgvalue%1000;
                    String thismonthavgvaluestring = tmka+"."+tmra;         ////   units per kwp this month
                    StoredValues.energyproducedthismonth = thismonthvaluestring;
                    StoredValues.unitsperkwpthismonth = thismonthavgvaluestring;





                    float todaysvalue =(float) (ValuesofTwoMonths.get(ValuesofTwoMonths.size()-2))/1000.0f;
                    Integer tk = ValuesofTwoMonths.get(ValuesofTwoMonths.size()-2) / 1000;
                    Integer tr = ValuesofTwoMonths.get(ValuesofTwoMonths.size()-2) % 1000;
                    String todaysvaluestring = tk+"."+tr;                                              ////   energy produced today
                    float todayavgvalue = ValuesofTwoMonths.get(ValuesofTwoMonths.size()-2) / 49.7f;
                    int tka = (int)todayavgvalue/1000;
                    int tra = (int)todayavgvalue%1000;
                    String todaysavgvaluestring = tka + "." + tra;                                     ////   units per kwp today
                    StoredValues.energyproducedtoday = todaysvaluestring;
                    StoredValues.unitsperkwptoday = todaysavgvaluestring;





                    float yesterdaysvalue =(float) (ValuesofTwoMonths.get(ValuesofTwoMonths.size()-1))/1000.0f;
                    Integer yk = ValuesofTwoMonths.get(ValuesofTwoMonths.size()-1) / 1000;
                    Integer yr = ValuesofTwoMonths.get(ValuesofTwoMonths.size()-1) % 1000;
                    String yesterdaysvaluestring = yk+"."+yr;                                           ////   energy produced yesterday
                    float yesterdayavgvalue = ValuesofTwoMonths.get(ValuesofTwoMonths.size()-1) / 49.7f;
                    int yka = (int)yesterdayavgvalue/1000;
                    int yra = (int)yesterdayavgvalue%1000;
                    String yesterdaysavgvaluestring = yka + "." + yra;                                  ////   units per kwp yesterday
                    StoredValues.energyproducedyesterday = yesterdaysvaluestring;
                    StoredValues.unitsperkwpyesterday = yesterdaysavgvaluestring;


                    energyproduced.setText(StoredValues.energyproducedtoday);
                    unitsperkwp.setText(StoredValues.unitsperkwptoday);
                    energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    energyproduced.setTextColor(Color.parseColor("#000000"));
                    unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    unitsperkwp.setTextColor(Color.parseColor("#000000"));


                    energyproducedyesterday.setText(StoredValues.energyproducedyesterday);
                    unitsperkwpyesterday.setText(StoredValues.unitsperkwpyesterday);
                    energyproducedyesterday.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    energyproducedyesterday.setTextColor(Color.parseColor("#000000"));
                    unitsperkwpyesterday.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    unitsperkwpyesterday.setTextColor(Color.parseColor("#000000"));


                    energyproducedthismonth.setText(StoredValues.energyproducedthismonth);
                    unitsperkwpthismonth.setText(StoredValues.unitsperkwpthismonth);
                    energyproducedthismonth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    energyproducedthismonth.setTextColor(Color.parseColor("#000000"));
                    unitsperkwpthismonth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    unitsperkwpthismonth.setTextColor(Color.parseColor("#000000"));


                    energyproducedlastmonth.setText(StoredValues.energyproducedlastmonth);
                    unitsperkwplastmonth.setText(StoredValues.unitsperkwplastmonth);
                    energyproducedlastmonth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    energyproducedlastmonth.setTextColor(Color.parseColor("#000000"));
                    unitsperkwplastmonth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    unitsperkwplastmonth.setTextColor(Color.parseColor("#000000"));


                    energyupdatebuttonpressed = false;

                }
                else
                {
                    energyupdatebuttonpressed = false;
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.noresponsedialog_design);

                    TextView noresponsetext = dialog.findViewById(R.id.noresponsetext);
                    noresponsetext.setText("Error, the recieved data is not the right type");
                    dialog.show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<WeeklyValues> call, Throwable t) {
                energyupdatebuttonpressed = false;
                Snackbar.make(getView(),"Error in receiving values please check internet connection",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        });
        getdata(context);
    }



    private void average_power_for_selected_day(final Context context,String date)               //////   for the selected day
    {

        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfToday(MainActivity.returnapivalue("system_id",context),date,MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));
        summaryOfDay.enqueue(new Callback<SummaryOfDay>() {
            @Override
            public void onResponse(Call<SummaryOfDay> call, Response<SummaryOfDay> response) {

                if(response.body() instanceof SummaryOfDay) {

                    SummaryOfDay today = response.body();

                    ArrayList<String> datalist = new ArrayList<String>();

                    Integer k = today.getEnergyToday() / 1000;
                    Integer r = today.getEnergyToday() % 1000;
                    String value = k + "." + r + " kWh";
                    float avgval = today.getEnergyToday() / 49.7f;
                    int ka = (int) avgval / 1000;
                    int ra = (int) avgval % 1000;
                    String avgvalue = ka + "." + ra;
                    datalist.add(/* "Energy produced today is: "+*/ value);
                    datalist.add(/* "Units/kWp is: "+*/ avgvalue);

                    energyproduced.setText(datalist.get(datalist.indexOf(value)));
                    StoredValues.energyproducedtoday = datalist.get(datalist.indexOf(value));
                    unitsperkwp.setText(datalist.get(datalist.indexOf(avgvalue)));
                    StoredValues.unitsperkwptoday = datalist.get(datalist.indexOf(avgvalue));

                    energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    energyproduced.setTextColor(Color.parseColor("#000000"));
                    unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    unitsperkwp.setTextColor(Color.parseColor("#000000"));
                }
                else
                {
                    sysstatus.setText("System status currently unknown...");
                    energyproduced.setText("No data....");
                    unitsperkwp.setText("No data....");
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.noresponsedialog_design);

                    TextView noresponsetext = dialog.findViewById(R.id.noresponsetext);
                    noresponsetext.setText("Data does not exist for selected date.....");
                    dialog.show();
                    return;
                }
            }


            @Override
            public void onFailure(Call<SummaryOfDay> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
                sysstatus.setText("System status currently unknown...");
                energyproduced.setText("No data....");
                unitsperkwp.setText("No data....");
                energyproduced.setTextSize(new Button(getContext()).getTextSize());
                unitsperkwp.setTextSize(new Button(getContext()).getTextSize());
            }


        });
    }




    private void average_power_per_day(final Context context)               //////   for the current day
    {

        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfToday(MainActivity.returnapivalue("system_id",context),returncurrentdate(),MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));
        summaryOfDay.enqueue(new Callback<SummaryOfDay>() {
            @Override
            public void onResponse(Call<SummaryOfDay> call, Response<SummaryOfDay> response) {
                SummaryOfDay today = response.body();

                ArrayList<String> datalist = new ArrayList<String>();

                Integer k = today.getEnergyToday()/1000;
                Integer r = today.getEnergyToday()%1000;
                String value = k+"."+r+" kWh";
                float avgval = today.getEnergyToday()/49.7f;
                int ka = (int)avgval/1000;
                int ra = (int)avgval%1000;
                String avgvalue = ka+"."+ra;
                datalist.add(/* "Energy produced today is: "+*/ value);
                datalist.add(/* "Units/kWp is: "+*/ avgvalue);

                energyproduced.setText(datalist.get(datalist.indexOf(value)));
                StoredValues.energyproducedtoday = datalist.get(datalist.indexOf(value));
                unitsperkwp.setText(datalist.get(datalist.indexOf(avgvalue)));
                StoredValues.unitsperkwptoday = datalist.get(datalist.indexOf(avgvalue));

                energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                energyproduced.setTextColor(Color.parseColor("#000000"));
                unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                unitsperkwp.setTextColor(Color.parseColor("#000000"));

            }


            @Override
            public void onFailure(Call<SummaryOfDay> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
                sysstatus.setText("System status currently unknown...");
                energyproduced.setText("No data....");
                unitsperkwp.setText("No data....");
            }


        });
    }




    private void getdata(final Context context)                  //////  for general details
    {
        Call<PostData> postData = SolarApi.getService().getPostData(MainActivity.returnapivalue("apikey",context),MainActivity.returnapivalue("user_id",context));
        postData.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                PostData data = response.body();

                List<System> systemlist = data.getSystems();

                ArrayList<String> datalist = new ArrayList<String>();

                String currentstatus=null;

                for(System s:systemlist)
                {
                    currentstatus = s.getStatus();
                    datalist.add("System status: "+s.getStatus());
                }

                if(currentstatus.contentEquals("normal"))
                {
                    sysstatus.setTextColor(Color.parseColor("#2AE016"));
                }

                sysstatus.setText(datalist.get(0));
                StoredValues.SystemStatus = datalist.get(0);
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
            }
        });
    }




    //////  for api values



}
