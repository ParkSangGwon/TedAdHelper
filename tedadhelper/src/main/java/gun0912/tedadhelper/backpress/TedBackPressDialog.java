package gun0912.tedadhelper.backpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import gun0912.tedadhelper.R;
import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.nativead.OnNativeAdListener;
import gun0912.tedadhelper.nativead.TedNativeAdHolder;
import gun0912.tedadhelper.util.AppUtil;
import gun0912.tedadhelper.util.SharedPreferenceUtil;


/**
 * Created by TedPark on 16. 5. 22..
 */
public class TedBackPressDialog extends AppCompatActivity {

    private static final String EXTRA_APP_NAME = "app_name";
    private static final String EXTRA_FACEBOOK_KEY = "facebook_key";
    private static final String EXTRA_ADMOB_KEY = "admob_key";
    private static final String EXTRA_AD_PRIORITY = "ad_priority";
    private static OnBackPressListener onBackPressListener;
    View adview_container;
    TextView tvFinish;
    TextView tvReview;
    String appName;
    String facebookKey;
    String admobKey;
    int adPriority;


    public static void startFacebookDialog(Activity activity, String appName, String facebookKey, OnBackPressListener onBackPressListener) {
        startDialog(activity, appName, facebookKey, null, TedAdHelper.AD_FACEBOOK, onBackPressListener);
    }

    public static void startDialog(Activity activity, String appName, String facebookKey, String admobKey, int adPriority, OnBackPressListener onBackPressListener) {
        Intent intent = new Intent(activity, TedBackPressDialog.class);
        intent.putExtra(EXTRA_APP_NAME, appName);
        intent.putExtra(EXTRA_FACEBOOK_KEY, facebookKey);
        intent.putExtra(EXTRA_ADMOB_KEY, admobKey);
        intent.putExtra(EXTRA_AD_PRIORITY, adPriority);


        if (onBackPressListener == null) {
            throw new RuntimeException("OnBackPressListener can not null");
        }
        TedBackPressDialog.onBackPressListener = onBackPressListener;

        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void startAdmobDialog(Activity activity, String appName, String admobKey, OnBackPressListener onBackPressListener) {
        startDialog(activity, appName, null, admobKey, TedAdHelper.AD_ADMOB, onBackPressListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setupFromSavedInstanceState(savedInstanceState);

        setContentView(R.layout.dialog_backpress);


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%
        getWindow().getAttributes().width = width;


        setFinishOnTouchOutside(false);

        initView();

        TedNativeAdHolder adViewNativeAdHolder
                = new TedNativeAdHolder(adview_container, this, appName, facebookKey, admobKey);

        adViewNativeAdHolder.loadAD(adPriority, new OnNativeAdListener() {
            @Override
            public void onError(String errorMessage) {

                if (onBackPressListener != null) {
                    onBackPressListener.onError(errorMessage);
                }
            }

            @Override
            public void onLoaded(int adType) {
                if (onBackPressListener != null) {
                    onBackPressListener.onLoaded(adType);
                }
            }

            @Override
            public void onAdClicked(int adType) {
                if(onBackPressListener!=null){
                    onBackPressListener.onAdClicked(adType);
                }
            }
        });


        checkReview();


    }

    private void checkReview() {

        boolean isReview = SharedPreferenceUtil.getSharedPreference(this, SharedPreferenceUtil.REVIEW, false);
        if (isReview) {
            tvReview.setVisibility(View.GONE);
        }


    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {


        // 액티버티를 처음 생성중인지, 재생성 중인지 확인한다.
        if (savedInstanceState != null) {
            // 저장된 상태를 복구한다.
            appName = savedInstanceState.getString(EXTRA_APP_NAME);
            facebookKey = savedInstanceState.getString(EXTRA_FACEBOOK_KEY);
            admobKey = savedInstanceState.getString(EXTRA_ADMOB_KEY);
            adPriority = savedInstanceState.getInt(EXTRA_AD_PRIORITY);

        } else {

            // 새 객체를 위해 멤버 변수를 초기화 한다.
            appName = getIntent().getStringExtra(EXTRA_APP_NAME);
            facebookKey = getIntent().getStringExtra(EXTRA_FACEBOOK_KEY);
            admobKey = getIntent().getStringExtra(EXTRA_ADMOB_KEY);
            adPriority = getIntent().getIntExtra(EXTRA_AD_PRIORITY, 0);


        }


    }

    private void initView() {

        adview_container = findViewById(R.id.adview_container);


        tvReview = (TextView) findViewById(R.id.tv_review);
        tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReviewClick();
            }
        });
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(0, 0);

                if (onBackPressListener != null) {
                    onBackPressListener.onFinish();
                }

            }
        });

    }

    public void onReviewClick() {

        AppUtil.openPlayStore(this, getPackageName());
        SharedPreferenceUtil.putSharedPreference(this, SharedPreferenceUtil.REVIEW, true);

        if (onBackPressListener != null) {
            onBackPressListener.onReviewClick();
        }

    }

    public void onFinishClick() {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // 현재 상태를 저장한다.
        savedInstanceState.putString(EXTRA_APP_NAME, appName);
        savedInstanceState.putString(EXTRA_FACEBOOK_KEY, facebookKey);
        savedInstanceState.putString(EXTRA_ADMOB_KEY, admobKey);
        savedInstanceState.putInt(EXTRA_AD_PRIORITY, adPriority);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }


}
