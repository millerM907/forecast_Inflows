package com.example.forecastInflows;

public enum ResourcesModel {
    // создаем 3 перечисления с тайтлом и макетом
    // для удобной работы в адаптере
    FIRST_SCREEN(R.string.txt_screen_1, R.layout.activity_current),
    SECOND_SCREEN(R.string.txt_screen_2, R.layout.activity_today);
    //THIRD_SCREEN(R.string.txt_screen_3, R.layout.view_screen_3);

    private int mTitleResourceId;
    private int mLayoutResourceId;

    ResourcesModel(int titleResId, int layoutResId) {
        mTitleResourceId = titleResId;
        mLayoutResourceId = layoutResId;
    }

    public int getTitleResourceId() {
        return mTitleResourceId;
    }

    public int getLayoutResourceId() {
        return mLayoutResourceId;
    }
}
