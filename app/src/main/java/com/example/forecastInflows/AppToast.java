package com.example.forecastInflows;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AppToast extends Toast {

    private static TextView toastText;
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public AppToast(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*
         * Производится инициализация разметки всплывающего сообщения,
         * устанавливается изображение значка информации в всплывающее сообщение
         */
        View rootView = inflater.inflate(R.layout.layout_refresh_toast, null);
        toastText = (TextView) rootView.findViewById(R.id.tv_toast);

        /*
         * Устанавливается инициализированный внешний вид,
         * местоположение всплывающего сообщения на экране устройства,
         * а также длительность существованая сообщения
         */
        this.setView(rootView);
        this.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        this.setDuration(Toast.LENGTH_SHORT);
    }

    /*
     * Метод вызова сообщения без установки длительности существования
     * с передачей сообщению текстовой информации в качестве последовательности
     * текстовых символов или строки
     */
    public static AppToast makeText(Context context, CharSequence text) {
        AppToast result = new AppToast(context);
        toastText.setText(text);

        return result;
    }

    /*
     * Метод вызова сообщения с установкой длительности существования и
     * с передачей сообщению текстовой информации в качестве последовательности
     * текстовых символов или строки
     */
    public static AppToast makeText(Context context, CharSequence text, int duration) {
        AppToast result = new AppToast(context);
        result.setDuration(duration);
        toastText.setText(text);

        return result;
    }

    /*
     * Метод вызова сообщения без установки длительности существования
     * с передачей сообщению ID текстового ресурса
     */
    public static Toast makeText(Context context, int resId)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId));
    }

    /*
     * Метод вызова сообщения с установкой длительности существования и
     * с передачей сообщению ID текстового ресурса
     */
    public static Toast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }
}