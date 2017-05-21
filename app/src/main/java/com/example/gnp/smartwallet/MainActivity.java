package com.example.gnp.smartwallet;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;

import com.motorola.mod.ModDevice;
import com.motorola.mod.ModManager;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private UUID ModID;
    private String PhoneID;
    private ModAssistant modAssistant;
    private ModRawProtocol modRawProtocol;
    private final int REQUEST_RAW_PERMISSION = 120;
    public MainActivity main;
    private PermissionsPage tab2;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //REMOVE 1

        // MOTOMOD: Verifying that the app is not compatible
        if (!ModAssistant.isSmartphoneCompatible(getApplicationContext())) {
            Toast.makeText(getBaseContext(), "Smartphone is not compatible with Mods architecture", Toast.LENGTH_SHORT).show();
        }

        // MOTOMOD: Check and grant permission to use Raw Protocol.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(ModManager.PERMISSION_USE_RAW_PROTOCOL)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ModManager.PERMISSION_USE_RAW_PROTOCOL},
                    REQUEST_RAW_PERMISSION);
        }

        modAssistant = ModAssistant.getInstance(this);
        modAssistant.registerListener(handler);

        modRawProtocol = ModRawProtocol.getInstance(this);
        modAssistant.setModRawProtocol(modRawProtocol);
        modRawProtocol.registerListener(handler);

        updateDeviceInfo();

    }

    protected void sendDataSuccess ()
    {
        modRawProtocol.sendRawData(new byte[] {0x01});
        tab2.addNewLog("Giovani");

    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case (ModAssistant.ACTION_MOD_ATTACH):
                    updateDeviceInfo();
                    break;
                case (ModAssistant.ACTION_MOD_DETACH):
                    updateDeviceInfo();
            }
        }
    };


    private void updateDeviceInfo() {
        if (modAssistant.getModDevice() == null) {
            Toast.makeText(getBaseContext(), "SmartWallet was detached", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getBaseContext(), "SmartWallet was attached", Toast.LENGTH_SHORT).show();
            boolean rawAvailable = ModRawProtocol.checkRawProtocolAvailable(modAssistant.getModManager(), modAssistant.getModDevice());
            ModID = modAssistant.getModDevice().getUniqueId();
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            PhoneID = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position){

                case 0:
                    FingerprintPage tab1 = new FingerprintPage();
                    tab1.setArguments(main, getSupportFragmentManager());
                    return tab1;
                case 1:
                    tab2 = new PermissionsPage();
                    return tab2;
                case 2:
                    ContentPage tab3 = new ContentPage();
                    return tab3;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Status";
                case 1:
                    return "Recent Activity";
                case 2:
                    return "Permissions";
            }
            return null;
        }
    }
}