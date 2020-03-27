package com.example.solarprototype;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.solarprototype.BarChartOperations.WeeklyDetails;
import com.example.solarprototype.RequestedValues.PostData;
import com.example.solarprototype.RequestedValues.System;
import com.example.solarprototype.RequestedValues.WeeklyValues;

import java.util.ArrayList;                        ////////   Class used only as example
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIrequests {


    private void average_power_per_day(final Context context)               //////   for the current day
    {

        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfToday("current date","api key","user id");
        summaryOfDay.enqueue(new Callback<SummaryOfDay>() {
            @Override
            public void onResponse(Call<SummaryOfDay> call, Response<SummaryOfDay> response) {
                SummaryOfDay today = response.body();

                ArrayList<String> datalist = new ArrayList<String>();

                Integer k = today.getEnergyToday()/1000;
                Integer r = today.getEnergyToday()%1000;
                String value = k+"."+r+" Kwh";
                float avgval = today.getEnergyToday()/49.7f;
                int ka = (int)avgval/1000;
                int ra = (int)avgval%1000;
                String avgvalue = ka+"."+r;
                datalist.add(/* "Energy produced today is: "+*/ value);
                datalist.add(/* "Units/kWp is: "+*/ avgvalue);


         /*
                for(int i=0;i<datalist.size();i++)
                {
                    Data[i]=datalist.get(i);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,android.R.id.text1,Data);

                //listView.setAdapter(adapter);


         */
            }


            @Override
            public void onFailure(Call<SummaryOfDay> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
            }


        });
    }




    private void getWeeklyValues(final Context context)
    {
        Call<WeeklyValues> weeklyValues = SolarApi.getService().getValuesofWeek("start date","end date","api key","uer id");
        weeklyValues.enqueue(new Callback<WeeklyValues>() {
            @Override
            public void onResponse(Call<WeeklyValues> call, Response<WeeklyValues> response) {
                WeeklyValues values = response.body();

                List<Integer> valuesofweek = values.getProduction();
            }

            @Override
            public void onFailure(Call<WeeklyValues> call, Throwable t) {
                Toast.makeText(context,"Error occured Could not get weekly values",Toast.LENGTH_SHORT).show();
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

                for(System s:systemlist)
                {
                    datalist.add("System ID: "+s.getSystemId());
                    datalist.add("System Name: "+s.getSystemName());
                    datalist.add("System Public Name: "+s.getSystemPublicName());
                    datalist.add("Status: "+s.getStatus());
                    datalist.add("Timezone: "+s.getTimezone());
                    datalist.add("Country: "+s.getCountry());
                    datalist.add("State: "+s.getState());
                    datalist.add("City: "+s.getCity());
                    datalist.add("Postal code: "+s.getPostalCode());
                }

                String[] Data = new String[datalist.size()];

                for(int i=0;i<datalist.size();i++)
                {
                    Data[i]=datalist.get(i);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,android.R.id.text1,Data);

                //listview.setAdapter(adapter);

                ////   RETURN THE ADAPTER FOR THE LISTVIEW HERE

            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
