package com.example.slidingconflict.p9;

import android.os.IInterface;

public interface PlusInterface extends IInterface {
    String DESC="PlusInterface";

    int add(int a, int b);
}
