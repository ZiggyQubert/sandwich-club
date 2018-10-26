package com.udacity.sandwichclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        //updating the Picasso and image editing code to handle images that don't load and hide the image display area
        ingredientsIv.setVisibility(View.VISIBLE);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.e("sandwitch-club", "Error loading image");
                exception.printStackTrace();
                hideImage();
            }
        });
        builder.build()
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void hideImage() {
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        ingredientsIv.setVisibility(View.GONE);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * adds all items in the list to the text view
     *
     * @param itemsToAdd
     * @param view
     */
    private void addListItemsToTextView(List<String> itemsToAdd, TextView view) {
        view.setText("");
        Boolean first = true;
        for (String value : itemsToAdd) {
            if (!first) {
                view.append("\n");
            }
            view.append("- " + value);
            first = false;
        }
    }

    private void populateUI(Sandwich sandwich) {

        //sets the sandwich image here
        ImageView image = (ImageView) findViewById(R.id.image_iv);
        String imageUrlString = sandwich.getImage();
        if (!imageUrlString.isEmpty()) {
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.GONE);
        }

        //sets the alt names
        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        TextView alsoKnownAsLabel = (TextView) findViewById(R.id.also_known_label_tv);
        TextView alsoKnownAs = (TextView) findViewById(R.id.also_known_tv);
        if (alsoKnownAsList.size() > 0) {
            alsoKnownAsLabel.setVisibility(View.VISIBLE);
            alsoKnownAs.setVisibility(View.VISIBLE);
            addListItemsToTextView(alsoKnownAsList, alsoKnownAs);
        } else {
            alsoKnownAs.setText("");
            alsoKnownAsLabel.setVisibility(View.GONE);
            alsoKnownAs.setVisibility(View.GONE);
        }

        //sets the ingredients
        TextView ingredients = (TextView) findViewById(R.id.ingredients_tv);
        addListItemsToTextView(sandwich.getIngredients(), ingredients);

        //gets the place of origin and sets it in the view
        TextView placeOfOrigin = (TextView) findViewById(R.id.origin_tv);
        placeOfOrigin.setText(sandwich.getPlaceOfOrigin());

        //gets the place of origin and sets it in the view
        TextView description = (TextView) findViewById(R.id.description_tv);
        description.setText(sandwich.getDescription());
    }
}
