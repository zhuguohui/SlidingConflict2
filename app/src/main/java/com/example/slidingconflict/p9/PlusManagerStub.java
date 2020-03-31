package com.example.slidingconflict.p9;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class PlusManagerStub extends Binder implements PlusInterface {

    public static PlusInterface asInterFace(IBinder binder) {
        if (binder == null) {
            return null;
        }
        IInterface iInterface = binder.queryLocalInterface(PlusInterface.DESC);
        if (iInterface instanceof PlusInterface) {
            return (PlusInterface) iInterface;
        }

        return new PlusProxy(binder);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        if (code == 100) {
            data.enforceInterface(DESC);
            int a = data.readInt();
            int b = data.readInt();
            int c = add(a, b);
            if (reply != null) {
                reply.writeNoException();
                reply.writeInt(c);
            }
            return true;
        }

        return super.onTransact(code, data, reply, flags);
    }



    @Override
    public IBinder asBinder() {
        return this;
    }


    static class PlusProxy implements PlusInterface {
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
}
