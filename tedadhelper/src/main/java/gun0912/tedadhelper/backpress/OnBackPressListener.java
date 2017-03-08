package gun0912.tedadhelper.backpress;

/**
 * Created by TedPark on 2017. 3. 8..
 */

public interface OnBackPressListener  {

    void onReviewClick();

    void onFinish();

    void onError(String errorMessage);

    void onLoaded(int adType);

    void onAdClicked(int adType);
}
