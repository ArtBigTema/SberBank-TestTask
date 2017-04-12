package av.sberbank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import av.sberbank.model.Currency;
import av.sberbank.presenter.Presenter;
import av.sberbank.presenter.PresenterImpl;
import av.sberbank.utils.NetworkUtils;
import av.sberbank.view.CurrenciesView;
import av.sberbank.view.CurrencyAdapter;

public class MainActivity extends AppCompatActivity
        implements CurrenciesView, DialogInterface.OnClickListener {

    private Button btnStartCurr;
    private Button btnEndCurr;
    private Button btnReverseCurr;
    private Button btnClearCurr;

    private EditText etStartCurr;
    private EditText etEndCurr;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Presenter presenter;

    private CurrencyAdapter adapterStartCurr, adapterEndCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAdapters();

        presenter = new PresenterImpl(this);
        presenter.refreshData();

        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void initAdapters() {
        adapterStartCurr = new CurrencyAdapter(this);
        adapterEndCurr = new CurrencyAdapter(this);
    }

    private void initViews() {
        btnStartCurr = (Button) findViewById(R.id.button_start_curr);
        btnEndCurr = (Button) findViewById(R.id.button_end_curr);
        btnReverseCurr = (Button) findViewById(R.id.button_reverse_curr);
        btnClearCurr = (Button) findViewById(R.id.button_clear_et);
        etStartCurr = (EditText) findViewById(R.id.edittext_start_curr);
        etEndCurr = (EditText) findViewById(R.id.edittext_end_curr);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
        btnStartCurr.setOnClickListener(startCurrClickListener);
        btnEndCurr.setOnClickListener(endCurrClickListener);
        btnReverseCurr.setOnClickListener(clickReverse);
        btnClearCurr.setOnClickListener(clickClear);
        btnClearCurr.setOnClickListener(clickClear);

        etStartCurr.addTextChangedListener(watcherStartCurr);
    }

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (NetworkUtils.isConnected(MainActivity.this)) {
                presenter.refreshData();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);

                showMessage(R.string.dlg_msg_wifi_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
            }
        }
    };

    private final View.OnClickListener startCurrClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

            dialogBuilder.setAdapter(adapterStartCurr,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapterStartCurr.updateSelected(which);
                            btnStartCurr.setText(adapterStartCurr.getSelectedItem().getCharCode());
                            calculate();
                        }
                    });

            dialogBuilder.show();
        }
    };

    private final View.OnClickListener endCurrClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

            dialogBuilder.setAdapter(adapterEndCurr,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapterEndCurr.updateSelected(which);
                            btnEndCurr.setText(adapterEndCurr.getSelectedItem().getCharCode());
                            calculate();
                        }

                    });

            dialogBuilder.show();
        }
    };

    private final View.OnClickListener clickReverse = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tmp = adapterStartCurr.getCurrentCurrencyPosition();
            adapterStartCurr.setCurrentCurrencyPosition(adapterEndCurr.getCurrentCurrencyPosition());
            adapterEndCurr.setCurrentCurrencyPosition(tmp);

            btnEndCurr.setText(adapterEndCurr.getSelectedItem().getCharCode());
            btnStartCurr.setText(adapterStartCurr.getSelectedItem().getCharCode());

            calculate();
        }
    };

    private final TextWatcher watcherStartCurr = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calculate();
        }
    };

    private void calculate() {
        if (etStartCurr.getText().length() < 1) {
            etEndCurr.setText("");
            return;
        }
        double startSum = Double.parseDouble(etStartCurr.getText().toString());
        Currency startCurrency = adapterStartCurr.getSelectedItem();
        Currency endCurrency = adapterEndCurr.getSelectedItem();
        presenter.calculate(startSum, startCurrency, endCurrency);
    }

    private final View.OnClickListener clickClear = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetView();
        }
    };

    @Override
    public void showListCurrencies(List<Currency> currencies) {
        mSwipeRefreshLayout.setRefreshing(false);

        adapterStartCurr.addAll(currencies);
        adapterEndCurr.addAll(currencies);

        resetView();
    }

    private void resetView() {
        adapterStartCurr.setCurrentCurrencyPosition(0);//fixme 0
        adapterEndCurr.setCurrentCurrencyPosition(0);//fixme 0

        btnStartCurr.setText(adapterStartCurr.getSelectedItem().getCharCode());
        btnEndCurr.setText(adapterEndCurr.getSelectedItem().getCharCode());

        etStartCurr.setText(R.string.currency_sum);
        etEndCurr.setText(R.string.currency_sum);
    }

    @Override
    public void endCalculate(double endSum) {
        etEndCurr.setText(String.valueOf(endSum));
    }

    @Override
    public Context getContextPresenter() {
        return this;
    }

    public void showMessage(int msg, DialogInterface.OnClickListener listener) {
        showMessage(getString(msg), listener);
    }

    public void showMessage(String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(android.R.string.ok, listener);
        dialogBuilder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}