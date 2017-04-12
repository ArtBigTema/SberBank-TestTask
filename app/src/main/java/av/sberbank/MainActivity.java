package av.sberbank;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import av.sberbank.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private Button btnStartCurr;
    private Button btnEndCurr;
    private Button btnReverseCurr;
    private Button btnClearCurr;

    private EditText etStartCurr;
    private EditText etEndCurr;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
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

        btnStartCurr.setOnClickListener(startCurrCliclListener);
        btnEndCurr.setOnClickListener(endCurrCliclListener);
    }

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                // download
            } else {
                // show msg
            }
        }
    };

    private final View.OnClickListener startCurrCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // show list curr
        }
    };

    private final View.OnClickListener endCurrCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // show list curr
        }
    };
}