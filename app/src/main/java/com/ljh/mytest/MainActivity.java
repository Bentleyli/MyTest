package com.ljh.mytest;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljh.mytest.db.MySqliteOpenHelper;
import com.ljh.mytest.widget.SlideTab;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlideTab slideTab;
    private List<String> tabNames = new ArrayList<>();
    private MySqliteOpenHelper mySqliteOpenHelper;
    private SQLiteDatabase db;

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

        /*mySqliteOpenHelper = new MySqliteOpenHelper(this);
        db = mySqliteOpenHelper.getWritableDatabase();*/
        SQLiteDatabase db = Connector.getDatabase();
    }
}
