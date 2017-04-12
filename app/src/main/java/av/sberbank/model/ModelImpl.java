package av.sberbank.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.List;

import av.sberbank.model.data.ResponseListener;
import av.sberbank.service.InternetService;

/**
 * Created by Artem on 12.04.2017.
 */

public abstract class ModelImpl implements Model {
    private ServiceConnection connection;
    private ResponseListener listener;

    protected ModelImpl() {
        listener = new ResponseListener() {
            @Override
            public void onCurrencyAccepted(List<Currency> currencies) {
                getContext().unbindService(connection);
                onCurrenciesAccepted(currencies);
            }
        };


        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                InternetService service = InternetService.DataBinder.class.cast(binder).getInternetService();
                service.requeryCurrencies(listener);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
    }

    @Override
    public void requestCurrencies() {
        Intent intent = new Intent(getContext(), InternetService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public abstract Context getContext();

    public abstract void onCurrenciesAccepted(List<Currency> currencies);
}