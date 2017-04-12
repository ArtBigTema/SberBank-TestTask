package av.sberbank.presenter;

import android.content.Context;

import java.util.List;

import av.sberbank.model.Currency;
import av.sberbank.model.Model;
import av.sberbank.model.ModelImpl;
import av.sberbank.view.CurrenciesView;

/**
 * Created by Artem on 12.04.2017.
 */

public class PresenterImpl implements Presenter {
    private Model model;
    private CurrenciesView currenciesView;

    public PresenterImpl(final CurrenciesView currenciesView) {
        this.currenciesView = currenciesView;

        model = new ModelImpl() {
            @Override
            public Context getContext() {
                return currenciesView.getContextPresenter();
            }

            @Override
            public void onCurrenciesAccepted(List<Currency> currencies) {
                currenciesView.showListCurrencies(currencies);
            }
        };
    }

    @Override
    public void calculate(double sum, Currency startCurrency, Currency endCurrency) {
        double startValue = startCurrency.getValue() / startCurrency.getNominal();
        double endValue = endCurrency.getValue() / endCurrency.getNominal();
        double result = sum * (startValue / endValue);
        currenciesView.endCalculate(result);
    }

    @Override
    public void refreshData() {
        model.requestCurrencies();
    }
}