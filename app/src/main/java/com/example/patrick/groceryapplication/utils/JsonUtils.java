package com.example.patrick.groceryapplication.utils;

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
}
