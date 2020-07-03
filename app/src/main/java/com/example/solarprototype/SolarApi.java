package com.example.solarprototype;



import android.content.Context;
import android.content.res.AssetManager;

import com.example.solarprototype.Fragments.StatusFragment;
import com.example.solarprototype.RequestedValues.HourlyValues;
import com.example.solarprototype.RequestedValues.PostData;
import com.example.solarprototype.RequestedValues.WeeklyValues;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class SolarApi {

    /*
    static String Systemid=SplashScreenActivity.returnapivalue("system_id",SplashScreenActivity.SplashScreenContext);
    static String Apikey=SplashScreenActivity.returnapivalue("apikey",SplashScreenActivity.SplashScreenContext);
    static String Userid=SplashScreenActivity.returnapivalue("user_id",SplashScreenActivity.SplashScreenContext);
    static String Url=SplashScreenActivity.returnapivalue("url",SplashScreenActivity.SplashScreenContext);
    */

    /*
    public static final String system_id=Systemid;
    public static final String apikey=Apikey;
    public static final String user_id=Userid;
    public static final String url=Url;
*/




    public static PostService postService = null;

    public static PostService getService()
    {
        if(postService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.returnapivalue("url",MainActivity.MainActivityContext))//(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);

        }
        return postService;
    }



    public interface PostService
    {
        @GET("systems/{system_id}/stats")
        Call<HourlyValues> getValuesOfEachHour(@Path(value = "system_id") String systemid, @Query("start_at") String starttime, @Query("end_at") String endtime, @Query("datetime_format") String format, @Query("user_id") String user_id, @Query("key") String apikey);


        @GET("systems")  //?key="+apikey+"&user_id="+user_id)
        Call<PostData> getPostData(@Query("key") String apikey,@Query("user_id") String user_id);

        @GET("systems/{system_id}/summary")  //@GET("systems/"+system_id+"/summary")
        Call<SummaryOfDay> getSummaryOfToday(@Path(value = "system_id") String systemid, @Query("summary_date") String curdate, @Query("key") String apikey, @Query("user_id") String user_id);


        // Used for both Weekly and monthly values
        @GET("systems/{system_id}/energy_lifetime")  //@GET("systems/"+system_id+"/energy_lifetime")
        Call<WeeklyValues> getValuesofWeek(@Path(value = "system_id") String systemid,@Query("start_date") String startdate, @Query("end_date") String enddate, @Query("key") String apikey, @Query("user_id") String user_id);

    }



}
