package com.stlanikstudio.forecastInflows.util.message;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

import com.stlanikstudio.forecastInflows.BuildConfig;
import com.stlanikstudio.forecastInflows.R;

public class AppAlertDialog extends DialogFragment {

    public android.app.AlertDialog onCreateDialog(Context context, int keyMessage) {
        // Use the Builder class for convenient dialog construction
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

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
            String appName = context.getString(R.string.app_name);
            String version = context.getString(R.string.about_prog_alert_dialog_mes) + " " + BuildConfig.VERSION_NAME;
            String developer = context.getString(R.string.about_prog_alert_dialog_company);
            builder.setIcon(R.drawable.icon).
                    setTitle(R.string.about_prog_alert_dialog_title)
                    .setMessage("\n" + appName + "\n\n" + version + "\n\n" + developer)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Закрываем приложение
                        }
                    });
        } else if (keyMessage == 3) {
            builder.setView(R.layout.layout_loading_dialog).setCancelable(false);
        } else if(keyMessage == 4){
            builder.setTitle(R.string.close_alert_dialog_net_title).setMessage(R.string.close_alert_dialog_net_update_db)
                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Закрываем приложение
                            System.exit(0);
                        }
                    });
        } else if (keyMessage == 5) {
            builder.setTitle(R.string.close_alert_dialog_warning_title)
                    .setMessage(R.string.warning_alert_dialog_net)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
        } else if (keyMessage == 6) {
            builder.setTitle(R.string.close_alert_dialog_warning_title)
                    .setMessage(R.string.warning_alert_dialog_server)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
        }


        return builder.create();
    }

    /**
     * Метод принимает на вход Сontext и ключ вызываемого сообщения keyMessage.
     * Создает и выводит AlertDialog с сообщением.
     * */
    public static void showAlertDialog(Context context, int keyMessage){
        AppAlertDialog alertDialog = new AppAlertDialog();
        android.app.AlertDialog dialog = alertDialog.onCreateDialog(context, keyMessage);
        dialog.setCancelable(false);
        dialog.show();
    }
}
