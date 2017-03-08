package gun0912.tedadhelper.banner;

/**
 * Created by TedPark on 2017. 3. 8..
 */

public interface OnBannerAdListener {
    void onError(String errorMessage);

    void onLoaded(int adType);

    void onAdClicked(int adType);

    void onFacebookAdCreated(com.facebook.ads.AdView facebookBanner);
}
