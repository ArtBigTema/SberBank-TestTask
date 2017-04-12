package av.sberbank.view;

import android.content.Context;

import java.util.List;

import av.sberbank.model.Currency;

/**
 * Created by Artem on 12.04.2017.
 */

public interface CurrenciesView {
    void showListCurrencies(final List<Currency> currencies);

    void endCalculate(double endSum);

    Context getContextPresenter();
}