package gun0912.tedadhelper.nativead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoOptions;
import com.tnkfactory.ad.NativeAdItem;
import com.tnkfactory.ad.NativeAdListener;
import com.tnkfactory.ad.TnkSession;

import java.util.ArrayList;
import java.util.Arrays;

import gun0912.tedadhelper.R;
import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.util.Constant;
import gun0912.tedadhelper.util.ConvertUtil;
import gun0912.tedadhelper.util.ImageUtil;


/**
 * Created by TedPark on 16. 5. 19..
 */

public class TedNativeAdHolder extends RecyclerView.ViewHolder {


    ArrayList<Integer> adPriorityList;
    String app_name;
    String facebook_ad_key;
    String admob_ad_key;
    Context context;
    // NativeAppInstallAdView admobAppInstallRootView;
    // NativeContentAdView admobContentRootView;
    ViewGroup container_admob_express;
    ProgressBar progressView;
    RelativeLayout view_root;
    LinearLayout view_container;
    ImageView ivLogo;
    TextView tvName;
    TextView tvBody;
    MediaView nativeAdMedia;
    ImageView ivImage;
    TextView tvEtc;
    TextView tvCallToAction;
    com.facebook.ads.NativeAd facebookAd;
    OnNativeAdListener onNativeAdListener;
    ViewGroup view_ad_choice;

    TedAdHelper.ImageProvider imageProvider;

    public TedNativeAdHolder(View itemView, Context context, String app_name, String facebook_ad_key, String admob_ad_key) {

        super(itemView);
        initField(context, app_name, facebook_ad_key, admob_ad_key, null);
        initView();
    }

    private void initField(Context context, String app_name, String facebook_ad_key, String admob_ad_key, TedAdHelper.ImageProvider imageProvider) {
        this.context = context;
        this.app_name = app_name;
        this.facebook_ad_key = facebook_ad_key;
        this.admob_ad_key = admob_ad_key;
        this.imageProvider = imageProvider;
    }

    private void initView() {
        view_root = (RelativeLayout) itemView.findViewById(R.id.view_root);
        //admobAppInstallRootView = (NativeAppInstallAdView) itemView.findViewById(R.id.admobAppInstallRootView);
        // admobContentRootView = (NativeContentAdView) itemView.findViewById(R.id.admobContentRootView);


        container_admob_express = (ViewGroup) itemView.findViewById(R.id.container_admob_express);
        progressView = (ProgressBar) itemView.findViewById(R.id.progressView);
        view_container = (LinearLayout) itemView.findViewById(R.id.view_container);
        // Create native UI using the ad metadata.
        ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvBody = (TextView) itemView.findViewById(R.id.tv_body);
        nativeAdMedia = (MediaView) itemView.findViewById(R.id.native_ad_media);
        ivImage = (ImageView) itemView.findViewById(R.id.iv_image);

        tvEtc = (TextView) itemView.findViewById(R.id.tv_etc);
        tvCallToAction = (TextView) itemView.findViewById(R.id.tv_call_to_action);
        view_ad_choice = (ViewGroup) itemView.findViewById(R.id.view_ad_choice);

    }

    public TedNativeAdHolder(View itemView, Context context, String app_name, String facebook_ad_key, String admob_ad_key, TedAdHelper.ImageProvider imageProvider) {
        super(itemView);
        initField(context, app_name, facebook_ad_key, admob_ad_key, imageProvider);
        initView();

    }

    public void onDestroy() {
        if (nativeAdMedia != null) {
            nativeAdMedia.destroy();
        }
        if (facebookAd != null) {
            facebookAd.destroy();
        }

    }

    public void loadFacebookAD(OnNativeAdListener onNativeAdListener) {
        loadAD(TedAdHelper.AD_FACEBOOK, onNativeAdListener);
    }

    public void loadAdmobAD(OnNativeAdListener onNativeAdListener) {
        loadAD(TedAdHelper.AD_ADMOB, onNativeAdListener);
    }

    public void loadAD(int adPriority, OnNativeAdListener onNativeAdListener) {
        Integer[] tempAdPriorityList = new Integer[2];
        tempAdPriorityList[0] = adPriority;
        if (adPriority == TedAdHelper.AD_FACEBOOK) {
            tempAdPriorityList[1] = TedAdHelper.AD_ADMOB;
        } else {
            tempAdPriorityList[1] = TedAdHelper.AD_FACEBOOK;
        }
        loadAD(tempAdPriorityList, onNativeAdListener);
    }

