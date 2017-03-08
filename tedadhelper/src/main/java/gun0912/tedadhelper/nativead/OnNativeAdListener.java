package gun0912.tedadhelper.nativead;

/**
 * Created by TedPark on 2017. 3. 8..
 */

public interface OnNativeAdListener {
    void onError(String errorMessage);

    void onLoaded(int adType);

    void onAdClicked(int adType);
}
