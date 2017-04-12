package av.sberbank.model.data;

import android.content.Context;

/**
 * Created by Artem on 12.04.2017.
 */

public class FromInternet implements Request {

    private final Context context;

    FromInternet(Context context) {
        this.context = context;
    }

    @Override
    public void requestData() {

    }
}