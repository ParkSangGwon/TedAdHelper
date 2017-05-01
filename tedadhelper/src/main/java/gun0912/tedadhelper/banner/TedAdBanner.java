package gun0912.tedadhelper.banner;

import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import gun0912.tedadhelper.TedAdHelper;

/**
 * Created by TedPark on 2017. 1. 18..
 */

public class TedAdBanner {

    private static OnBannerAdListener onBannerAdListener;
    private static String facebookKey;
    private static String admobKey;
    private static ViewGroup bannerContainer;

    public static void showFacebookBanner(ViewGroup bannerContainer, String facebookKey, OnBannerAdListener onBannerAdListener) {
        showBanner(bannerContainer, facebookKey, null, TedAdHelper.AD_FACEBOOK, onBannerAdListener);
    }

    public static void showAdmobBanner(ViewGroup bannerContainer, String admobKey, OnBannerAdListener onBannerAdListener) {
        showBanner(bannerContainer, null, admobKey, TedAdHelper.AD_ADMOB, onBannerAdListener);
    }

    public static void showBanner(ViewGroup bannerContainer, String facebookKey, String admobKey, int adPriority, OnBannerAdListener onBannerAdListener) {

        try {


            TedAdBanner.facebookKey = facebookKey;
            TedAdBanner.admobKey = admobKey;
            TedAdBanner.onBannerAdListener = onBannerAdListener;
            TedAdBanner.bannerContainer = bannerContainer;

            if (bannerContainer == null) {
                throw new RuntimeException("BannerContainer can not null");
            }

            switch (adPriority) {
                case TedAdHelper.AD_ADMOB:
                    showAdmobBanner(!TextUtils.isEmpty(facebookKey));
                    break;
                case TedAdHelper.AD_FACEBOOK:
                    showFacebookBanner(!TextUtils.isEmpty(admobKey));
                    break;

                default:
                    throw new RuntimeException("You have to select priority type ADMOB or FACEBOOK");
            }

        } catch (Exception e) {

            if (onBannerAdListener != null) {
                onBannerAdListener.onError("");
            }

        }

    }

    private static void showFacebookBanner(final boolean failToAdmob) {


        final com.facebook.ads.AdView facebookBanner = new com.facebook.ads.AdView(bannerContainer.getContext(), facebookKey, AdSize.BANNER_HEIGHT_50);

        if (onBannerAdListener != null) {
            onBannerAdListener.onFacebookAdCreated(facebookBanner);
        }

        facebookBanner.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TedAdHelper.TAG, "[FACEBOOK BANNER]Error: " + adError.getErrorMessage());
                if (failToAdmob) {
                    showAdmobBanner(false);
                } else if (onBannerAdListener != null) {
                    onBannerAdListener.onError(adError.getErrorMessage());
                }


            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK BANNER]Loaded");
                bannerContainer.removeAllViews();
                ViewGroup parentView =  ((ViewGroup)facebookBanner.getParent());
                if(parentView!=null){
                    parentView.removeAllViews();
                }

                bannerContainer.addView(facebookBanner);

                if (onBannerAdListener != null) {
                    onBannerAdListener.onLoaded(TedAdHelper.AD_FACEBOOK);
                }


            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK BANNER]Clicked");
                if (onBannerAdListener != null) {
                    onBannerAdListener.onAdClicked(TedAdHelper.AD_FACEBOOK);
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        });
        facebookBanner.loadAd();

    }

    private static void showAdmobBanner(final boolean failToFacebook) {
        final com.google.android.gms.ads.AdView admobBanner = new AdView(bannerContainer.getContext());
        admobBanner.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        admobBanner.setAdUnitId(admobKey);

        AdRequest adRequest = new AdRequest.Builder().build();
        admobBanner.loadAd(adRequest);

        admobBanner.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                String errorMessage = TedAdHelper.getMessageFromAdmobErrorCode(errorCode);
                Log.e(TedAdHelper.TAG, "[ADMOB BANNER]Error: " + errorMessage);

                if (failToFacebook) {
                    showFacebookBanner(false);
                } else if (onBannerAdListener != null) {
                    onBannerAdListener.onError(errorMessage);
                }


            }

            @Override
            public void onAdLoaded() {
                Log.d(TedAdHelper.TAG, "[ADMOB BANNER]Loaded");
                bannerContainer.removeAllViews();
                ViewGroup parentView =  ((ViewGroup)admobBanner.getParent());
                if(parentView!=null){
                    parentView.removeAllViews();
                }
                bannerContainer.addView(admobBanner);

                if (onBannerAdListener != null) {
                    onBannerAdListener.onLoaded(TedAdHelper.AD_ADMOB);
                }
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(TedAdHelper.TAG, "[ADMOB BANNER]Clicked");
                super.onAdLeftApplication();
                if (onBannerAdListener != null) {
                    onBannerAdListener.onAdClicked(TedAdHelper.AD_ADMOB);
                }
            }


        });

    }


}
