
package com.example.fire_detection;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HttpURLConnection conn = null;
    TextView currDate = null;
    TextView currTemp = null;
    TextView currHum = null;

    TextView temp1 = null;
    TextView temp2 = null;
    TextView hum1 = null;
    TextView hum2 = null;

    TextView time1 = null;
    TextView time2 = null;
    TextView fireProbability=null;
    DatabaseHelper db;
    Context thisContext;

//    for refreshing method invoke
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        thisContext = container.getContext();
        db = new DatabaseHelper(thisContext, null, null, 1);
        currDate = (TextView)v.findViewById(R.id.dateText);
        currTemp = (TextView)v.findViewById(R.id.dispTemp);
        currHum = (TextView)v.findViewById(R.id.humidityText);


        temp1 = (TextView)v.findViewById(R.id.temp1);
        temp2 = (TextView)v.findViewById(R.id.temp2);
        hum1 = (TextView)v.findViewById(R.id.hum1);
        hum2 = (TextView)v.findViewById(R.id.hum2);
        time1 = (TextView)v.findViewById(R.id.time1);
        time2 = (TextView)v.findViewById(R.id.time2);

        fireProbability = (TextView)v.findViewById(R.id.fireprob);

        fireProbability.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFire(s.toString());

            }
        });

        String dateTime = currDateTime();
        currDate.setText(dateTime);

        // Inflate the layout for this fragment
        return v;

    }

    @Override
    public void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                parseData();
                Log.d("refreshLog", "Code gets here");
            }
        }, delay);
        super.onResume();
    }

    @Override
    public void onPause() {
//        stop handler when activity is not visible
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    public void checkFire(String h){

        Log.d("fireprob", h);

        double fireProb = Double.parseDouble(h);
        if(fireProb >= 0.75){
            final Dialog dialog = new Dialog(thisContext);
            dialog.setContentView(R.layout.fire_alert);
            Button dismissButton = dialog.findViewById(R.id.dismiss_btn);
            dismissButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Toast.makeText(thisContext.getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }

    }

    public String currDateTime(){

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return formatter.format(date);

    }

    public void parseData(){
        JSONTask myTask = new JSONTask();
        myTask.execute( "https://api.thingspeak.com/channels/1219334/feeds.json?api_key=N6Z53E97Y26LIYTK&results=2");
    }


    class JSONTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.connect();



                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = "";
                    while((line = bufferedReader.readLine()) != null){
                        stringBuffer.append(line);
                    }
                    return stringBuffer.toString();
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            String jsonString = s.toString();


                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        JSONArray jsonArray = jsonObject.getJSONArray("feeds");


                        JSONObject firstObj = jsonArray.getJSONObject(1);
                        String time = firstObj.getString("created_at");
                        String id = firstObj.getString("entry_id");
                        String temp = firstObj.getString("field1");
                        String pressure = firstObj.getString("field3");
                        String humidity = firstObj.getString("field2");
                        double fireprob = firstObj.getDouble("field4");

                        JSONObject secondObj = jsonArray.getJSONObject(0);
                        String time02 = secondObj.getString("created_at");
                        String id2 = secondObj.getString("entry_id");
                        String temp02 = secondObj.getString("field1");
                        String pressure2 = secondObj.getString("field3");
                        String humidity2 = secondObj.getString("field2");

                        String fProb = Double.toString(fireprob);

                        boolean addData = db.addData(id, time, temp, humidity, fProb);
                        if(addData){
                            Toast.makeText(thisContext, "added data to database", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(thisContext, "unable to add data", Toast.LENGTH_LONG).show();
                        }

                        currTemp.setText(temp);
                        currHum.setText(humidity);
                        fireProbability.setText(fProb);



                        time1.setText(time);
                        temp1.setText(temp);

                        hum1.setText(humidity);

                        time2.setText(time02);
                        temp2.setText(temp02);

                        hum2.setText(humidity2);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

        }

    }

}