    public void loadAD(Integer[] tempAdPriorityList, OnNativeAdListener onNativeAdListener) {
        if (tempAdPriorityList == null || tempAdPriorityList.length == 0) {
            if (onNativeAdListener != null) {
                onNativeAdListener.onError("You have to select priority type ADMOB/FACEBOOK/TNK");
            }
            return;
        }
        ArrayList resultTempAdPriorityList = new ArrayList<>(Arrays.asList(tempAdPriorityList));
        loadAD(resultTempAdPriorityList, onNativeAdListener);

    }

    public void loadAD(ArrayList tempAdPriorityList, OnNativeAdListener onNativeAdListener) {
        this.onNativeAdListener = onNativeAdListener;

        if (tempAdPriorityList == null || tempAdPriorityList.size() == 0) {
            if (onNativeAdListener != null) {
                onNativeAdListener.onError("You have to select priority type ADMOB/FACEBOOK/TNK");
            }
            return;
        }
        adPriorityList = tempAdPriorityList;

        try {
            selectAd();
        } catch (Exception e) {
            e.printStackTrace();
            if (onNativeAdListener != null) {
                onNativeAdListener.onError(e.toString());
            }
        }

    }

    private void selectAd() {
        view_ad_choice.removeAllViews();

        int adPriority = adPriorityList.remove(0);
        switch (adPriority) {
            case TedAdHelper.AD_FACEBOOK:
                loadFacebookAD();
                break;
            case TedAdHelper.AD_ADMOB:
                loadAdmobExpressAD();
                break;
            case TedAdHelper.AD_TNK:
                loadTnkAD();
                break;
            default:
                onNativeAdListener.onError("You have to select priority type ADMOB or FACEBOOK");
        }
    }

    private void onLoadAdError(String errorMessage) {
        if (adPriorityList.size() > 0) {
            //loadAdmobAdvanceAD(false);
            selectAd();
        } else {
            view_root.setVisibility(View.GONE);

            if (onNativeAdListener != null) {
                onNativeAdListener.onError(errorMessage);
            }

        }
    }

    private void loadAdmobExpressAD() {
        progressView.setVisibility(View.VISIBLE);
        view_container.setVisibility(View.INVISIBLE);
        final NativeExpressAdView admobExpressAdView = new NativeExpressAdView(context);

        // Set its video options.
        admobExpressAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());


        // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
        // loading.
        admobExpressAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TedAdHelper.TAG, "[ADMOB NATIVE AD]Loaded");

                view_container.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);

                if (onNativeAdListener != null) {
                    onNativeAdListener.onLoaded(TedAdHelper.AD_ADMOB);
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                super.onAdFailedToLoad(errorCode);

                String errorMessage = TedAdHelper.getMessageFromAdmobErrorCode(errorCode);
                Log.e(TedAdHelper.TAG, "[ADMOB NATIVE AD]errorMessage: " + errorMessage);
                onLoadAdError(errorMessage);
            }

            @Override
            public void onAdOpened() {
                Log.d(TedAdHelper.TAG, "[ADMOB NATIVE AD]Opend");
                super.onAdOpened();
                if (onNativeAdListener != null) {
                    onNativeAdListener.onAdClicked(TedAdHelper.AD_ADMOB);
                }
            }

        });

        container_admob_express.post(new Runnable() {
            @Override
            public void run() {

                int realWidth = ConvertUtil.pxToDp(context, container_admob_express.getWidth());


                int realHeight = 320 * realWidth / 360;
                if (admobExpressAdView.getAdUnitId() == null) {
                    admobExpressAdView.setAdUnitId(admob_ad_key);
                }

                if (admobExpressAdView.getAdSize() == null) {
                    admobExpressAdView.setAdSize(new AdSize(realWidth, realHeight));
                }

                //admobExpressAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH,320));


                for (int i = 0; i < container_admob_express.getChildCount(); i++) {
                    if (container_admob_express.getChildAt(i) instanceof NativeExpressAdView) {
                        NativeExpressAdView temp = (NativeExpressAdView) container_admob_express.getChildAt(i);
                        temp.destroy();
                    }
                }
                container_admob_express.removeAllViews();
                container_admob_express.addView(admobExpressAdView);


                admobExpressAdView.loadAd(TedAdHelper.getAdRequest());

            }

        });

        /*
        ViewTreeObserver viewTreeObserver = container_admob_express.getViewTreeObserver();

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
container_admob_express.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });
*/

    }

    private void loadTnkAD() {
        progressView.setVisibility(View.VISIBLE);
        view_container.setVisibility(View.INVISIBLE);
        TnkSession.prepareNativeAd(context, TnkSession.CPC, NativeAdItem.STYLE_LANDSCAPE, new NativeAdListener() {

            @Override
            public void onFailure(int errCode) {

                String errorMessage = TedAdHelper.getMessageFromTnkErrorCode(errCode);
                Log.e(TedAdHelper.TAG, "[TANK NATIVE AD]" + errorMessage);
                onLoadAdError(errorMessage);
            }

            @Override
            public void onLoad(NativeAdItem adItem) {
                Log.d(TedAdHelper.TAG, "[TANK NATIVE AD]onLoad");
                bindTnkAD(adItem);
                if (onNativeAdListener != null) {
                    onNativeAdListener.onLoaded(TedAdHelper.AD_FACEBOOK);
                }
            }

            @Override
            public void onClick() {
                Log.d(TedAdHelper.TAG, "[TANK NATIVE AD]onClick");

                if (onNativeAdListener != null) {
                    onNativeAdListener.onAdClicked(TedAdHelper.AD_FACEBOOK);
                }
            }

            @Override
            public void onShow() {
                Log.d(TedAdHelper.TAG, "[TANK NATIVE AD]onShow");
            }
        });
    }
