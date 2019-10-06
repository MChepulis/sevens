package com.example.sevens;

import android.content.ClipData;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;


public class  HexGridDragDropOnTouchListener implements View.OnTouchListener {


    /*
        private Pair<Float, Float> GetAbsoluteCoord(View view)
        {
            float x = view.getX();
            float y = view.getY();
            ViewParent parent = view.getParent();
            while(parent != null)
            {
                try {
                    ViewGroup group = (ViewGroup) parent;
                    x += group.getX();
                    y += group.getY();
                    parent = parent.getParent();
                } catch(Exception e){
                    parent = null;
                }
            }
            return new Pair<>(x, y);
        }
    */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == motionEvent.ACTION_DOWN) {
            // Get view object tag value.
            String tag = (String) view.getTag();

            // Create clip data.
            ClipData clipData = ClipData.newPlainText("", tag);

            // Create drag shadow builder object.

            //View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);

            MyDragShadowBuilder dragShadowBuilder = new MyDragShadowBuilder(view);
            System.out.println("event  " + motionEvent.getX() + "\t\t" + motionEvent.getY());
            dragShadowBuilder.setOffsets(motionEvent.getX(), motionEvent.getY());
            //dragShadowBuilder.setOffsets( view.getWidth() / 2,  view.getHeight() / 2);
            ((HexagonGrid) view).dragTouchCoord_x = motionEvent.getX();
            ((HexagonGrid) view).dragTouchCoord_y = motionEvent.getY();
            ((HexagonGrid) view).isDragTouchCoordSet = true;





        /* Invoke view object's startDrag method to start the drag action.
           clipData : to be dragged data.
           dragShadowBuilder : the shadow of the dragged view.
        */
            view.startDrag(clipData, dragShadowBuilder, view, 0);


            // Hide the view object because we are dragging it.
            view.setVisibility(View.INVISIBLE);
            return true;
        }
        if (motionEvent.getAction() == motionEvent.ACTION_UP) {
            view.setVisibility(View.INVISIBLE);
            return true;
        }
        return true;
    }
}

