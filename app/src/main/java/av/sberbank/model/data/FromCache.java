package av.sberbank.model.data;

import android.content.Context;

import java.util.Collections;

import av.sberbank.model.Currency;
import av.sberbank.model.db.DBHelper;

/**
 * Created by Artem on 12.04.2017.
 */

public class FromCache implements Request {
    private  Context context;
    private DBHelper dbHelper;

    FromCache(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context, Collections.<Class>singletonList(Currency.class));
    }
    @Override
    public void requestData() {

    }
}
