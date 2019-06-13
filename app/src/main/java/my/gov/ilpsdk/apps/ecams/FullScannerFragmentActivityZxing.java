package my.gov.ilpsdk.apps.ecams;

import android.os.Bundle;

import my.gov.ilpsdk.apps.ecams.widget.BaseScannerActivity;
import my.gov.ilpsdk.apps.ecams.R;

public class FullScannerFragmentActivityZxing extends BaseScannerActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_full_screen_scanner_fragment_zxing);
        setupToolbar();

    }
}