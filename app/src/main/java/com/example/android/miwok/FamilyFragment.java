package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

    /* Handles playing all audio files */
    private MediaPlayer mMediaPlayer;

    /* Manages audio focus */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered when the {@link MediaPlayer}
     * Finishes playing an audio file
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    /**
     * This listener gets triggered whenever there is a change in audio focus
     * (i.e we get a call and audio focus changes from our app to the ringtone)
     */
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new
            AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {

                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
                            || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //Checks if audio focus has been temporarily lost
                        //Or if the audio focus has been temporarily lost and we can duck
                        //in both cases, we want to pause and restart the audio file

                        Log.v("FamilyActivity", "Audio focus loss transient/ can duck");

                        //Pause media player
                        mMediaPlayer.pause();

                        //Reset the media player to the beginning of the audio file
                        //So that when we regain audio focus, it will start from the beginning
                        mMediaPlayer.seekTo(0);

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //Checks if app has permenantly lost audio focus
                        Log.v("FamilyActivity", "lost audio focus");

                        //Stop and release the media player
                        releaseMediaPlayer();

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT){
                        //Checks if this app has regained audio focus
                        // Play audio file
                        Log.v("FamilyActivity", "Gained audio focus");
                        mMediaPlayer.start();

                    } else if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                        //Checks if app has regained audio focus after temporarily losing it
                        //Start playing the audio file
                        mMediaPlayer.start();
                        Log.v("FamilyActivity", "Audio focused request Granted");
                    }

                }
            };


    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the word_list view
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //Initialize audio manager wiht AUDIO_SERVICE
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //create an ArrayList for the default(Eng) and miwok translation of family members
        // and the corresponding color and pronunciation audio resources
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father", "әpә",
                R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa",
                R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi",
                R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune",
                R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi",
                R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti",
                R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe",
                R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti",
                R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "ama",
                R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa",
                R.drawable.family_grandfather, R.raw.family_grandfather));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.

        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_family);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        //Create an on item click listener
        //that plays the pronunciation of the clicked on miwok word
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Release the current audio file if it exists
                //because we are about to play a new one
                releaseMediaPlayer();

                //reference to current word that the user clicked on
                Word word = words.get(position);
                Log.v("FamilyActivity", word.toString());

                //Request short-term audio focus
                int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    // We now have audio focus

                    //Music player gets audio file for the word the user clicked on
                    mMediaPlayer = MediaPlayer.create(getActivity(),
                            word.getAudioResourceId());

                    //Start playing the audio file
                    mMediaPlayer.start();
                    Log.v("FamilyActivity", "Audio focused request Granted");

                    //Setup mediaPlayers on completion listener so that we can stop &
                    // relaese the media player once the audio file stops playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
                //Music player gets audio file for word and plays it
                mMediaPlayer = MediaPlayer.create(getActivity(),
                        word.getAudioResourceId());

                //Start the audio file
                mMediaPlayer.start();

                //Setup mediaPlayers on completion listener so that we can stop & relaese the media
                // Player once the audio file stops playing
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
            }
        });

        return rootView;
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {

        //Abandon audio focus regardless of whether or not we were granted it
        //This also unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
        mAudioManager.abandonAudioFocus(audioFocusChangeListener);

        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //when the activity is stopped, release the media player
        //because we wont need to play anymore sounds
        releaseMediaPlayer();

    }


}
