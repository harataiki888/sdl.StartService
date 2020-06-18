package jp.ac.titech.itpro.sdl.startservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import static android.content.Intent.ACTION_ANSWER;

public class Service3 extends Service {
    private final static String TAG = Service3.class.getSimpleName();
    public final static String EXTRA_MYARG = "MYARG";
    private final static int MSG_TAG = 1234;

    private Looper looper;
    private Handler handler;
    private String EXTRA_ANSWER;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate in " + Thread.currentThread());
        HandlerThread th = new HandlerThread("service3 thread");
        th.start();
        looper = th.getLooper();
        handler = new Handler(looper) {
            @Override
            public void handleMessage(@NonNull Message message) {
                Log.d(TAG, "handleMessage in " + Thread.currentThread());
                onHandleIntent((Intent) message.obj);
                stopSelf(message.arg1);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand in " + Thread.currentThread());
        Message message = handler.obtainMessage(MSG_TAG, startId, 0, intent);
        handler.sendMessage(message);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy in " + Thread.currentThread());
        looper.quit();
        super.onDestroy();
    }

    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent in " + Thread.currentThread());
        Log.d(TAG, "myarg = " + intent.getStringExtra(EXTRA_MYARG));

        Intent intent3 = new Intent();
        intent3.setAction("ACTION_ANSWER");
        intent3.putExtra("EXTRA_ANSWER","Received form Service3");
        sendBroadcast(intent3);

        try {
            Thread.sleep(5000); // 5 sec
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
