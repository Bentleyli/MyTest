package com.ljh.mytest;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ljh.mytest.db.MySqliteOpenHelper;
import com.ljh.mytest.db.model.Comment;
import com.ljh.mytest.db.model.News;
import com.ljh.mytest.widget.SlideTab;

import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
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

//        initData();
        addMoreNews();
    }

    private void addMoreNews() {
        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            News news = new News();
            news.setTitle("批量增加标题" + i);
            news.setContent("批量增加新闻内容" + i);
            news.setPublishDate(new Date());
            news.save();
            newsList.add(news);
        }
    }

    private void initData() {
        News news = new News();
        news.setTitle("这是一条新闻标题");
        news.setContent("这是一条新闻内容");
        news.setPublishDate(new Date());
        Log.d("TAG", "news id is " + news.getId());
        news.save();
        Log.d("TAG", "news id is " + news.getId());


        Comment comment1 = new Comment();
        comment1.setContent("好评！");
        comment1.setPublishDate(new Date());
        comment1.save();
        Comment comment2 = new Comment();
        comment2.setContent("赞一个");
        comment2.setPublishDate(new Date());
        comment2.save();
        News news1 = new News();
        news1.getCommentList().add(comment1);
        news1.getCommentList().add(comment2);
        news1.setTitle("第二条新闻标题");
        news1.setContent("第二条新闻内容");
        news1.setPublishDate(new Date());
        news1.setCommentCount(news1.getCommentList().size());
        news1.save();
    }
}
