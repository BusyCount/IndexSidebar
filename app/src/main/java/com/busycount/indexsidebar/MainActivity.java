package com.busycount.indexsidebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.busycount.library.SidebarView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.mText);
        SidebarView sidebarView = (SidebarView) findViewById(R.id.mSidebar);
        sidebarView.setOnLetterChangedListener(new SidebarView.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(int position, String str) {
                if (position != -1) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(str);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }
}
