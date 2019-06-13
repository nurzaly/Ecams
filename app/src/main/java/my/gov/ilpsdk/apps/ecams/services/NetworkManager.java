package my.gov.ilpsdk.apps.ecams.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NetworkManager {

    public static void getAsJSONList(Context context, String url, String msg, Class<?> object){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        progressDialog.show();
        AndroidNetworking.get(url)
                .setTag(context)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(object, new ParsedRequestListener<List<?>>() {
                    @Override
                    public void onResponse(List<?> response) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                        progressDialog.dismiss();
                    }
                });

    }

}
