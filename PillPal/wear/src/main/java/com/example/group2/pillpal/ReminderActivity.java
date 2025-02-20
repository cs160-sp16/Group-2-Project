package com.example.group2.pillpal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class ReminderActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Intent intent = getIntent();
        final String t = intent.getStringExtra("time");
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                TextView time = (TextView) stub.findViewById(R.id.time);
                time.setText(t);
            }
        });
    }
    public void reminder(final View view) {
        Intent i = new Intent(ReminderActivity.this, SnoozeActivity.class);
        startActivity(i);
    }
}
