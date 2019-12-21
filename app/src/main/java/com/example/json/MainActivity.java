package com.example.json;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class DownloadContent extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data =  reader.read();
                while(data != -1)
                {
                    char temp = (char)data;
                    result += temp;
                    data = reader.read();
                }
                return result;
            }catch (Exception e) {
                e.printStackTrace();
                //return "Failed";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
//               extract information using Jason object.
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather Info", weatherInfo);

//                 convert it into json array from weather info (it has multiple id and information
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for(int i = 0 ; i<jsonArray.length();i++)
                {
//                    each jason object
                    JSONObject jsonpart = jsonArray.getJSONObject(i);
                    String myMain= jsonpart.getString("main");
                    String desc = jsonpart.getString("description");
                    Log.i("TAG", myMain);
                    Log.i("TAG", desc);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Website content" , result);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadContent downloadContent = new DownloadContent();
        try {
            downloadContent.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
            //Log.i("Output", Result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
