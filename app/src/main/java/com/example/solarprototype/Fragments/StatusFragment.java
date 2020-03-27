package com.example.solarprototype.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.solarprototype.RequestedValues.PostData;
import com.example.solarprototype.R;
import com.example.solarprototype.SolarApi;
import com.example.solarprototype.StoredValues;
import com.example.solarprototype.SummaryOfDay;
import com.example.solarprototype.RequestedValues.System;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatusFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    TextView currentdate;
    TextView energyproduced;
    TextView unitsperkwp;
    TextView sysstatus;

    Button getdetails;
    Button changedate;

    // for current date

    Calendar calendar,calender;
    DateFormat dateFormat,dateformat;
    String date;


    String returnedvaluestatus,returnedvalueenergyproduced,returnedvalueunitsperkwp;

    Boolean valuespresent;

    public StatusFragment(String status,String energyproducedtoday,String unitsperkwptoday)
    {
        if(status!=null)
        {
            returnedvaluestatus = status;
            valuespresent = true;
        }
        if(energyproducedtoday!=null)
        {
            returnedvalueenergyproduced = energyproducedtoday;
            valuespresent = true;
        }
        if(unitsperkwptoday!=null)
        {
            returnedvalueunitsperkwp = unitsperkwptoday;
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String selecteddate = dateFormat.format(calendar.getTime());
        currentdate.setText("Details for  "+selecteddate);
        sysstatus.setText("Retrieving system status....");
        energyproduced.setText("Retrieving details....");
        unitsperkwp.setText("Retrieving details....");
        average_power_for_selected_day(getContext(),selecteddate);
        getdata(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_status,container,false);

        currentdate = v.findViewById(R.id.currentdate);
        energyproduced = v.findViewById(R.id.energyproduced);
        unitsperkwp = v.findViewById(R.id.unitsperkwp);
        sysstatus = v.findViewById(R.id.sysstatus);

        getdetails = v.findViewById(R.id.getbutton);
        changedate = v.findViewById(R.id.datebutton);

        calender = Calendar.getInstance();
        dateformat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateformat.format(calender.getTime());
        dateformat = new SimpleDateFormat("yyyy-dd-MM");
        StoredValues.Todaysdate = dateformat.format(calender.getTime());
        currentdate.setText("Details for  "+date);

        if(valuespresent)
        {
            sysstatus.setText(returnedvaluestatus);
            sysstatus.setTextColor(Color.parseColor("#2AE016"));
            energyproduced.setText(returnedvalueenergyproduced);
            energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
            energyproduced.setTextColor(Color.parseColor("#000000"));
            unitsperkwp.setText(returnedvalueunitsperkwp);
            unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
            unitsperkwp.setTextColor(Color.parseColor("#000000"));
        }

        getdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Updating energy produced today....",Toast.LENGTH_SHORT).show();
                sysstatus.setText("Retrieving system status....");
                energyproduced.setText("Retrieving details....");
                unitsperkwp.setText("Retrieving details....");
                average_power_per_day(getContext());
                getdata(getContext());
            }
        });


        changedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(),"Date Picker");
            }
        });

        //average_power_per_day(this.getContext());
        //getdata(this.getContext());

        return v;

    }



    private void average_power_for_selected_day(final Context context,String date)               //////   for the selected day
    {

        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfToday(date,SolarApi.apikey,SolarApi.user_id);
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
                String avgvalue = ka+"."+r;
                datalist.add(/* "Energy produced today is: "+*/ value);
                datalist.add(/* "Units/kWp is: "+*/ avgvalue);

                energyproduced.setText(datalist.get(datalist.indexOf(value)));
                StoredValues.energyproducedtoday = datalist.get(datalist.indexOf(value));
                unitsperkwp.setText(datalist.get(datalist.indexOf(avgvalue)));
                StoredValues.unitsperkwptoday = datalist.get(datalist.indexOf(avgvalue));

                energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                energyproduced.setTextColor(Color.parseColor("#000000"));
                unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                unitsperkwp.setTextColor(Color.parseColor("#000000"));

            }


            @Override
            public void onFailure(Call<SummaryOfDay> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
            }


        });
    }




    private void average_power_per_day(final Context context)               //////   for the current day
    {

        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfToday(returncurrentdate(),SolarApi.apikey,SolarApi.user_id);
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
                String avgvalue = ka+"."+r;
                datalist.add(/* "Energy produced today is: "+*/ value);
                datalist.add(/* "Units/kWp is: "+*/ avgvalue);

                energyproduced.setText(datalist.get(datalist.indexOf(value)));
                StoredValues.energyproducedtoday = datalist.get(datalist.indexOf(value));
                unitsperkwp.setText(datalist.get(datalist.indexOf(avgvalue)));
                StoredValues.unitsperkwptoday = datalist.get(datalist.indexOf(avgvalue));

                energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                energyproduced.setTextColor(Color.parseColor("#000000"));
                unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                unitsperkwp.setTextColor(Color.parseColor("#000000"));

            }


            @Override
            public void onFailure(Call<SummaryOfDay> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
            }


        });
    }




    private void getdata(final Context context)                  //////  for general details
    {
        Call<PostData> postData = SolarApi.getService().getPostData();
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



}
