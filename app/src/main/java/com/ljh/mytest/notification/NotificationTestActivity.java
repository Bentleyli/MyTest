
package com.ljh.mytest.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ljh.mytest.MainActivity;
import com.ljh.mytest.R;

public class NotificationTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Button btnSendNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        context = this;
        btnSendNotify = findViewById(R.id.btn_send_notify);

        btnSendNotify.setOnClickListener(this);
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
                        .setWhen(System.currentTimeMillis())  //setWhen() 指定通知被创建的时间，以毫秒为单位，下拉通知后会将时间显示在相应的通知上。
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();
                manager.notify(1,notification);
                break;
        }
    }
}
