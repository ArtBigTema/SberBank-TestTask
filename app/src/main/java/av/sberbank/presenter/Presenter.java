package av.sberbank.presenter;

import av.sberbank.model.Currency;

/**
 * Created by Artem on 12.04.2017.
 */

public interface Presenter {
    void onResultButtonClick(double startSum, Currency startCurrency, Currency endCurrency);

    void onViewCreated();
}