package av.sberbank.model.data;

import android.content.Context;
import android.os.Handler;

import java.util.Collections;
import java.util.List;

import av.sberbank.model.Currency;
import av.sberbank.model.db.DBHelper;

/**
 * Created by Artem on 12.04.2017.
 */

public abstract class FromCache implements Request {
    private Context context;
    private DBHelper dbHelper;

    public FromCache(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context, Collections.<Class>singletonList(Currency.class));
    }

    @Override
    public void requestData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Currency> currencies = dbHelper.getAll(Currency.class);

                if (currencies.size() < 1) {
                    currencies = Collections.singletonList(Currency.getRuble());
                }//fixme if empty put Ruble

                notifyDataReceived(currencies);
            }
        }).run();
    }

    private void notifyDataReceived(final List<Currency> currencies) {
        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                onCurrenciesAccepted(currencies);
            }
        };
        mainHandler.post(myRunnable);
    }

    public void saveInCache(List<Currency> currencies) {
        dbHelper.clearTable(Currency.class);
        dbHelper.addAll(currencies, Currency.class);
    }

    public abstract void onCurrenciesAccepted(List<Currency> currencies);
}
