package com.nad.utility.blocker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.nad.utility.blocker.BuildConfig;
import com.nad.utility.blocker.R;
import com.nad.utility.blocker.activity.SettingsActivity;
import com.nad.utility.blocker.model.Call;
import com.nad.utility.blocker.model.SMS;
import com.nad.utility.blocker.util.BlockerManager;
import com.nad.utility.blocker.util.Logger;
import com.nad.utility.blocker.util.SettingsHelper;

import java.util.Date;

public class XposedMod  {

    public static final String MODULE_NAME = BuildConfig.APPLICATION_ID;
    public static final String FILTER_NOTIFY_BLOCKED = BuildConfig.APPLICATION_ID + "_NOTIFY_BLOCKED";

    private String MODULE_PATH;

    private NotificationManagerCompat notiManager;
    private NotificationCompat.Builder notiBuilder;

    private int smallNotificationIcon = -1;
    private String notificationContentText;

    private SettingsHelper settingsHelper;
    private BlockerManager blockerManager;



    private void saveHistoryAndNotify(Context context, Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            int type = bundle.getInt("type");
            switch (type) {
                case BlockerManager.TYPE_SMS:
                    SMS savedSMS = new SMS();
                    savedSMS.setSender(bundle.getString("sender"));
                    savedSMS.setContent(bundle.getString("content"));
                    savedSMS.setCreated(bundle.getLong("created"));
                    savedSMS.setRead(SMS.SMS_UNREADED);

                    blockerManager.saveSMS(savedSMS);

                    Logger.log("Block SMS: " + savedSMS.getSender() + "," + savedSMS.getContent() + "," + savedSMS.getCreated());
                    break;
                case BlockerManager.TYPE_CALL:
                    Call savedCall = new Call();
                    savedCall.setCaller(bundle.getString("caller"));
                    savedCall.setCreated(new Date().getTime());
                    savedCall.setRead(Call.CALL_UNREADED);

                    blockerManager.saveCall(savedCall);

                    Logger.log("Block call: " + savedCall.getCaller() + "," + savedCall.getCreated());
                    break;
            }

            if (settingsHelper.isShowBlockNotification()) {
                showNotification(context, type);
            }
        }
    }

    private void showNotification(Context context, int type) {
        if (smallNotificationIcon == -1 || notificationContentText == null) {
            return;
        }

        if (type != BlockerManager.TYPE_SMS && type != BlockerManager.TYPE_CALL) {
            return;
        }

        int unreadSMSCount = blockerManager.getUnReadSMSCount();
        int unreadCallCount = blockerManager.getUnReadCallCount();
        if (unreadSMSCount == 0 && unreadCallCount == 0) {
            return;
        }

        ComponentName componentName = new ComponentName(MODULE_NAME, SettingsActivity.class.getName());

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(componentName);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("position", type == BlockerManager.TYPE_SMS ? 2 : 3);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(componentName);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        notiBuilder.setSmallIcon(smallNotificationIcon)
                .setContentText(String.format(notificationContentText, unreadSMSCount, unreadCallCount))
                .setContentIntent(pendingIntent);

        notiManager.notify(0, notiBuilder.build());
    }
}
