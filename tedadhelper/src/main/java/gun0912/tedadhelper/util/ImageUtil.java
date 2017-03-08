package gun0912.tedadhelper.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;


/**
 * Created by TedPark on 15. 11. 6..
 */
public class ImageUtil {




    public static void loadImage( ImageView imageView, String url) {
        loadImage( imageView, url, false, false, -1, -1, null);
    }

    public static void loadImage( ImageView imageView, int res_id, RequestListener callback) {

        Glide.with(imageView.getContext()).load(res_id).dontAnimate().listener(callback).into(imageView);

    }


    public static void loadImage( ImageView imageView, String url, boolean thumbnail, boolean centerCrop, int holder_resId, int err_resId, RequestListener callback) {

        Context context = imageView.getContext();

        if (context == null)
            return;

        if (context instanceof Activity) {

            if (((Activity) context).isFinishing())
                return;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed())
                return;


        }


        try {
            if (!checkWebUrl( imageView, url, err_resId, callback))
                return;



            DrawableTypeRequest request = Glide.with(context).load(url);




            if (holder_resId > 0) {
                request.placeholder(holder_resId);
            } else {
                // request.placeholder(R.drawable.place_holder_default);
            }

            if (err_resId > 0) {
                request.error(err_resId);
            }

            if (thumbnail) {
                request.thumbnail(0.1f);
            }

            if (centerCrop) {
                request.centerCrop();
            } else {
                //    request.fitCenter();
            }


            if (url.endsWith(".gif")) {

                Glide.with(context)
                        .load(url)
                        .asGif()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .priority(Priority.IMMEDIATE)
                        .into(imageView);
            } else {
                request.asBitmap();
            }


            if (callback != null) {
                request.listener(callback);
            }

            request.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .priority(Priority.IMMEDIATE)
                    .dontAnimate()
                    .into(imageView);


        } catch (Exception e) {

        }


    }

    private static boolean checkWebUrl( ImageView imageView, String url, int err_resId, RequestListener callback) {

        Context context = imageView.getContext();

        try {

            // 빈 주소 체크
            if (TextUtils.isEmpty(url)) {

                Glide.with(context).load(err_resId)
                        .dontAnimate()
                        .listener(callback)
                        .into(imageView);

                return false;
            }
            // 웹주소가 아닌지 체크
            else if (!url.startsWith("http")) {


                Glide.with(context).load(url).error(err_resId)
                        .dontAnimate()
                        .listener(callback)
                        .into(imageView);
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;


    }




}
