package gun0912.tedadhelper.front;

import android.app.Activity;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.tnkfactory.ad.TnkAdListener;
import com.tnkfactory.ad.TnkSession;

import java.util.ArrayList;
import java.util.Arrays;

import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.util.Constant;


/**
 * Created by TedPark on 2017. 1. 25..
 */

public class TedAdFront {


    private static OnFrontAdListener onFrontAdListener;
    private static String facebookKey;
    private static String admobKey;
    private static Activity activity;

    private static ArrayList<Integer> adPriorityList;

    public static void showFacebookFrontAd(Activity activity, String facebookKey, OnFrontAdListener onFrontAdListener) {
        showFrontAD(activity, facebookKey, null, TedAdHelper.AD_FACEBOOK, onFrontAdListener);
    }

    public static void showAdmobFrontAd(Activity activity, String admobKey, OnFrontAdListener onFrontAdListener) {
        showFrontAD(activity, null, admobKey, TedAdHelper.AD_ADMOB, onFrontAdListener);
    }

    public static void showFrontAD(Activity activity, String facebookKey, final String admobKey, int adPriority, OnFrontAdListener onFrontAdListener) {
       Integer[] tempAdPriorityList=new Integer[2];
        tempAdPriorityList[0] = adPriority;
        if (adPriority == TedAdHelper.AD_FACEBOOK) {
            tempAdPriorityList[1]=TedAdHelper.AD_ADMOB;
        } else {
            tempAdPriorityList[1]=TedAdHelper.AD_FACEBOOK;
        }

        showFrontAD(activity, facebookKey, admobKey, tempAdPriorityList, onFrontAdListener);
    }

    public static void showFrontAD(Activity activity, String facebookKey, final String admobKey, Integer[] tempAdPriorityList, OnFrontAdListener onFrontAdListener) {
        if(tempAdPriorityList==null||tempAdPriorityList.length==0){
            throw new RuntimeException("You have to select priority type ADMOB/FACEBOOK/TNK");
        }

        TedAdFront.adPriorityList =new ArrayList<>(Arrays.asList(tempAdPriorityList));


        try {


            TedAdFront.activity = activity;
            TedAdFront.facebookKey = facebookKey;
            TedAdFront.admobKey = admobKey;
            TedAdFront.onFrontAdListener = onFrontAdListener;

            selectAd();

        } catch (Exception e) {
            e.printStackTrace();
            if (onFrontAdListener != null) {
                onFrontAdListener.onError(e.toString());
            }
        }

    }

    private static void selectAd() {
        int adPriority = adPriorityList.remove(0);
        switch (adPriority) {
            case TedAdHelper.AD_FACEBOOK:
                showFacebookFrontAd();
                break;
            case TedAdHelper.AD_ADMOB:
                showAdmobFrontAd();
                break;
            case TedAdHelper.AD_TNK:
                showTnkFrontAd();
                break;
            default:
                onFrontAdListener.onError("You have to select priority type ADMOB or FACEBOOK");
        }
    }

    private static void showFacebookFrontAd() {

        if(TedAdHelper.isSkipFacebookAd(activity)){
            Log.e(TedAdHelper.TAG, "[FACEBOOK FRONT AD]Error: " + Constant.ERROR_MESSAGE_FACEBOOK_NOT_INSTALLED);

            if (adPriorityList.size() > 0) {
                selectAd();
            } else if (onFrontAdListener != null) {
                onFrontAdListener.onError(Constant.ERROR_MESSAGE_FACEBOOK_NOT_INSTALLED);
            }
            return;
        }

        final com.facebook.ads.InterstitialAd facebookFrontAD = new com.facebook.ads.InterstitialAd(activity, facebookKey);

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
                if (adPriorityList.size() > 0) {
                    selectAd();
                } else if (onFrontAdListener != null) {
                    onFrontAdListener.onError(adError.getErrorMessage());
                }

            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK FRONT AD]Loaded");
                // Show the ad when it's done loading.
                try {
                    if (facebookFrontAD != null) {
                        facebookFrontAD.show();
                    }
                }catch (Exception e){
                    if (onFrontAdListener != null) {
                        onFrontAdListener.onError("");

                    }
                    return;
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


    private static void showAdmobFrontAd() {

        final com.google.android.gms.ads.InterstitialAd admobFrontAD = new com.google.android.gms.ads.InterstitialAd(activity);
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

                if (adPriorityList.size() > 0) {
                    selectAd();
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
        admobFrontAD.loadAd(TedAdHelper.getAdRequest());

    }

    private static void showTnkFrontAd() {
        TnkSession.prepareInterstitialAd(activity, TnkSession.CPC, new TnkAdListener() {

            @Override
            public void onClose(int type) {
                Log.d(TedAdHelper.TAG, "[TNK FRONT AD]onClose");
                if (onFrontAdListener != null) {
                    onFrontAdListener.onDismissed(TedAdHelper.AD_TNK);
                }
            }

            @Override
            public void onFailure(int errCode) {
                String errorMessage=TedAdHelper.getMessageFromTnkErrorCode(errCode);
                Log.e(TedAdHelper.TAG, "[TANK FRONT AD]" + errorMessage);
                if (adPriorityList.size() > 0) {
                    selectAd();
                } else if (onFrontAdListener != null) {
                    onFrontAdListener.onError(errorMessage);
                }
            }

            @Override
            public void onLoad() {
                Log.d(TedAdHelper.TAG, "[TNK FRONT AD]onLoad");
                TnkSession.showInterstitialAd(activity);
                if (onFrontAdListener != null) {
                    onFrontAdListener.onLoaded(TedAdHelper.AD_TNK);
                }

            }

            @Override
            public void onShow() {
                Log.d(TedAdHelper.TAG, "[TNK FRONT AD]onShow");

            }
        });
    }

}
