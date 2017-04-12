package av.sberbank.model.data;

import java.util.List;

import av.sberbank.model.Currency;

/**
 * Created by Artem on 12.04.2017.
 */

public interface ResponseListener {
    void onCurrencyAccepted(List<Currency> currencies);
}