package my.gov.ilpsdk.apps.ecams.services;

import android.content.Context;
import android.content.SharedPreferences;

import my.gov.ilpsdk.apps.ecams.data.Constant;

import static android.content.Context.MODE_PRIVATE;

public class GetSharePref {
    private SharedPreferences myconfig;

    public GetSharePref(Context applicationContext) {
        this.myconfig = applicationContext.getSharedPreferences(Constant.MYCONFIG,MODE_PRIVATE);
    }

    public String getString(String str, String def){
        return myconfig.getString(str,def);
    }
    public boolean getBoolean(String str,boolean def){
        return  myconfig.getBoolean(str,def);
    }

    public  SharedPreferences.Editor edit(){
        return myconfig.edit();
    }
}
