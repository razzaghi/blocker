package com.nad.utility.blocker.receiver;

import android.content.Context;
import android.util.Log;

import java.util.Date;

/**
 * Created by razzaghi on 28/06/2016.
 */
public class CallReceiver extends IncomingCall {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {

        Log.d("Mr",number + " Started.");

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

        Log.d("Mr",number + " Ended.");

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }
}

