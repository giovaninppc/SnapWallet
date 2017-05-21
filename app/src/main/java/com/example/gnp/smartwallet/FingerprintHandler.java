package com.example.gnp.smartwallet;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;

//@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context context;
    private MainActivity main;
    private FingerprintPage fingerprintFragment;
    private FragmentManager fm;

    public FingerprintHandler(Context mContext, MainActivity main, FingerprintPage fingerprintFragment, FragmentManager fm) {
        context = mContext;
        this.main = main;
        this.fingerprintFragment = fingerprintFragment;
        this.fm = fm;

    }

    //Responsible for starting the fingerprint authentication process
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters

    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device

    public void onAuthenticationFailed()
    {
        Toast message = Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG);
        TextView toastMessage = (TextView) message.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.RED);
        message.show();
    }

    @Override

    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }@Override

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

        main.sendDataSuccess();
        Toast message = Toast.makeText(context, "Success!", Toast.LENGTH_LONG);
        TextView toastMessage = (TextView) message.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.GREEN);
        message.show();
        refreshTab();
    }


    private void refreshTab() {
        final FragmentTransaction ft = fm.beginTransaction();
        ft.detach(fingerprintFragment);
        ft.attach(fingerprintFragment);
        ft.commit();

    }

}