/*

    private void loadAdmobAdvanceAD() {

        AdLoader.Builder builder = new AdLoader.Builder(context, admob_ad_key);
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
                bindAdmobAppInstallAD(nativeAppInstallAd);
            }
        });

        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd nativeContentAd) {
                bindAdmobContentAD(nativeContentAd);
            }
        });


        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();
        builder.withNativeAdOptions(adOptions);


        AdLoader adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);


                if (adPriorityList.size()>0) {
                    selectAd();
                } else {
                    view_root.setVisibility(View.GONE);

                    if(onNativeAdListener!=null){
                        String errorMessage = TedAdHelper.getMessageFromAdmobErrorCode(errorCode);
                        onNativeAdListener.onError(errorMessage);
                    }

                }

            }


        }).build();


        AdRequest.Builder requestBuilder = new AdRequest.Builder();
        // requestBuilder.addTestDevice("4126DBFAA8B1B6F71AB0DF06CD69F9E3");

        adLoader.loadAd(requestBuilder.build());

    }

*/

    private void loadFacebookAD() {

        if (TedAdHelper.isSkipFacebookAd(context)) {
            Log.e(TedAdHelper.TAG, "[FACEBOOK NATIVE AD]Error: " + Constant.ERROR_MESSAGE_FACEBOOK_NOT_INSTALLED);
            onLoadAdError(Constant.ERROR_MESSAGE_FACEBOOK_NOT_INSTALLED);
            return;
        }

        progressView.setVisibility(View.VISIBLE);
        view_container.setVisibility(View.INVISIBLE);

        facebookAd = new com.facebook.ads.NativeAd(context, facebook_ad_key);


        facebookAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                Log.e(TedAdHelper.TAG, "[FACEBOOK NATIVE AD]Error: " + error.getErrorMessage());
                onLoadAdError(error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK NATIVE AD]Loaded");
                bindFacebookAD(ad);

                if (onNativeAdListener != null) {
                    onNativeAdListener.onLoaded(TedAdHelper.AD_FACEBOOK);
                }

            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TedAdHelper.TAG, "[FACEBOOK NATIVE AD]Clicked");
                if (onNativeAdListener != null) {
                    onNativeAdListener.onAdClicked(TedAdHelper.AD_FACEBOOK);
                }

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }


        });
        facebookAd.loadAd();
    }

    private void bindTnkAD(NativeAdItem adItem) {

        MyNativeAd myNativeAd = new MyNativeAd();

        myNativeAd.setLogoUrl(adItem.getIconUrl());
        myNativeAd.setName(adItem.getTitle());
        myNativeAd.setImageUrl(adItem.getCoverImageUrl());
        myNativeAd.setBody(adItem.getDescription());
        bindNativeAd(myNativeAd);

        adItem.attachLayout(view_root);
    }

    private void bindFacebookAD(Ad ad) {

        if (facebookAd != ad) {
            view_root.setVisibility(View.GONE);
            return;
        }

        MyNativeAd myNativeAd = new MyNativeAd();

        if (facebookAd.getAdIcon() != null) {
            myNativeAd.setLogoUrl(facebookAd.getAdIcon().getUrl());
        }

        myNativeAd.setName(facebookAd.getAdTitle());

        if (facebookAd.getAdCoverImage() != null) {
            myNativeAd.setImageUrl(facebookAd.getAdCoverImage().getUrl());
            nativeAdMedia.setVisibility(View.GONE);
        }

        myNativeAd.setBody(facebookAd.getAdBody());
        myNativeAd.setCallToAction(facebookAd.getAdCallToAction());
        myNativeAd.setEtc(facebookAd.getAdSocialContext());

        bindNativeAd(myNativeAd);

        nativeAdMedia.setNativeAd(facebookAd);

        AdChoicesView adChoicesView = new AdChoicesView(context, facebookAd, true);
        view_ad_choice.addView(adChoicesView);

        facebookAd.unregisterView();
        facebookAd.registerViewForInteraction(view_root);


    }

    private void bindNativeAd(MyNativeAd myNativeAd) {

        // 로고
        String logoUrl = myNativeAd.getLogoUrl();

        if (imageProvider == null) {
            ImageUtil.loadImage(ivLogo, logoUrl);
        } else {
            imageProvider.onProvideImage(ivLogo, logoUrl);
        }


        // 이름
        String name = myNativeAd.getName();
        if (TextUtils.isEmpty(name)) {
            name = app_name + " 광고";
        }

        tvName.setText(name);

        // 이미지
        String imageUrl = myNativeAd.getImageUrl();
        if (imageProvider == null) {
            ImageUtil.loadImage(ivImage, imageUrl);
        } else {
            imageProvider.onProvideImage(ivImage, imageUrl);
        }


        // 설명
        String body = myNativeAd.getBody();
        tvBody.setText(body);


        // 액션이름 및 기타
        String etc = myNativeAd.getEtc();
        if (TextUtils.isEmpty(etc)) {
            tvEtc.setVisibility(View.GONE);
        } else {
            tvEtc.setVisibility(View.VISIBLE);
            tvEtc.setText(etc);
        }


        String callToAction = myNativeAd.getCallToAction();
        if (TextUtils.isEmpty(callToAction)) {
            tvCallToAction.setVisibility(View.GONE);
        } else {
            tvCallToAction.setVisibility(View.VISIBLE);
            tvCallToAction.setText(myNativeAd.getCallToAction());
        }


        view_root.setVisibility(View.VISIBLE);
        view_container.setVisibility(View.VISIBLE);
        container_admob_express.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
    }



