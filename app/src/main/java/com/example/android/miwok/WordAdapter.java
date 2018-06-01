package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaielalondon on 5/13/18.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private Context mContext;

    /* Creates a new list of words*/
    private List<Word> wordList = new ArrayList<>();

    /* Resource ID for the background color for this list of words*/
    private int mBackgroundColorResourceId;

    /**
     * The context is used to inflate the layout file, and words is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param words A List of Words objects to display in a list
     * @param color is the desired background color
     */
    public WordAdapter(@NonNull Context context, ArrayList<Word> words, int color) {
        // Here, we initialize the WordAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews, the adapter is not
        // going to use this second argument, so it can be an
        // y value. Here, we used 0.
        super(context, 0, words);
        mContext = context;
        wordList = words;
        mBackgroundColorResourceId = color;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Word currentWord = wordList.get(position);

        // Find the TextView in the list_item.xml layout with the ID miwok_text_view
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        // Get the miwok word from the current Word object and
        // set this text on the name TextView
        miwokTextView.setText(currentWord.getMiwokTranslation());

        // Find the TextView in the list_item.xml layout with the ID default_text_view
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        // Get the defualt language word from the current Word object and
        // set this text on the number TextView
        defaultTextView.setText(currentWord.getDefaultTranslation());

        //Find the ImageView in the list_item.xml layout with the ID image
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        if (currentWord.hasImage()) {
            //get the ID of the image that is associated with the current word
            //set this image on the ImageView
            imageView.setImageResource(currentWord.getImageResourceId());
            //set ImageView to be visible
            imageView.setVisibility(View.VISIBLE);
        } else {
            //set ImageView to be GONE
            imageView.setVisibility(View.GONE);
        }

        // Find the LinearLayout that surrounds the defualt and miwok TextViews
        View textViewLayout = listItemView.findViewById(R.id.TextViewContainer);

        //find the color the resource id maps to
        int color = ContextCompat.getColor(getContext(), mBackgroundColorResourceId);

        //set background color to this color
        textViewLayout.setBackgroundColor(color);

        // Return the whole list item layout (containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }

}
