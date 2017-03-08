package gun0912.tedadhelper;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by TedPark on 2017. 3. 7..
 */

public class TedAdHelper {

    public static final String TAG = "TedAdHelper";

    public static final int AD_FACEBOOK = 1;
    public static final int AD_ADMOB = 2;


    public static String getMessageFromAdmobErrorCode(int errorCode) {

        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                return "an invalid response was received from the ad server";

            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                return "ad unit ID was incorrect";

            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                return "The ad request was unsuccessful due to network connectivity";

            case AdRequest.ERROR_CODE_NO_FILL:
                return "The ad request was successful, but no ad was returned due to lack of ad inventory";

            default:
                return "";
        }

    }
}
