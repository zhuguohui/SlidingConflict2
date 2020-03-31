package com.example.slidingconflict.p9;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class PlusService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return plusManagerStub;
    }

    final PlusManagerStub plusManagerStub = new PlusManagerStub() {
        @Override
        public int add(int a, int b) {
            Log.i("zzz", "PlusService 中调用 add方法 a=" + a + " b=" + b);
            return a + b;
        }
    };
}
