package com.example.patrick.groceryapplication.utils;

/**
 * Created by Nomis on 8/3/2017.
 */

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import com.example.patrick.groceryapplication.models.BarCodeItems;
public class NetworkUtils {
    //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=
    //9afb25d9435f4779934d24d24cb2cc33
    public static final String BASE_URL = "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest";
    public static final String TAG = "NetworkUtils";
    public static final String apikey = "0f0cb14a14b7134d22586414523c975d";
    //public String itemNumber = ;

    //URL :http://api.upcdatabase.org/json/0f0cb14a14b7134d22586414523c975d/0111222333446
    //Api Key:0f0cb14a14b7134d22586414523c975d
    //Item#:0111222333446

    public static URL makeUrl(){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(apikey)
                //.appendPath(itemNumber)
                .build();


        URL url = null;
        try{
            String urlString = uri.toString();
            Log.d(TAG, "Url: " + urlString);
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    //parse the data and put into a object
    public static ArrayList<BarCodeItems> parseJson(String json) throws JSONException{
        ArrayList<BarCodeItems> parsedData = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("items");

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String number = item.getString("number");
            String itemname = item.getString("itemname");
            String description = item.getString("description");
            String avg_price = item.getString("url");

            //NewsItem news = new NewsItem(author, title, description, url,urlToImage, publishedAt);
            parsedData.add(new BarCodeItems(number,itemname,description,avg_price));
        }
        return parsedData;
    }
}
