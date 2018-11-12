package com.ljh.mytest.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljh.mytest.MainActivity;
import com.ljh.mytest.R;
import com.ljh.mytest.widget.AdImageView;
import com.ljh.mytest.widget.AdImageViewVersion1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Button btnSendNotify;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Map<String, Object>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        context = this;
        btnSendNotify = findViewById(R.id.btn_send_notify);
        recyclerView = findViewById(R.id.recyclerview);
        btnSendNotify.setOnClickListener(this);
        initRecyclerView();
    }
    private void initRecyclerView() {
        for (int i=0;i<50;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "万历十五年" + i);
            map.put("content", "在《万历十五年》一书中，黄仁宇用近乎平淡的笔触分析一个皇朝从兴盛走向衰颓的原因，而这些平淡的叙述自有力量，他淡然勾勒出的人生困境，即便是对历史学不感兴趣的读者，也心有戚戚焉。");
            datas.add(map);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new TestAdapter(context, datas));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int fPos = linearLayoutManager.findFirstVisibleItemPosition();
                int lPos = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                for(int i=fPos; i<=lPos; i++){
                    View view = linearLayoutManager.findViewByPosition(i);
                    AdImageViewVersion1 adImageView = view.findViewById(R.id.ad_iv);
                    if (adImageView.getVisibility() == View.VISIBLE) {
                        Log.e("查看", "onScrolled: linearLayoutManager.getHeight()："+linearLayoutManager.getHeight()+"\tview.getTop()"+view.getTop());
                        adImageView.setDy(linearLayoutManager.getHeight() - view.getTop());
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_notify:
                Intent intent = new Intent(NotificationTestActivity.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(context);
                Notification notification = builder
                        .setContentTitle("这是通知标题")
                        .setContentText("这是通知内容")
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis()) //setWhen() 指定通知被创建的时间，以毫秒为单位，下拉通知后会将时间显示在相应的通知上。
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();

                manager.notify(1, notification);
                break;
        }
    }
    public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView content;
            public ImageView ad_iv;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tv_title);
                content = itemView.findViewById(R.id.tv_content);
                ad_iv = itemView.findViewById(R.id.ad_iv);
            }
        }
        private List<Map<String, Object>> mDatas;
        private final Context context;
        public TestAdapter(Context context, List<Map<String, Object>> mDatas) {
            this.context = context;
            this.mDatas = mDatas;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,null);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            if (i > 0 && i % 7 == 0) {
                viewHolder.title.setVisibility(View.GONE);
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.ad_iv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.title.setVisibility(View.VISIBLE);
                viewHolder.content.setVisibility(View.VISIBLE);
                viewHolder.ad_iv.setVisibility(View.GONE);
            }

            viewHolder.title.setText(mDatas.get(i).get("title").toString());
            viewHolder.content.setText(mDatas.get(i).get("content").toString());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "点击"+mDatas.get(i).get("title").toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}
