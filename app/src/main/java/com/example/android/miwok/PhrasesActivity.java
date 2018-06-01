package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

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

                        Log.v("PhrasesActivity", "Audio focus loss transient/ can duck");

                        //Pause media player
                        mMediaPlayer.pause();

                        //Reset the media player to the beginning of the audio file
                        //So that when we regain audio focus, it will start from the beginning
                        mMediaPlayer.seekTo(0);

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //Checks if app has permenantly lost audio focus
                        Log.v("PhrasesActivity", "lost audio focus");

                        //Stop and release the media player
                        releaseMediaPlayer();

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT){
                        //Checks if this app has regained audio focus
                        // Play audio file
                        Log.v("PhrasesActivity", "Gained audio focus");
                        mMediaPlayer.start();

                    } else if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                        //Checks if app has regained audio focus after temporarily losing it
                        //Start playing the audio file
                        mMediaPlayer.start();
                        Log.v("PhrasesActivity", "Audio focused request Granted");
                    }

                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Enable and display Up button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize audio manager wiht AUDIO_SERVICE
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //create an ArrayList for the default(Eng) and miwok translation of phrases
        // and the corresponding color and pronunciation audio resources
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?", "minto wuksus",
                R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә",
                R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...",
                R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?",
                R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit",
                R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?",
                R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm",
                R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm",
                R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis",
                R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem",
                R.raw.phrase_come_here));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml file.
        ListView listView = (ListView) findViewById(R.id.list);

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
                Log.v("PhrasesActivity", word.toString());

                //Request short-term audio focus
                int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    // We now have audio focus

                    //Music player gets audio file for the word the user clicked on
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this,
                            word.getAudioResourceId());

                    //Start playing the audio file
                    mMediaPlayer.start();
                    Log.v("PhrasesActivity", "Audio focused request Granted");

                    //Setup mediaPlayers on completion listener so that we can stop & relaese the media
                    // Player once the audio file stops playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }

            }
        });

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
    protected void onStop() {
        super.onStop();

        //when the activity is stopped, release the media player
        //because we wont need to play anymore sounds
        releaseMediaPlayer();
    }

    /**
     * Return to home screen when the Up butten is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
