package com.example.solarprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.solarprototype.Fragments.EnergyFragment;
import com.example.solarprototype.Fragments.MenuFragment;
import com.example.solarprototype.Fragments.StatusFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<String> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containor,new StatusFragment(StoredValues.SystemStatus,StoredValues.energyproducedtoday,StoredValues.unitsperkwptoday)).commit();

        //listview = findViewById(R.id.Solarlistview);
        //getdata();
        //average_power_per_day();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedfragment=null;

                    switch(menuItem.getItemId())
                    {
                        case R.id.nav_status:
                            selectedfragment = new StatusFragment(StoredValues.SystemStatus,StoredValues.energyproducedtoday,StoredValues.unitsperkwptoday);
                            break;
                        case R.id.nav_energy:
                            selectedfragment = new EnergyFragment();
                            break;
                        case R.id.nav_menu:
                            selectedfragment = new MenuFragment(StoredValues.systemname,StoredValues.systemID,StoredValues.systempublicname,StoredValues.timezone,StoredValues.country,StoredValues.state,StoredValues.city,StoredValues.Postalcode);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containor,selectedfragment).commit();

                    return true;
                }
            };



/*
    private void average_power_per_day()               //////   for one day
    {
        Call<SummaryOfDay> summaryOfDay = SolarApi.getService().getSummaryOfDay();
        summaryOfDay.enqueue(new Callback<SummaryOfDay>() {
            @Override
            public void onResponse(Call<SummaryOfDay> call, Response<SummaryOfDay> response) {
                SummaryOfDay today = response.body();

                datalist = new ArrayList<String>();

                Integer k = today.getEnergyToday()/1000;
                Integer r = today.getEnergyToday()%1000;
                String value = k+"."+r+" Kwh";
                float avgval = today.getEnergyToday()/49.7f;
                int ka = (int)avgval/1000;
                int ra = (int)avgval%1000;
                String avgvalue = ka+"."+r;
                datalist.add("Energy produced today is: "+value);
                datalist.add("Units/kWp is: "+avgvalue);

                String[] Data = new String[datalist.size()];

                for(int i=0;i<datalist.size();i++)
                {
                    Data[i]=datalist.get(i);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,Data);

                listview.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<SummaryOfDay> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
            }
        });
    }
*/



/*

    private void getdata()                  //////  for general details
    {
        Call<PostData> postData = SolarApi.getService().getPostData();
        postData.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                PostData data = response.body();

                List<System> systemlist = data.getSystems();

                datalist = new ArrayList<String>();

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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,Data);

                listview.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
            }
        });
    }

 */



}