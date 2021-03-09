package com.example.fire_detection;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Previous_data#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Previous_data extends Fragment {
    DatabaseHelper db;
    TextView listTime;
    TextView listTemp;
    TextView listHumidity;
    Context thisContext;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Previous_data() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment previous_data.
     */
    // TODO: Rename and change types and number of parameters
    public static Previous_data newInstance(String param1, String param2) {
        Previous_data fragment = new Previous_data();
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

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_previous_data, container, false);
        thisContext = container.getContext();
        db = new DatabaseHelper(thisContext, null, null, 1);
        listTime = (TextView) v.findViewById(R.id.dbtime);
        listTemp = (TextView) v.findViewById(R.id.dbtemp);
        listHumidity = (TextView) v.findViewById(R.id.dbhum);

        viewTime();
        viewTemp();
        viewHum();

        return v;
    }

    private void viewHum() {
        ArrayList<Integer> dbList = new ArrayList<>();
        Cursor humData = db.getHumidity();
        if (humData.getCount() == 0) {
            Toast.makeText(thisContext, "database is empty", Toast.LENGTH_LONG).show();
        }
        else{
            if(humData.moveToFirst()){
                do{
                    dbList.add(humData.getColumnIndex("humidity"));
                }while(humData.moveToNext());
                humData.close();
            }
//            while(humData.moveToNext()){
//                dbList.add(humData.getString(0));
//            }
        }

        for(int t : dbList){
            String s = Integer.toString(t);
            listHumidity.setText(s);
        }
    }

    public void viewTime(){
        ArrayList<Integer> dbList = new ArrayList<>();
        Cursor timeData = db.getTime();
        if (timeData.getCount() == 0) {
            Toast.makeText(thisContext, "database is empty", Toast.LENGTH_LONG).show();
        }
        else{
            if(timeData.moveToFirst()){
                do{
                    dbList.add(timeData.getColumnIndex("time"));
                }while(timeData.moveToNext());
                timeData.close();
            }
//            while(timeData.moveToNext()){
//                dbList.add(timeData.getString(0));
//            }
        }

        for(int t : dbList){
            String s = Integer.toString(t);
            listTime.setText(s);
        }
    }

    public void viewTemp(){
        ArrayList<Integer> dbList = new ArrayList<>();
        Cursor tempData = db.getTemp();
        if (tempData.getCount() == 0) {
            Toast.makeText(thisContext, "database is empty", Toast.LENGTH_LONG).show();
        }
        else{
            if(tempData.moveToFirst()){
                do{
                    dbList.add(tempData.getColumnIndex("temperature"));
                }while(tempData.moveToNext());
                tempData.close();
            }
//            while(tempData.moveToNext()){
//                dbList.add(tempData.getString(0));
//            }
        }

        for(int t : dbList){
            String s = Integer.toString(t);
            listTemp.setText(s);
        }
    }


}