package my.gov.ilpsdk.apps.ecams;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import my.gov.ilpsdk.apps.ecams.data.Constant;
import my.gov.ilpsdk.apps.ecams.widget.BaseScannerActivity;
import my.gov.ilpsdk.apps.ecams.R;


public class FullScannerFragmentActivity extends BaseScannerActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_full_scanner_fragment);
        TextView item_name = (TextView) findViewById(R.id.item_name);
        if(Constant.assets != null){
            item_name.setText("Item : " + Constant.assets.getNo_siri_pendaftaran());
        }
        else{
            item_name.setVisibility(View.GONE);
        }
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}