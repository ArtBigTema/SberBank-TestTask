package av.sberbank.model.data;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import av.sberbank.model.Currencies;
import av.sberbank.model.Currency;
import av.sberbank.utils.Constant;

/**
 * Created by Artem on 12.04.2017.
 */

public abstract class FromInternet implements Request {
    private static final String TAG = FromInternet.class.getName();

    private final Context context;

    public FromInternet(Context context) {
        this.context = context;
    }

    @Override
    public void requestData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(Constant.CBR_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(Constant.TIMEOUT);
                    parseXml(urlConnection.getInputStream());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    onException();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void parseXml(InputStream inputStream) {
        try {
            Serializer serializer = new Persister();
            Currencies currencies = serializer.read(Currencies.class, inputStream);
            notifyDataAccepted(currencies.getCurrencies());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void notifyDataAccepted(final List<Currency> currencies) {
        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                onCurrenciesAccepted(currencies);
            }
        };
        mainHandler.post(myRunnable);
    }

    public abstract void onCurrenciesAccepted(List<Currency> currencies);

    public abstract void onException();
}