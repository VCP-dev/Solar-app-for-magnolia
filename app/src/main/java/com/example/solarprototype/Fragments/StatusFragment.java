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

import com.example.solarprototype.MainActivity;
import com.example.solarprototype.RequestedValues.PostData;
import com.example.solarprototype.R;
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

    Button getdetails;
    Button changedate;

    FloatingActionButton updatebutton;

    // for current date

    Calendar calendar,calender;
    DateFormat dateFormat,dateformat;
    String date;


    String returnedvaluestatus,returnedvalueenergyproduced,returnedvalueunitsperkwp;

    Boolean valuespresent;

    ColorStateList oldColors;



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


        currentdate = v.findViewById(R.id.currentdate);
        energyproduced = v.findViewById(R.id.energyproduced);
        unitsperkwp = v.findViewById(R.id.unitsperkwp);
        sysstatus = v.findViewById(R.id.sysstatus);

        //getdetails = v.findViewById(R.id.getbutton);
        //changedate = v.findViewById(R.id.datebutton);

        updatebutton = v.findViewById(R.id.floatingupdatebutton);

        calender = Calendar.getInstance();
        dateformat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateformat.format(calender.getTime());
        dateformat = new SimpleDateFormat("yyyy-dd-MM");
        StoredValues.Todaysdate = dateformat.format(calender.getTime());
        currentdate.setText("Details for  "+date+" :");

        oldColors = energyproduced.getTextColors();

        updatebutton.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5904A0")));

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

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



        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Update button pressed",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
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

                    energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
                    energyproduced.setTextColor(Color.parseColor("#000000"));
                    unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
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

                energyproduced.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                energyproduced.setTextColor(Color.parseColor("#000000"));
                unitsperkwp.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
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
