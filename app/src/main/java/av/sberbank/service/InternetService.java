package av.sberbank.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;

import av.sberbank.model.Currency;
import av.sberbank.model.data.FromCache;
import av.sberbank.model.data.FromInternet;
import av.sberbank.model.data.Request;
import av.sberbank.model.data.ResponseListener;
import av.sberbank.utils.NetworkUtils;

/**
 * Created by Artem on 12.04.2017.
 */

public class InternetService extends Service {
    private DataBinder dataBinder = new DataBinder();

    private FromCache cache;
    private FromInternet internet;
    private ResponseListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        internet = new FromInternet(this) {
            @Override
            public void onCurrenciesAccepted(List<Currency> currencies) {
                listener.onCurrencyAccepted(currencies);
                cache.saveInCache(currencies);
            }

            @Override
            public void onException() {
                cache.requestData();
            }
        };

        cache = new FromCache(this) {
            @Override
            public void onCurrenciesAccepted(List<Currency> currencies) {
                listener.onCurrencyAccepted(currencies);
            }
        };
    }

    public void requeryCurrencies(ResponseListener listener) {
        this.listener = listener;
        Request request;
        if (NetworkUtils.isConnected(this)) {
            request = internet;
        } else {
            request = cache;
        }
        request.requestData();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return dataBinder;
    }

    public class DataBinder extends Binder {
        public InternetService getInternetService() {
            return InternetService.this;
        }
    }
}