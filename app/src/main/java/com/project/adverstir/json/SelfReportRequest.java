package com.project.adverstir.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.adverstir.comms.NetworkConstant;

public class SelfReportRequest {
    BlueToothSeed[] seeds;
    Region region;

    public static JSONObject toJson(String[] seeds, long[] ts_start, long[] ts_end, String uuid, double lat, double longi, int precision) throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        for (int i = 0; i < seeds.length; i++) {
            arr.put(BlueToothSeed.toJson(seeds[i],ts_start[i],ts_end[i]));
        }
        obj.put("uuid",uuid);
        obj.put("seeds",arr);
        obj.put("region",Region.toJson(lat,longi,precision));
        return obj;
    }

    public static String toHttpString() {
        return "https://adverstir-yogeshvar.vercel.app/submit";
    }
}
