package my.gov.ilpsdk.apps.ecams.services;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import my.gov.ilpsdk.apps.ecams.data.Constant;

/**
 * Created by Nurzaly on 1/2/2018.
 */

public class RunBarcodeScanner {
    private static final int CAMERA_PERMISSION = 1;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;


    public void launchActivity(Class<?> clss, Activity activity, Context context, Integer mode) {

        Constant.SCAN_MODE = mode;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(context, clss);
            context.startActivity(intent);
        }
    }

    public interface RunBarcodeScannerListener{
        public void onFinish();
    }
}
