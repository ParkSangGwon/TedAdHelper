package gun0912.tedadhelper;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdRequest;

import gun0912.tedadhelper.util.AppUtil;
import gun0912.tedadhelper.util.Constant;

/**
 * Created by TedPark on 2017. 3. 7..
 */

public class TedAdHelper {

    public static final String TAG = "TedAdHelper";
    public static final int AD_FACEBOOK = 1;
    public static final int AD_ADMOB = 2;
    public static final int AD_TNK = 3;
    private static String admobDeviceId;
    private static boolean onlyFacebookInstalled = false;

    public static void setTestDeviceId(String facebookDeviceId, String admobDeviceId) {
        setFacebookTestDeviceId(facebookDeviceId);
        setAdmobTestDeviceId(admobDeviceId);
    }

    public static void setFacebookTestDeviceId(String deviceId) {
        AdSettings.addTestDevice(deviceId);
    }

    public static void setAdmobTestDeviceId(String deviceId) {
        admobDeviceId = deviceId;
    }

    public static void showAdOnlyFacebookInstalledUser(boolean value) {
        onlyFacebookInstalled = value;

    }

    public static AdRequest getAdRequest() {
        AdRequest.Builder builder = new AdRequest.Builder();

        if (!TextUtils.isEmpty(TedAdHelper.admobDeviceId)) {
            builder.addTestDevice(TedAdHelper.admobDeviceId);
        }


        return builder.build();
    }

    public static boolean isSkipFacebookAd(Context context) {
        return TedAdHelper.onlyFacebookInstalled && !AppUtil.isExistApp(context, Constant.FACEBOOK_PACKAGE_NAME);


    }

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

    public static String getMessageFromTnkErrorCode(int errorCode) {
        switch (errorCode) {
            case -1:
                return "제공할 광고가 없을 경우 또는 해당 광고앱이 이미 사용자 단말기에 설치되어 있는 경우입니다.";
            case -2:
                return "광고를 가져왔으나 전면 이미지 정보가 없는 경우입니다.";
            case -3:
                return "showInterstitialAd() 호출 후 지정된 timeoout시간 (기본값 5초) 이내에 전면광고가 도착하지 않은 경우입니다. 이 경우에는 전면광고를 띄우지 않습니다.";
            case -4:
                return "prepareInterstitialAd() 호출하였으나 서버에서 설정한 광고 노출 주기를 지나지 않아 취소된 경우입니다.";
            case -5:
                return "prepareInterstitialAd()를 호출하지 않고 showInterstitialAd()를 호출한 경우입니다.";
            case -9:
                return "prepareInterstitialAd()를 호출하지 않고 showInterstitialAd()를 호출한 경우입니다.";
            default:
                return "";
        }
    }

    public interface ImageProvider {
        void onProvideImage(ImageView imageView, String imageUrl);
    }
}
