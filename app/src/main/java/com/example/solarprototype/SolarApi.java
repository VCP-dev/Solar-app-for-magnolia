package com.example.solarprototype;



import com.example.solarprototype.RequestedValues.PostData;
import com.example.solarprototype.RequestedValues.WeeklyValues;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SolarApi {

    public static final String apikey="a458e9f43bad8aaec8121015f8b67b0f";
    public static final String user_id="4d5467324e446b304d773d3d0a";
    public static final String url="https://api.enphaseenergy.com/api/v2/";




    public static PostService postService = null;

    public static PostService getService()
    {
        if(postService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);

        }
        return postService;
    }



    public interface PostService
    {
        @GET("systems?key="+apikey+"&user_id="+user_id)
        Call<PostData> getPostData();

        @GET("systems/1685487/summary")
        Call<SummaryOfDay> getSummaryOfToday(@Query("summary_date") String curdate,@Query("key") String apikey,@Query("user_id") String user_id);

        @GET("systems/1685487/energy_lifetime")
        Call<WeeklyValues> getValuesofWeek(@Query("start_date") String startdate, @Query("end_date") String enddate, @Query("key") String apikey, @Query("user_id") String user_id);

    }



}
