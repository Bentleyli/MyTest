package com.ljh.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljh.mytest.widget.SlideTab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlideTab slideTab;
    private List<String> tabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabNames.add("标题1");
        tabNames.add("标题2");
        tabNames.add("标题3");
        tabNames.add("标题4");
        tabNames.add("标题5");
        slideTab = findViewById(R.id.slide_tab);
        slideTab.setTabNames(tabNames);
        slideTab.setSelectedIndex(2);
    }
}
