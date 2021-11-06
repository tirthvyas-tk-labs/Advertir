package com.project.adverstir;

import com.project.adverstir.model.RecentsData;
//import com.travel.travelmate.model.RecentsData;

import com.example.adverstir.R;

import java.util.ArrayList;
import java.util.List;

public class Const {

    public static String SHAREDPREFERENCE = "preference";
    public static final String Mobile = "mobileKey";
    public static final String UserId = "useridKey";
    public static final String Email = "emailKey";
    public static final String Name = "nameKey";

    // Travel information
    // You cannot directly assign in a class, need a method or function
    public static List<RecentsData> recentsDataList = initialize_travel_information();
    // Initialization function
    private static ArrayList<RecentsData> initialize_travel_information()
    {
        ArrayList<RecentsData> array_recentsDataList = new ArrayList<>();
        array_recentsDataList.add(new RecentsData("Agra", "India", "$200",R.drawable.india_location, 3.5f));
        array_recentsDataList.add(new RecentsData("Great Wall of China", "China", "$300", R.drawable.china_location, 4.5f));
        array_recentsDataList.add(new RecentsData("Marina Sand Bay", "Singapore", "$400", R.drawable.singapore_location, 4f));
        array_recentsDataList.add(new RecentsData("KLCC", "Malaysia", "$500", R.drawable.malaysia_location, 5f));
        array_recentsDataList.add(new RecentsData("Bangkok", "Thailand", "$600", R.drawable.thailand_location, 5f));
        return array_recentsDataList;
    }

    /*
    // Original way to do in MainActivity
    List<RecentsData> recentsDataList = new ArrayList<>()
    recentsDataList.add(new RecentsData("Agra", "India", "$200",R.drawable.india_location, 3.5f));
    recentsDataList.add(new RecentsData("Great Wall of China", "China", "$300", R.drawable.china_location, 4.5f));
    recentsDataList.add(new RecentsData("Marina Sand Bay", "Singapore", "$400", R.drawable.singapore_location, 4f));
    recentsDataList.add(new RecentsData("KLCC", "Malaysia", "$500", R.drawable.malaysia_location, 5f));
    recentsDataList.add(new RecentsData("Bangkok", "Thailand", "$600", R.drawable.thailand_location, 5f));
     */
}
