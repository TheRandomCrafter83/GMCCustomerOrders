package com.randomsoft.gmccustomerorders;

import android.content.res.Resources;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

public class NavigationBar {

    final int BOTTOM = 0;
    final int RIGHT = 1;
    final int LEFT = 2;
    final int NONE = 3;
    private int LOCATION = NONE;

    private View view;

    NavigationBar(View view) {
        this.view = view;
    }

    int getNavBarLocation() {
        ViewCompat.setOnApplyWindowInsetsListener(view, new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if (insets.getSystemWindowInsetBottom() != 0)
                    LOCATION = BOTTOM;
                else if (insets.getSystemWindowInsetRight() != 0)
                    LOCATION = RIGHT;
                else if (insets.getSystemWindowInsetLeft() != 0)
                    LOCATION = LEFT;
                else
                    LOCATION = NONE;
                return insets;
            }
        });
        return LOCATION;
    }

    int getNavBarHeight() {
        Resources resources = view.getResources();
        int resourceId = resources.getIdentifier(
                "navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        return 0;
    }
}

