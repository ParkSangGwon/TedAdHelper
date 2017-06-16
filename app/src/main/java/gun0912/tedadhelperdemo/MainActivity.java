package gun0912.tedadhelperdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.ads.InterstitialAd;

import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.backpress.OnBackPressListener;
import gun0912.tedadhelper.backpress.TedBackPressDialog;
import gun0912.tedadhelper.banner.OnBannerAdListener;
import gun0912.tedadhelper.banner.TedAdBanner;
import gun0912.tedadhelper.front.OnFrontAdListener;
import gun0912.tedadhelper.front.TedAdFront;
import gun0912.tedadhelper.nativead.OnNativeAdListener;
import gun0912.tedadhelper.nativead.TedNativeAdHolder;


public class MainActivity extends AppCompatActivity {

    public static final String FACEBOOK_KEY_BANNER = "xxxxxxxxxxx_xxxxxxxxxxxx";
    public static final String FACEBOOK_KEY_FRONT = "xxxxxxxxxxx_xxxxxxxxxxxx";
    public static final String FACEBOOK_KEY_BACKPRESS = "xxxxxxxxxxx_xxxxxxxxxxxx";
    public static final String FACEBOOK_KEY_NATIVE = "xxxxxxxxxxx_xxxxxxxxxxxx";


    public static final String ADMOB_KEY_BANNER = "ca-app-pub-xxxxx/xxxxxxxx";
    public static final String ADMOB_KEY_FRONT = "ca-app-pub-xxxxx/xxxxxxxx";

    public static final String ADMOB_KEY_BACKPRESS = "ca-app-pub-xxxxx/xxxxxxxx";
    public static final String ADMOB_KEY_NATIVE = "ca-app-pub-xxxxx/xxxxxxxx";

    InterstitialAd facebookFrontAD;
    com.facebook.ads.AdView facebookBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Banner
         */

        FrameLayout bannerContainer = (FrameLayout) findViewById(R.id.bannerContainer);

        //TedAdBanner.showFacebookBanner();
        //TedAdBanner.showAdmobBanner();

        TedAdBanner.showBanner(bannerContainer, FACEBOOK_KEY_BANNER, ADMOB_KEY_BANNER, TedAdHelper.AD_FACEBOOK, new OnBannerAdListener() {
            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onLoaded(int adType) {

            }

            @Override
            public void onAdClicked(int adType) {

            }

            @Override
            public void onFacebookAdCreated(com.facebook.ads.AdView facebookBanner) {
                MainActivity.this.facebookBanner = facebookBanner;
            }

        });


        /**
         * Front AD
         */

        //TedAdFront.showAdmobFrontAd();
        //TedAdFront.showFacebookFrontAd();
        TedAdFront.showFrontAD(this, FACEBOOK_KEY_FRONT, ADMOB_KEY_FRONT, TedAdHelper.AD_ADMOB, new OnFrontAdListener() {
            @Override
            public void onDismissed(int adType) {

            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onLoaded(int adType) {

            }

            @Override
            public void onAdClicked(int adType) {

            }

            @Override
            public void onFacebookAdCreated(InterstitialAd facebookFrontAD) {
                MainActivity.this.facebookFrontAD = facebookFrontAD;
            }
        });


        /**
         * Native AD
         */
        View cardview = findViewById(R.id.cardview);
        TedNativeAdHolder tedNativeAdHolder = new TedNativeAdHolder(cardview, this, getString(R.string.app_name), FACEBOOK_KEY_NATIVE, ADMOB_KEY_NATIVE);

        tedNativeAdHolder.loadAD(TedAdHelper.AD_FACEBOOK, new OnNativeAdListener() {
            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onLoaded(int adType) {

            }

            @Override
            public void onAdClicked(int adType) {

            }
        });
        //tedNativeAdHolder.loadFacebookAD();
        //tedNativeAdHolder.loadAdmobAD();

    }

    @Override
    public void onBackPressed() {

        //TedBackPressDialog.startFacebookDialog();
        //TedBackPressDialog.startAdmobDialog();
        TedBackPressDialog.startDialog(this, getString(R.string.app_name), FACEBOOK_KEY_BACKPRESS, ADMOB_KEY_BACKPRESS, TedAdHelper.AD_FACEBOOK, false,new OnBackPressListener() {
            @Override
            public void onReviewClick() {
            }

            @Override
            public void onFinish() {
                finish();
            }

            @Override
            public void onError(String errorMessage) {
            }

            @Override
            public void onLoaded(int adType) {
            }

            @Override
            public void onAdClicked(int adType) {
            }
        });
    }


    @Override
    protected void onDestroy() {

        if (facebookFrontAD != null) {
            facebookFrontAD.destroy();
        }

        if (facebookBanner != null) {
            facebookBanner.destroy();
        }

        super.onDestroy();
    }
}


