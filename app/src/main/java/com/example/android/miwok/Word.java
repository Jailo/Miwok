package com.example.android.miwok;

/**
 * Created by jaielalondon on 5/12/18.
 * {@link Word} represents a vocabulary word the user wants to learn.
 * it contains a defualt translation and Miwok translation of that word
 */

public class Word {

    /*
    * declare miwok translation string
    * declare default translation string
     */
    private String mDefaultTranslation;
    private String mMiwokTranslation;

    //declare image resource id field that has no image initially
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    //constant value that represents no image was provided for this word
    private static final int NO_IMAGE_PROVIDED = -1;

    // Audio resource ID for the word
    private int mAudioResourceId;


    /**
     *
     * Creates a new Word with values for the miwok and defualt translations
     * @param defaultTranslation represents the same word in the users native language
     *                           such as english
     * @param miwokTranslation represents a word in the Miwok language
     *
     * @param miwokAudio is the raw resource for the miwok word pronunciation audio file
     */
    public Word(String defaultTranslation, String miwokTranslation, int miwokAudio) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mAudioResourceId = miwokAudio;
    }

    /**
     *
     * Creates a new Word object
     * @param defaultTranslation represents a word in the users native language
     *                           such as english
     * @param miwokTranslation represents the word translated to the Miwok language
     *
     * @param imageResourceId is the drawable resource id for the image associated with the word
     *
     * @param miwokAudio is the raw resource for the miwok word pronunciation audio file
     */
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId,
                int miwokAudio) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = miwokAudio;

    }

    /**
     * Returns the defualt translation of the word
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Returns the miwok translation of the word
  */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     *Returns image id
     */
    public int getImageResourceId() { return mImageResourceId; }

    /**
     * Returns weather or not there is an image for this word
     */
    public boolean hasImage(){ return mImageResourceId != NO_IMAGE_PROVIDED; }


    /**
     * Returns audio resource
     */
    public int getAudioResourceId() { return mAudioResourceId; }


    /**
     * Returns the string representation of the Word object
     */
    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }
}




