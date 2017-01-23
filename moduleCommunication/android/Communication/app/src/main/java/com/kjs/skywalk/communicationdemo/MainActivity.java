package com.kjs.skywalk.communicationdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.kjs.skywalk.communicationlibrary.CommunicationCommand;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.CommunicationManager;
import com.kjs.skywalk.communicationlibrary.CommunicationParameterKey;
import com.kjs.skywalk.communicationlibrary.CommunicationError;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private final int mFragmentCount = 2;
    private final String TAG = "MainActivity";
    private Fragment[] mFragments;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();
        mFragments = new Fragment[mFragmentCount];
        mFragments[0] = mFragmentManager.findFragmentById(R.id.fragment_main);
        mFragments[1] = mFragmentManager.findFragmentById(R.id.fragment_log_in_out);
        mFragmentTransaction = mFragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]);
        mFragmentTransaction.show(mFragments[0]).commit();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        FragmentTransaction transaction = mFragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]);
//        for(int i = 0; i < mFragments.length; i ++) {
//            transaction = transaction.hide(mFragments[i]);
//        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Log.i(TAG, "Home Clicked");
            transaction.show(mFragments[0]).commit();
            return true;
        } else if(id == R.id.action_log_in_out) {
            Log.i(TAG, "Log In/Log Out Clicked");
            transaction.show(mFragments[1]).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
