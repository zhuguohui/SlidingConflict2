package com.example.slidingconflict.p9;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class PlusProxy implements PlusInterface {
    IBinder binder;

    public PlusProxy(IBinder binder) {
        this.binder = binder;

    }

    @Override
    public int add(int a, int b) {
        Log.i("zzz", "调用Proxy的add方法");
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        int i = 0;
        try {
            data.writeInterfaceToken(PlusInterface.DESC);
            data.writeInt(a);
            data.writeInt(b);
            binder.transact(100, data, reply, 1);
            i = reply.readInt();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }

        return i;
    }

    @Override
    public IBinder asBinder() {
        return binder;
    }
}
