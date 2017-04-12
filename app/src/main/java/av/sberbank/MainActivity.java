package av.sberbank;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import av.sberbank.model.Currency;
import av.sberbank.presenter.Presenter;
import av.sberbank.presenter.PresenterImpl;
import av.sberbank.utils.AnimationUtil;
import av.sberbank.utils.Constants;
import av.sberbank.utils.NetworkUtils;
import av.sberbank.view.CurrenciesView;
import av.sberbank.view.CurrencyAdapter;

public class MainActivity extends AppCompatActivity
        implements CurrenciesView {

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

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.accent));
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
        btnStartCurr.setOnClickListener(startCurrClickListener);
        btnEndCurr.setOnClickListener(endCurrClickListener);
        btnReverseCurr.setOnClickListener(clickReverse);
        btnClearCurr.setOnClickListener(clickClear);
        btnClearCurr.setOnClickListener(clickClear);

        etStartCurr.addTextChangedListener(watcherStartCurr);
        etEndCurr.setOnClickListener(clickToCopy);
    }

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (NetworkUtils.isConnected(getContextPresenter())) {
                presenter.refreshData();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                showMessageConnect();
            }
        }
    };

    private final View.OnClickListener startCurrClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContextPresenter());

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
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContextPresenter());

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
            AnimationUtil.animateReverse(getContextPresenter(),
                    btnReverseCurr, btnStartCurr, btnEndCurr,
                    etStartCurr, etEndCurr);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    reverseView();
                    calculate();
                }
            }, getResources().getInteger(R.integer.duration_long));
        }
    };
    private final View.OnClickListener clickToCopy = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipMan.setPrimaryClip(ClipData.newPlainText(Constants.NAME, etEndCurr.getText()));
            Snackbar.make(v, R.string.toast_click_to_copy, Toast.LENGTH_SHORT).show();
        }
    };

    private void reverseView() {
        int tmp = adapterStartCurr.getCurrentCurrencyPosition();
        adapterStartCurr.setCurrentCurrencyPosition(adapterEndCurr.getCurrentCurrencyPosition());
        adapterEndCurr.setCurrentCurrencyPosition(tmp);

        // etStartCurr.setText(etEndCurr.getText()); //fixme
        btnEndCurr.setText(adapterEndCurr.getSelectedItem().getCharCode());
        btnStartCurr.setText(adapterStartCurr.getSelectedItem().getCharCode());
    }

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
            AnimationUtil.animateClear(getContextPresenter(),
                    btnStartCurr, btnEndCurr, etStartCurr, etEndCurr);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetView();
                }
            }, getResources().getInteger(R.integer.duration_long));
        }
    };

    @Override
    public void showListCurrencies(List<Currency> currencies) {
        mSwipeRefreshLayout.setRefreshing(false);

        if (currencies.size() < 1) {
            showMessageConnect();
            return;
        } else {
            Snackbar.make(mSwipeRefreshLayout,
                    R.string.toast_finish_downloading, Toast.LENGTH_SHORT).show();
        }

        adapterStartCurr.addAll(currencies);
        adapterEndCurr.addAll(currencies);

        resetView();
    }

    private void resetView() {
        adapterStartCurr.setCurrentCurrencyPosition(0);
        adapterEndCurr.setCurrentCurrencyPosition(0);

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

    public void showMessageConnect() {
        showMessage(R.string.dlg_msg_wifi_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    public void showMessage(int msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContextPresenter());
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(android.R.string.ok, listener);
        dialogBuilder.show();
    }
}