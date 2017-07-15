# What is TedAdHelper?<br/><br/>

## English
Do you have your application?<br/>
Do you want make money using your application?<br/>
Then, you can add advertise in your application (banner, front ad, native ad, etc..)<br/>

If you want to know specific information, you can check this site.<br/>
- [Facebook Audience Network](https://developers.facebook.com/docs/audience-network)(Recommended)
- [AdMob](https://firebase.google.com/docs/admob)<br/>
Also you can make advertise source code.(Banner/Front AD/Native AD/etc)<br/><br/>
![Screenshot](https://github.com/ParkSangGwon/TedAdHelper/blob/master/Screenshot_demo_1.jpeg?raw=true) 

And if you want use admob,facebook all, you need [mediation](https://www.facebook.com/help/audiencenetwork/1744424245771424?helpref=hc_fnav)<br/>
[Admob](https://support.google.com/admob/answer/3124703) and [adlibr](http://adlibr.com) support mediation service.
### But
They support mediation for only banner,front ad(don't support native ad mediation)<br/>
**TedAdHelper support not only banner,front ad but also native ad, back press popup dialog**<br/>
If you want show back press popup with advertise, you can use back press popup dialog.<br/>
###Back Press Popup Dialog(with mediation)<br/>
![Screenshot](https://github.com/ParkSangGwon/TedAdHelper/blob/master/Screenshot_backpress_en_1.jpeg?raw=true) <br/><br/>

## Korean
TedAdHelper는 광고 퍼블리셔를 위한 라이브러리입니다.<br/>
Admob과 Facebook Audience Network를 사용하면서 2개 광고를 [미디에이션](https://www.facebook.com/help/audiencenetwork/1744424245771424?helpref=hc_fnav)하고 싶었습니다.<br/>
[adlibr](http://adlibr.com)이나 [Admob](https://support.google.com/admob/answer/3124703)을 통해 미디에이션 설정을 할수 있지만 **배너나 전면광고만 미디에이션이 가능하고 Native광고는 미디에이션 할 수 없습니다.**<br/>
**TedAdHelper에서는 Native Ad도 미디에이션 가능합니다.**<br/>
(다만 Native광고의 특성상 라이브러리에서 만든 광고의 레이아웃형태로 밖에 보여줄수 없습니다.<br/>
본인이 원하는 레이아웃으로 보여주고 싶다면 직접 만드셔야 합니다.)<br/>

또한, 앱을 종료할때 광고와 함께 종료팝업을 보여주고자 하는 경우가 있습니다.<br/>
이러한 경우 팝업을 띄우고 몇개의 버튼과 함께 Native 광고를 보여주도록 하면되는데 TedAdHelper를 통해 미디에이션까지 포함하여 보여줄 수 있습니다.<br/>

### 뒤로가기 팝업 Dialog(with 미디에이션)<br/>
![Screenshot](https://github.com/ParkSangGwon/TedAdHelper/blob/master/Screenshot_backpress_ko_1.jpeg?raw=true) 




<br/><br/><br/><br/>

## Setup


### Gradle
```javascript
dependencies {
    compile 'gun0912.ted:tedadhelper:1.0.17'
}

```

<br/><br/>


## How to use

### Test
For test advertise, you have to use test mode.<br/>
If you using live advertise, your advertise id will block from google/facebook.<br/>
So when you test your application, you load test Advertise(using your device ID).<br/>
Your device ID can be obtained by viewing the logcat output after creating a new ad.<br/>
<br/>
Before loading advertise, set test device ID like this.<br/>
- Facebook: `TedAdHelper.setFacebookTestDeviceId("")`
- Admob: `TedAdHelper.setAdmobTestDeviceId("")`
<br/><br/>

### Type
You can use 3 type advertise
- TedAdHelper.AD_FACEBOOK: [Facebook Audience Network](https://developers.facebook.com/docs/audience-network)
- TedAdHelper.AD_ADMOB: [AdMob](https://firebase.google.com/docs/admob)<br/>
- TedAdHelper.AD_TNK: [TNK Factory](https://http://www.tnkfactory.com/tnk/en/home.front.main)<br/>

If you use TNK factory's advertise, you have declare code in your `AndroidManifest.xml`
- `<meta-data android:name="tnkad_app_id"   android:value=""/>`
<br/><br/>
### Banner
1. Make banner container in xml
```javascript
 <FrameLayout
        android:id="@+id/bannerContainer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        />
```
<br/>
2. Show banner using `TedAdBanner`<br/>
```javascript
TedAdBanner.showBanner(ViewGroup bannerContainer, String facebookKey, String admobKey, int adPriority, OnBannerAdListener onBannerAdListener)
```
* **adPriority:** TedAdHelper.AD_FACEBOOK / TedAdHelper.AD_ADMOB<br/>


```javascript

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
            }

        });

```
<br/><br/><br/><br/>

### Front AD
```javascript
TedAdFront.showFrontAD(Context context, String facebookKey, final String admobKey, Integer[] adPriorityList, OnFrontAdListener onFrontAdListener)
```
* **adPriorityList:** TedAdHelper.AD_FACEBOOK / TedAdHelper.AD_ADMOB / TedAdHelper.AD_TNK<br/>

```javascript

        /**
         * Front AD
         */

        //TedAdFront.showAdmobFrontAd();
        //TedAdFront.showFacebookFrontAd();
        TedAdFront.showFrontAD(this, FACEBOOK_KEY_FRONT, ADMOB_KEY_FRONT, new Integer[]{TedAdHelper.AD_ADMOB,TedAdHelper.AD_FACEBOOK,TedAdHelper.AD_TNK}, new OnFrontAdListener() {
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
            }
        });

```
<br/><br/><br/><br/>

### BackPress Poup Dialog
![Screenshot](https://github.com/ParkSangGwon/TedAdHelper/blob/master/Screenshot_backpress_en_1.jpeg?raw=true)
```javascript
TedBackPressDialog.startDialog(Activity activity, String appName, String facebookKey, String admobKey, Integer[] adPriorityList, OnBackPressListener onBackPressListener)
```
* **adPriorityList:** TedAdHelper.AD_FACEBOOK / TedAdHelper.AD_ADMOB / TedAdHelper.AD_TNK<br/>
```javascript

    @Override
    public void onBackPressed() {

        //TedBackPressDialog.startFacebookDialog();
        //TedBackPressDialog.startAdmobDialog();
        TedBackPressDialog.startDialog(this, getString(R.string.app_name), FACEBOOK_KEY_BACKPRESS, ADMOB_KEY_BACKPRESS, TedAdHelper.AD_FACEBOOK, new OnBackPressListener() {
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

```

<br/><br/><br/><br/>
### Native AD
1. Make your Native ad container and include `adview_native_base.xml`

```javascript
    <android.support.v7.widget.CardView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/cardview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"

        app:cardBackgroundColor="@color/white"
        app:cardCornerRadius="2dp"
        app:cardElevation="5dp"
        app:cardUseCompatPadding="true">

        <include layout="@layout/adview_native_base"/>

    </android.support.v7.widget.CardView>
```
2. Make instance and Show Native ad using `TedNativeAdHolder`

```javascript
        View cardview = findViewById(R.id.cardview);
        TedNativeAdHolder tedNativeAdHolder = new TedNativeAdHolder(cardview, this, getString(R.string.app_name), FACEBOOK_KEY_NATIVE, ADMOB_KEY_NATIVE);

        tedNativeAdHolder.loadAD(new Integer[]{TedAdHelper.AD_FACEBOOK,TedAdHelper.AD_ADMOB}, new OnNativeAdListener() {
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

```



<br/><br/><br/><br/>
## Customize
### Color
You can change button or divider color.
Override variable in your `colors.xml`
```javascript
    <color name="tedBtnPrimary">...</color>
    <color name="tedBtnHighlight">...</color>
```

### Check Facebook app
If you want request facebook advertise for only facebook app installed user, you can use `showAdOnlyFacebookInstalledUser()`.
Library check facebook app installed or not.
```javascript
TedAdHelper.showAdOnlyFacebookInstalledUser(true);
```
<br/><br/><br/><br/>

## FAQ
### 1. I got error message `leaked IntentReceiver com.facebook.ads.internal.DisplayAdController$c@cf9db8c that was originally registered here. Are you missing a call to unregisterReceiver()?`<br/>
If you use Facebook Audience Network, you have to destroy your banner or front ad  
```javascript
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

```

You can get `facebookFrontAD`,`facebookBanner` instance from `onFacebookAdCreated(InterstitialAd facebookFrontAD)` method
```javascript
  TedAdFront.showFrontAD(this, FACEBOOK_KEY_FRONT, ADMOB_KEY_FRONT, TedAdHelper.AD_ADMOB, new OnFrontAdListener() {
           
           ...

            @Override
            public void onFacebookAdCreated(InterstitialAd facebookFrontAD) {
                MainActivity.this.facebookFrontAD = facebookFrontAD;
            }
        });
```
<br/><br/>
### 2. I want change native ad layout
If you want customize your native ad, you have to overwrite view id.<br/>
Make your layout (not include `<include layout="@layout/adview_native_base"/>` code)<br/>
You have to declare view id
- `R.id.view_root (RelativeLayout)`: If Native ad load fail, this view will disappear
- `R.id.container_admob_express (container_admob_express)`: For add admobExpress
- `R.id.progressView (ProgressBar)`: When advertise loading, this view will appear
- `R.id.view_container (LinearLayout)`: when advertise loaded, this view will appear
- `R.id.iv_logo (ImageView)`
- `R.id.tv_name (TextView)`
- `R.id.native_ad_media (com.facebook.ads.MediaView)`
- `R.id.iv_image (ImageView)`
- `R.id.tv_body (TextView)`
- `R.id.tv_call_to_action (TextView)`
- `R.id.tv_etc (TextView)`

<br/><br/>


## License 
 ```code
Copyright 2017 Ted Park

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.```

