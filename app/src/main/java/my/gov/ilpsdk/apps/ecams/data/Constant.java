package my.gov.ilpsdk.apps.ecams.data;

import java.util.ArrayList;

import my.gov.ilpsdk.apps.ecams.model.Assets;
import my.gov.ilpsdk.apps.ecams.model.Locations;
import my.gov.ilpsdk.apps.ecams.model.Staff;

/**
 * Created by Nurzaly on 2/2/2018.
 */

public class Constant {

    public static final String DOMAIN_AMOS = "http://apps.ilpsdk.gov.my/amos/";
    public static final String AMOS_API = "http://api.ilpsdk.gov.my/amos/api/";
    public static final String DOMAIN_STAFED = "http://apps.ilpsdk.gov.my/stafed/";
    public static final String STAFED_API =  "http://api.ilpsdk.gov.my/stafed/api/";

    public static final String URL_ASSETS = AMOS_API + "assets";
    public static final String URL_ASSETS_BY_LOCATION = AMOS_API + "get-assets-by-location";
    public static final String URL_HAS_BARCODE = AMOS_API + "assets-has-barcode";
    public static final String URL_UPDATE_BARCODE = AMOS_API + "assets-update-barcode";
    public static final String URL_FIND_BARCODE = AMOS_API + "assets-find-barcode";
    public static final String URL_RESET_BARCODE = AMOS_API + "assets-reset-barcode";
    public static final String URL_GET_LOCATION = AMOS_API + "locations";
    public static final String URL_UPDATE_LOCATION = AMOS_API + "update-location";
    public static final String URL_UPDATE_PIC = AMOS_API + "update-pic";
    public static final String URL_COUNT_USER_ASSETS = AMOS_API + "count-user-assets";
    public static final String URL_ASSETS_JENIS = AMOS_API + "assets-jenis";
    public static final String URL_SEARCH_ASSETS = AMOS_API + "search-assets";
    public static final String URL_SAVE_PEMERIKSAAN_DATA = AMOS_API + "save-pemeriksaan-data";
    public static final String URL_SAVE_PEGAWAI_PEMERIKSA = AMOS_API + "save-pegawai-pemeriksa";
    public static final String URL_GET_PEMERIKSAAN_DATA = AMOS_API + "get-pemeriksaan-data";

    public static final String URL_LOGIN = STAFED_API + "login";
    public static final String URL_STAF_LIST = STAFED_API + "get-all-staf";
    public static final String URL_STAF_SINGLE = STAFED_API + "get-staf";

    //share preferences - myconfig
    public static final String MYCONFIG = "myconfig";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AMOS = "amos";
    public static final String KEY_BAHAGIAN = "bahagian";
    public static final String KEY_NAMA_BAHAGIAN = "nama_bahagian";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_URL = "url";

    public static Integer SCAN_MODE = 0;
    public static Integer FRAGMENT_ID = 0;

    public static int HARTAMODAL = -1;

    public static Assets assets = null;
    public static ArrayList<Locations> locations = null;
    public static ArrayList<Staff> staff = null;
    public static Boolean UPDATE_BARCODE_SUCCESS = false;
    public static String UPDATE_BARCODE_DATA;

}