/*


    private void bindAdmobContentAD(NativeContentAd nativeContentAd) {

        MyNativeAd myNativeAd = new MyNativeAd();

        myNativeAd.setLogoUrl(nativeContentAd.getLogo().getUri().toString());
        myNativeAd.setName(nativeContentAd.getHeadline().toString());

        List<NativeAd.Image> images = nativeContentAd.getImages();
        if (images.size() > 0) {
            myNativeAd.setImageUrl(nativeContentAd.getImages().get(0).getUri().toString());
        }

        myNativeAd.setBody(nativeContentAd.getBody().toString());
        myNativeAd.setCallToAction(nativeContentAd.getCallToAction().toString());
        myNativeAd.setEtc(nativeContentAd.getAdvertiser().toString());

        bindNativeAd(myNativeAd);

        admobContentRootView.setNativeAd(nativeContentAd);

    }

    private void bindAdmobAppInstallAD(NativeAppInstallAd nativeAppInstallAd) {

        MyNativeAd myNativeAd = new MyNativeAd();

        myNativeAd.setLogoUrl(nativeAppInstallAd.getIcon().getUri().toString());
        myNativeAd.setName(nativeAppInstallAd.getHeadline().toString());

        List<NativeAd.Image> images = nativeAppInstallAd.getImages();
        if (images.size() > 0) {
            myNativeAd.setImageUrl(nativeAppInstallAd.getImages().get(0).getUri().toString());
        }

        myNativeAd.setBody(nativeAppInstallAd.getBody().toString());
        myNativeAd.setCallToAction(nativeAppInstallAd.getCallToAction().toString());
        myNativeAd.setEtc(nativeAppInstallAd.getStore().toString());

        bindNativeAd(myNativeAd);

        admobAppInstallRootView.setNativeAd(nativeAppInstallAd);

    }
*/


    class MyNativeAd {
        String logoUrl;
        String name;
        String imageUrl;
        String body;
        String callToAction;
        String etc;

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getCallToAction() {
            return callToAction;
        }

        public void setCallToAction(String callToAction) {
            this.callToAction = callToAction;
        }

        public String getEtc() {
            return etc;
        }

        public void setEtc(String etc) {
            this.etc = etc;
        }
    }
}