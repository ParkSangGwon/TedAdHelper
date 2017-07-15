package gun0912.tedadhelper.banner;

import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;

import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.util.Constant;

/**
 * Created by TedPark on 2017. 1. 18..
 */

public class TedAdBanner {

    private static OnBannerAdListener onBannerAdListener;
    private static String facebookKey;
    private static String admobKey;
    private static ViewGroup bannerContainer;
    private static ArrayList<Integer> adPriorityList;

    public static void showFacebookBanner(ViewGroup bannerContainer, String facebookKey, OnBannerAdListener onBannerAdListener) {
        showBanner(bannerContainer, facebookKey, null, TedAdHelper.AD_FACEBOOK, onBannerAdListener);
    }

    public static void showAdmobBanner(ViewGroup bannerContainer, String admobKey, OnBannerAdListener onBannerAdListener) {
        showBanner(bannerContainer, null, admobKey, TedAdHelper.AD_ADMOB, onBannerAdListener);
    }

    public static void showBanner(ViewGroup bannerContainer, String facebookKey, String admobKey, int adPriority, OnBannerAdListener onBannerAdListener) {
        Integer[] tempAdPriorityList = new Integer[2];
        tempAdPriorityList[0] = adPriority;
        if (adPriority == TedAdHelper.AD_FACEBOOK) {
            tempAdPriorityList[1] = TedAdHelper.AD_ADMOB;
        } else {
            tempAdPriorityList[1] = TedAdHelper.AD_FACEBOOK;
        }
        showBanner(bannerContainer, facebookKey, admobKey, tempAdPriorityList, onBannerAdListener);
    }

    public static void showBanner(ViewGroup bannerContainer, String facebookKey, String admobKey, Integer[] tempAdPriorityList, OnBannerAdListener onBannerAdListener) {

        try {

            TedAdBanner.adPriorityList = new ArrayList<>(Arrays.asList(tempAdPriorityList));
            TedAdBanner.facebookKey = facebookKey;
            TedAdBanner.admobKey = admobKey;
            TedAdBanner.onBannerAdListener = onBannerAdListener;
            TedAdBanner.bannerContainer = bannerContainer;

            if (bannerContainer == null) {
                onBannerAdListener.onError("BannerContainer can not null");
                return;
            }

            selectAd();

        } catch (Exception e) {

            if (onBannerAdListener != null) {
                onBannerAdListener.onError("");
            }

        }
    }

    private static void selectAd() {
        int adPriority = adPriorityList.remove(0);
        switch (adPriority) {
            case TedAdHelper.AD_FACEBOOK:
                showFacebookBanner(!TextUtils.isEmpty(admobKey));
                break;
            case TedAdHelper.AD_ADMOB:
                showAdmobBanner(!TextUtils.isEmpty(facebookKey));
                break;
            case TedAdHelper.AD_TNK:
                onBannerAdListener.onError("TNK can not load banner");
                break;
            default:
                onBannerAdListener.onError("You have to select priority type ADMOB or FACEBOOK");

        }
    }


    private static void showFacebookBanner(final boolean failToAdmob) {

        if (TedAdHelper.isSkipFacebookAd(bannerContainer.getContext())) {
            Log.e(TedAdHelper.TAG, "[FACEBOOK BANNER]Error: " + Constant.ERROR_MESSAGE_FACEBOOK_NOT_INSTALLED);
            Log.d(TedAdHelper.TAG, "failToAdmob: " + failToAdmob);

            if (failToAdmob) {
                showAdmobBanner(false);
            } else if (onBannerAdListener != null) {
                onBannerAdListener.onError(Constant.ERROR_MESSAGE_FACEBOOK_NOT_INSTALLED);
            }
            return;
        }


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
                ViewGroup parentView = ((ViewGroup) facebookBanner.getParent());
                if (parentView != null) {
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

        admobBanner.loadAd(TedAdHelper.getAdRequest());

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
                ViewGroup parentView = ((ViewGroup) admobBanner.getParent());
                if (parentView != null) {
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
