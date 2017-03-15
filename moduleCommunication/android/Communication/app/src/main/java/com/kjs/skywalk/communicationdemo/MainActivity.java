package com.kjs.skywalk.communicationdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");
    }
}
