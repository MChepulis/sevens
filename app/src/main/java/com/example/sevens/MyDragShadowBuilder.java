package com.example.sevens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.DrawableWrapper;


public class MyDragShadowBuilder extends View.DragShadowBuilder {

    // The drag shadow image, defined as a drawable thing
    float offset_x;
    float offset_y;
    View m_view;

    public void setOffsets(float offset_x, float offset_y) {
        this.offset_x = offset_x;
        this.offset_y = offset_y;

    }

    // Defines the constructor for myDragShadowBuilder
    public MyDragShadowBuilder(View v) {

        // Stores the View parameter passed to myDragShadowBuilder.
        super(v);
        m_view = v;
        // Creates a draggable image that will fill the Canvas provided by the system.
        //shadow = new BitmapDrawable(v.getResources());
    }

    // Defines a callback that sends the drag shadow dimensions and touch point back to the
    // system.
    @Override
    public void onProvideShadowMetrics(Point size, Point touch) {
        // Defines local variables
        int width, height;
        // Sets the width of the shadow to half the width of the original View
        width = getView().getWidth();

        // Sets the height of the shadow to half the height of the original View
        height = getView().getHeight();

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(width, height);

        // Sets the touch point's position to be in the middle of the drag shadow

        touch.set((int) offset_x, (int) offset_y);
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    @Override
    public void onDrawShadow(Canvas canvas) {
        super.onDrawShadow(canvas);
        System.out.println("onDrawShadow");
        // Draws the ColorDrawable in the Canvas passed in from the system.
        //shadow.
        //shadow.draw(canvas);

    }
}

