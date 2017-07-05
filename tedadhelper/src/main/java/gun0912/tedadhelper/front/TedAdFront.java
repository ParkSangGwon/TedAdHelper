package gun0912.tedadhelper.front;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdRequest;

import gun0912.tedadhelper.TedAdHelper;


/**
 * Created by TedPark on 2017. 1. 25..
 */

public class TedAdFront {


    private static OnFrontAdListener onFrontAdListener;
    private static String facebookKey;
    private static String admobKey;
    private static Context context;

    public static void showFacebookFrontAd(Context context, String facebookKey, OnFrontAdListener onFrontAdListener) {
        showFrontAD(context, facebookKey, null, TedAdHelper.AD_FACEBOOK, onFrontAdListener);
    }

    public static void showAdmobFrontAd(Context context, String admobKey, OnFrontAdListener onFrontAdListener) {
        showFrontAD(context, null, admobKey, TedAdHelper.AD_ADMOB, onFrontAdListener);
    }

    public static void showFrontAD(Context context, String facebookKey, final String admobKey, int adPriority, OnFrontAdListener onFrontAdListener) {

        try {

            TedAdFront.context = context;
            TedAdFront.facebookKey = facebookKey;
            TedAdFront.admobKey = admobKey;
            TedAdFront.onFrontAdListener = onFrontAdListener;


            switch (adPriority) {
                case TedAdHelper.AD_FACEBOOK:
                    showFacebookFrontAd(!TextUtils.isEmpty(admobKey));
                    break;
                case TedAdHelper.AD_ADMOB:
                    showAdmobFrontAd(!TextUtils.isEmpty(facebookKey));
                    break;
                default:
                    throw new RuntimeException("You have to select priority type ADMOB or FACEBOOK");
            }

        } catch (Exception e) {

            if (onFrontAdListener != null) {
                onFrontAdListener.onError("");
            }
        }


    }

    private static void showFacebookFrontAd(final boolean failToAdmob) {

        final com.facebook.ads.InterstitialAd facebookFrontAD = new com.facebook.ads.InterstitialAd(context, facebookKey);

        if (onFrontAdListener != null) {
            onFrontAdListener.onFacebookAdCreated(facebookFrontAD);
        }


        // Set listeners for the Interstitial Ad
        facebookFrontAD.setAdListener(new com.facebook.ads.InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial displayed callback
                Log.d(TedAdHelper.TAG, "[FACEBOOK FRONT AD]Displayed");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK FRONT AD]Dismissed");
                // Interstitial dismissed callback
                if (onFrontAdListener != null) {
                    onFrontAdListener.onDismissed(TedAdHelper.AD_FACEBOOK);
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TedAdHelper.TAG, "[FACEBOOK FRONT AD]Error: " + adError.getErrorMessage());
                if (failToAdmob) {
                    showAdmobFrontAd(false);
                } else if (onFrontAdListener != null) {
                    onFrontAdListener.onError(adError.getErrorMessage());
                }

            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK FRONT AD]Loaded");
                // Show the ad when it's done loading.
                if (facebookFrontAD != null) {
                    try {
                        facebookFrontAD.show();
                    } catch (Exception e) {
                        if (failToAdmob) {
                            showAdmobFrontAd(false);
                        } else if (onFrontAdListener != null) {
                            onFrontAdListener.onError("");
                        }
                    }

                }


                if (onFrontAdListener != null) {
                    onFrontAdListener.onLoaded(TedAdHelper.AD_FACEBOOK);

                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK FRONT AD]Clicked");
                // Ad clicked callback
                if (onFrontAdListener != null) {
                    onFrontAdListener.onAdClicked(TedAdHelper.AD_FACEBOOK);
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // Load the interstitial ad
        facebookFrontAD.loadAd();


    }


    private static void showAdmobFrontAd(final boolean failToFacebook) {

        final com.google.android.gms.ads.InterstitialAd admobFrontAD = new com.google.android.gms.ads.InterstitialAd(context);
        admobFrontAD.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                Log.d(TedAdHelper.TAG, "[ADMOB FRONT AD]Dismissed");
                if (onFrontAdListener != null) {
                    onFrontAdListener.onDismissed(TedAdHelper.AD_ADMOB);
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                String errorMessage = TedAdHelper.getMessageFromAdmobErrorCode(errorCode);
                Log.e(TedAdHelper.TAG, "[ADMOB FRONT AD]Error: " + errorMessage);

                if (failToFacebook) {
                    showFacebookFrontAd(false);
                } else if (onFrontAdListener != null) {
                    onFrontAdListener.onError(errorMessage);
                }
            }

            @Override
            public void onAdLoaded() {
                Log.d(TedAdHelper.TAG, "[ADMOB FRONT AD]Loaded");
                admobFrontAD.show();

                if (onFrontAdListener != null) {
                    onFrontAdListener.onLoaded(TedAdHelper.AD_ADMOB);
                }
            }


            @Override
            public void onAdLeftApplication() {
                Log.d(TedAdHelper.TAG, "[ADMOB FRONT AD]Clicked");
                super.onAdLeftApplication();
                if (onFrontAdListener != null) {
                    onFrontAdListener.onAdClicked(TedAdHelper.AD_ADMOB);
                }
            }

        });
        admobFrontAD.setAdUnitId(admobKey);
        admobFrontAD.loadAd(new AdRequest.Builder().build());

    }


}
