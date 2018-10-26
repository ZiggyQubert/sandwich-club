package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /**
     * helper function to parse out the array data from the JSONArray
     *
     * @param dataToParse
     * @return
     */
    public static List<String> getValuesFromJsonArray(JSONArray dataToParse) {
        List<String> parsedData = new ArrayList<String>();

        for (int i = 0; i < dataToParse.length(); i++) {
            try {
                String value = dataToParse.getString(i);
                parsedData.add(value);
            } catch (Exception e) {
                Log.e("sandwitch-club", "Error parsing value");
                e.printStackTrace();
            }
        }

        return parsedData;
    }

    public static Sandwich parseSandwichJson(String json) {
        Log.i("sandwitch-club", json);

        JSONObject sandwichJSON;

        String mainName;
        List<String> alsoKnownAs;
        String placeOfOrigin;
        String description;
        String image;
        List<String> ingredients;

        Sandwich sandwich;

        try {
            //the main json object
            sandwichJSON = new JSONObject(json);

            //json object to hold name information
            JSONObject nameInformation = sandwichJSON.getJSONObject("name");
            //pulls all the name information
            mainName = nameInformation.getString("mainName");
            JSONArray knownAsJsonArray = nameInformation.getJSONArray("alsoKnownAs");
            alsoKnownAs = JsonUtils.getValuesFromJsonArray(knownAsJsonArray);

            //gets the image
            placeOfOrigin = sandwichJSON.getString("placeOfOrigin");

            //gets the image
            description = sandwichJSON.getString("description");

            //gets the image
            image = sandwichJSON.getString("image");

            //gets the list of ingridents
            JSONArray ingredientsJsonArray = sandwichJSON.getJSONArray("ingredients");
            ingredients = JsonUtils.getValuesFromJsonArray(ingredientsJsonArray);

            sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
        } catch (Exception e) {
            Log.e("sandwitch-club", "Error Parsing JSON");
            e.printStackTrace();
            return null;
        }

        return sandwich;
    }
}
