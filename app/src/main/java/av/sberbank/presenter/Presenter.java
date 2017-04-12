package av.sberbank.presenter;

import av.sberbank.model.Currency;

/**
 * Created by Artem on 12.04.2017.
 */

public interface Presenter {
    void calculate(double startSum, Currency startCurrency, Currency endCurrency);

    void refreshData();
}