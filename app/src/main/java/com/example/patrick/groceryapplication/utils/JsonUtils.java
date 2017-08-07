package com.example.patrick.groceryapplication.utils;

import com.example.patrick.groceryapplication.models.BarCodeItems;
import com.example.patrick.groceryapplication.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Patrick on 7/3/2017.
 */

public class JsonUtils {

    private final static String RN_DIVIDER="";
    private final static String RN_NAME="";


    public static String[] getStringFromJson(String listJsonString)throws JSONException{

        String parsedShoppingList[]=null;
        JSONObject shoppingJson=new JSONObject(listJsonString);
        JSONArray shoppingArray=shoppingJson.getJSONArray(RN_DIVIDER);

        parsedShoppingList=new String[shoppingArray.length()];

        for(int i=0;i<shoppingArray.length();i++){
            JSONObject shoppingItem=shoppingArray.getJSONObject(i);

            String itemName=shoppingItem.getString(RN_NAME);

            parsedShoppingList[i]=itemName;
        }
        return parsedShoppingList;
    }

    public static ArrayList<Item> getItemListFromJson(String listJsonString) throws JSONException{
        ArrayList<Item> parsedShoppingList=new ArrayList<>();

        JSONObject shoppingJson=new JSONObject(listJsonString);
        JSONArray shoppingArray=shoppingJson.getJSONArray(RN_DIVIDER);

        for(int i=0;i<shoppingArray.length();i++){
            JSONObject shoppingItems=shoppingArray.getJSONObject(i);

            String itemName=shoppingItems.getString(RN_NAME);

            parsedShoppingList.add(new Item(itemName));
        }
        return parsedShoppingList;
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
