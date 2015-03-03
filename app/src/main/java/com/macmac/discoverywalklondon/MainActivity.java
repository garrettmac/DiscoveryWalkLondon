package com.macmac.discoverywalklondon;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private ImageButton play_button;
    private TextView playedTimeView;
    private SeekBar seekBar;
    private Spinner bookSpinner;
    private TextView maxTimeView;
    private int position;


    private LocalBroadcastManager bcm;

    private int oldPosition = -1;
    private static final String TAG = "de.ph1b.audiobook.fragment.BookPlayFragment";

    private boolean seekBarIsUpdating = false;
   // private SongDetail song;
    //private ArrayList<MediaDetail> allMedia;

    private int duration;

    //public AudioPlayerService mService;
    public boolean mBound = false;
    private boolean sleepTimerActive = false;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        //SET PLAYER------------------------

//--------------------
        File file =getFilesDir();
        AssetManager am = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("100 - Welcome to London.mp3", MODE_PRIVATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = createFileFromInputStream(inputStream);


        // Context context = null;
       // File file = new File(context.getFilesDir() + File.separator + filePath);
       // Uri myUri = Uri.parse(String.valueOf(filePath));

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(getFileStreamPath(am.toString())));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {}

    }


   /* public void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (resultCode == RESULT_OK)
        {
            Uri data = result.getData();
            String thePath = data.getPath();
            // Do something with the file path
        }
    }
*/
        //-------------------
/*

        AssetManager am = getAssets();
        InputStream inputStream = am.open(file:"/app/src/main/assets");
        File infile = createFileFromInputStream(inputStream);
*/

        private File createFileFromInputStream(InputStream inputStream) {

            try{
                //File my_file_name;
                File f = new File("createFileFromImputStream");
                OutputStream outputStream = new FileOutputStream(f);
                byte buffer[] = new byte[1024];
                int length = 0;

                while((length=inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer,0,length);
                }

                outputStream.close();
                inputStream.close();

                return f;
            }catch (IOException e) {
                //Logging exception
            }

            return null;
        }




    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {

            int resID2 = v.getId();

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            try {
                startActivityForResult(intent,resID2); }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please install a file manager", Toast.LENGTH_LONG).show();
            }
        }
    };



    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private TextView maxTimeView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            playerDisplay(rootView);
            return rootView;
        }
        private void playerDisplay(View rootView) {
            //init buttons
            SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
            ImageButton play_button = (ImageButton) rootView.findViewById(R.id.play);
            ImageButton rewind_button = (ImageButton) rootView.findViewById(R.id.rewind);
            ImageButton fast_forward_button = (ImageButton) rootView.findViewById(R.id.fast_forward);
            TextView playedTimeView = (TextView) rootView.findViewById(R.id.played);
            ImageView coverView = (ImageView) rootView.findViewById(R.id.book_cover);

            //setup buttons
            //rewind_button.setOnClickListener(this);
           // fast_forward_button.setOnClickListener(this);
            //play_button.setOnClickListener(this);
            //playedTimeView.setOnClickListener(this);



        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
