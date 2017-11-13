package gun0912.tedadhelper.nativead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import gun0912.tedadhelper.TedAdHelper;


/**
 * Created by TedPark on 16. 5. 19..
 */

public class TedNativeAdHolder extends RecyclerView.ViewHolder {

    TedNativeAd tedNativeAd;

    public TedNativeAdHolder(ViewGroup itemView, Context context, String app_name, String facebook_ad_key, String admob_ad_key) {
        super(itemView);
        tedNativeAd = new TedNativeAd(itemView, context, app_name, facebook_ad_key, admob_ad_key);
    }


    public TedNativeAdHolder(ViewGroup itemView, Context context, String app_name, String facebook_ad_key, String admob_ad_key, TedAdHelper.ImageProvider imageProvider) {
        super(itemView);
        tedNativeAd = new TedNativeAd(itemView, context, app_name, facebook_ad_key, admob_ad_key, imageProvider);
    }


    public void loadFacebookAD(OnNativeAdListener onNativeAdListener) {
        tedNativeAd.loadFacebookAD(onNativeAdListener);
    }

    public void loadAdmobAD(OnNativeAdListener onNativeAdListener) {
        tedNativeAd.loadAdmobAD(onNativeAdListener);
    }

    public void loadAD(int adPriority, OnNativeAdListener onNativeAdListener) {
        tedNativeAd.loadAD(adPriority, onNativeAdListener);
    }

    public void loadAD(Integer[] tempAdPriorityList, OnNativeAdListener onNativeAdListener) {
        tedNativeAd.loadAD(tempAdPriorityList, onNativeAdListener);
    }

    public void loadAD(ArrayList tempAdPriorityList, OnNativeAdListener onNativeAdListener) {
        tedNativeAd.loadAD(tempAdPriorityList, onNativeAdListener);
    }

    public void onDestroy(){
        tedNativeAd.onDestroy();
    }
}