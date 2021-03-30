package com.example.fire_detection;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
    GridView mGridView;
    ArrayList<String> dblist;
    ArrayAdapter<String> mAdapter;
    Context thisContext;
    String time, temp, humidity;

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
        mGridView = (GridView)v.findViewById(R.id.dataview);
        dblist = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(thisContext, android.R.layout.simple_spinner_item, dblist);
        try{
            Cursor w = db.getData();
            if(w.moveToFirst()){
                do{
                    time = w.getString(w.getColumnIndex("time"));
                    temp = w.getString(w.getColumnIndex("temperature"));
                    humidity = w.getString(w.getColumnIndex("humidity"));
                    dblist.add(time);
                    dblist.add(temp);
                    dblist.add(humidity);
                    mGridView.setAdapter(mAdapter);
                }while(w.moveToNext());
            }else{
                Toast.makeText(thisContext, "no data found", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(thisContext, "no data found"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return v;

    }



}