package my.gov.ilpsdk.apps.ecams.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import my.gov.ilpsdk.apps.ecams.data.Constant;

import static android.content.ContentValues.TAG;

/**
 * Created by Nurzaly on 2/2/2018.
 */

public class DbBackgroundServices extends IntentService{
    public static final String PARAM_SCAN_BARCODE = "scan_barcode";
    private  String scan_barcode;

    public DbBackgroundServices() {
        super("DisplayNotification");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

         scan_barcode = intent.getStringExtra(PARAM_SCAN_BARCODE);

        //updateAssets(scan_barcode);
    }

    private void updateAssets(String scan_barcode){
        AndroidNetworking.post(Constant.AMOS_API)
                .addBodyParameter("id", String.valueOf(Constant.assets.getId()))
                .addBodyParameter("barcode",scan_barcode)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: success update " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError
                        );
                    }
                });
    }


}
