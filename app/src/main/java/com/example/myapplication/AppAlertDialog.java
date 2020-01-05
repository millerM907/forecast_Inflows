package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

public class AppAlertDialog extends DialogFragment {

    public android.app.AlertDialog onCreateDialog(MainActivity activity, int keyMessage) {
        // Use the Builder class for convenient dialog construction
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

        if(keyMessage == 0){
            builder.setTitle(R.string.close_alert_dialog_site_title).setMessage(R.string.close_alert_dialog_site)
                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Закрываем приложение
                            System.exit(0);
                        }
                    });
        } else if (keyMessage == 1) {
            builder.setTitle(R.string.close_alert_dialog_net_title).setMessage(R.string.close_alert_dialog_net)
                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Закрываем приложение
                            System.exit(0);
                        }
                    });
        } else if (keyMessage == 2){
            String appName = activity.getString(R.string.app_name);
            String version = "Версия приложения: " + BuildConfig.VERSION_NAME;
            String developer = "Разработчик: Иван Иванов";
            builder.setTitle(R.string.about_prog_alert_dialog_title)
                    .setMessage("\n" + appName + "\n\n" + version + "\n\n" + developer)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Закрываем приложение
                        }
                    });
        }
        return builder.create();
    }
}
