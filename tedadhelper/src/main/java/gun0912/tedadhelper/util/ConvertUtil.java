package gun0912.tedadhelper.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by TedPark on 2017. 3. 7..
 */

public class ConvertUtil {


    public static int dpToPx(Context c, int dp) {
        return (int) (dp * c.getResources().getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(Context c, int px) {
        return (int) (px / c.getResources().getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(double dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


}
