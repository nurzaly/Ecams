package my.gov.ilpsdk.apps.ecams.data;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import my.gov.ilpsdk.apps.ecams.model.Assets;
import my.gov.ilpsdk.apps.ecams.model.Locations;

/**
 * Created by Nurzaly on 2/2/2018.
 */

public class GlobalVariable extends Application {
    private List<Assets> assets = new ArrayList<>();
    private List<Assets> updateds = new ArrayList<>();
    public List<Locations> locations = new ArrayList<>();


    public void setAssets(List<Assets> assets) {
        this.assets = assets;
    }
    public void setUpdates(List<Assets> updateds) {
        this.updateds = updateds;
    }

    public List<Assets> getAsset() {
        return assets;
    }

    public List<Assets> getUpdateds() {
        return updateds;
    }

    public void updatedAssets(int position){
        updateds.add(assets.get(position));
        assets.remove(position);
    }

    public static String get_no_casis(String no_casis){
        String ret = "";
        try {
            if(no_casis.length() > 0){
                ret = " - No. casis : " + no_casis;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
