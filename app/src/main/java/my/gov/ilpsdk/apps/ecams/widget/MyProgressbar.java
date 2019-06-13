package my.gov.ilpsdk.apps.ecams.widget;

import android.app.ProgressDialog;
import android.view.View;

/**
 * Created by Nurzaly on 7/2/2018.
 */

public class MyProgressbar {
    private static ProgressDialog pDialog;
    private static String str = "Loading...";

    public MyProgressbar(View view, String str) {
        this.str = str;
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage(this.str);
        pDialog.setCancelable(false);
        showProgressDialog();
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
}
