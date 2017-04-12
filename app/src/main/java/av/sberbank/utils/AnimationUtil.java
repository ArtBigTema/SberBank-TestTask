package av.sberbank.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import av.sberbank.R;

/**
 * Created by Artem on 12.04.2017.
 */

public class AnimationUtil {
    public static void animateReverse(Context context, Button buttonReverse,
                                      Button btnStart, Button btnEnd,
                                      EditText etStart, EditText etEnd) {

        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.button_rotate);
        rotation.setRepeatCount(Animation.RELATIVE_TO_SELF);
        buttonReverse.startAnimation(rotation);

        Animation slideLeft1 = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        rotation.setRepeatCount(Animation.RELATIVE_TO_SELF);
        btnStart.startAnimation(slideLeft1);

        Animation slideRight1 = AnimationUtils.loadAnimation(context, R.anim.slide_right);
        rotation.setRepeatCount(Animation.RELATIVE_TO_SELF);
        btnEnd.startAnimation(slideRight1);

        Animation slideLeft2 = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        rotation.setRepeatCount(Animation.RELATIVE_TO_SELF);
        etStart.startAnimation(slideLeft2);

        Animation slideRight2 = AnimationUtils.loadAnimation(context, R.anim.slide_right);
        rotation.setRepeatCount(Animation.RELATIVE_TO_SELF);
        etEnd.startAnimation(slideRight2);
    }

    public static void animateClear(Context context, View... views) {
        for (View view : views) {
            Animation rotation = AnimationUtils.loadAnimation(context, R.anim.fade);
            rotation.setRepeatCount(Animation.ABSOLUTE);
            view.startAnimation(rotation);
        }
    }
}
