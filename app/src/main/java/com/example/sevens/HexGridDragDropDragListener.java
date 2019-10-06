package com.example.sevens;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Pair;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class  HexGridDragDropDragListener implements View.OnDragListener {

    private Context context = null;

    public HexGridDragDropDragListener(Context context) {
        this.context = context;
    }


    private ArrayList<Pair<Float, Float>> getCenters(DragEvent dragEvent) {
        float touch_coord_x;
        float touch_coord_y;
        HexagonGrid srcView = (HexagonGrid) dragEvent.getLocalState();
        ArrayList<Pair<Float, Float>> centers = srcView.getAllHexagonsCenters();

        if (srcView.isDragTouchCoordSet) {

            touch_coord_x = srcView.dragTouchCoord_x;
            touch_coord_y = srcView.dragTouchCoord_y;
        } else {
            touch_coord_x = srcView.getWidth() / 2;
            touch_coord_y = srcView.getHeight() / 2;
        }
        System.out.println("offset" + "\t" + touch_coord_x + "\t" + touch_coord_y);

        Float x_offset = dragEvent.getX() - touch_coord_x;
        Float y_offset = dragEvent.getY() - touch_coord_y;
        for (int i = 0; i < centers.size(); i++) {
            centers.set(i, new Pair<>(centers.get(i).first + x_offset, centers.get(i).second + y_offset));
        }
        return centers;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        // Get the drag drop action.

        int dragAction = dragEvent.getAction();
        if (dragAction == dragEvent.ACTION_DRAG_STARTED) {
            System.out.println("dragEvent.ACTION_DRAG_STARTED");
            // Check whether the dragged view can be placed in this target view or not.

            ClipDescription clipDescription = dragEvent.getClipDescription();

            if (clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                // Return true because the target view can accept the dragged object.
                return true;

            }
        } else if (dragAction == dragEvent.ACTION_DRAG_ENTERED) {
            // When the being dragged view enter the target view, change the target view background color.
            System.out.println("dragEvent.ACTION_DRAG_ENTERED");
            changeTargetViewBackground(view, Color.GREEN);
            //((HexagonGrid)view).ProcessDragMes(dragEvent);
            ((HexagonGrid) view).Process_Drag_Hex(getCenters(dragEvent));

            return true;
        } else if (dragAction == dragEvent.ACTION_DRAG_EXITED) {
            System.out.println("dragEvent.ACTION_DRAG_EXITED");
            // When the being dragged view exit target view area, clear the background color.
            resetTargetViewBackground(view);

            return true;
        } else if (dragAction == dragEvent.ACTION_DRAG_LOCATION) {
            // When the being dragged view exit target view area, clear the background color.
            System.out.println("dragEvent.ACTION_DRAG_LOCATION");
            changeTargetViewBackground(view, Color.GREEN);
            //((HexagonGrid)view).ProcessDragMes(dragEvent);
            ((HexagonGrid) view).Process_Drag_Hex(getCenters(dragEvent));
            return true;
        } else if (dragAction == dragEvent.ACTION_DRAG_ENDED) {
            System.out.println("dragEvent.ACTION_DRAG_ENDED");


            // When the drop ended reset target view background color.
            resetTargetViewBackground(view);
            // Make the dragged view object visible.
            View srcView = (View) dragEvent.getLocalState();
            srcView.setVisibility(View.VISIBLE);

            // Get drag and drop action result.
            boolean result = dragEvent.getResult();

            // result is true means drag and drop action success.
            if (result) {
            } else {
                resetTargetViewBackground(view);
            }

            return true;

        } else if (dragAction == dragEvent.ACTION_DROP) {
            System.out.println("dragEvent.ACTION_DROP");
            // When drop action happened.

            // Get clip data in the drag event first.
            ClipData clipData = dragEvent.getClipData();

            // Get drag and drop item count.
            int itemCount = clipData.getItemCount();

            // If item count bigger than 0.
            if (itemCount > 0) {

                resetTargetViewBackground(view);
                ((HexagonGrid) view).Process_Drag_Hex_Drop(getCenters(dragEvent));

            }
            return true;
        } else {
            Toast.makeText(context, "Drag and drop unknow action type.", Toast.LENGTH_LONG).show();
        }

        return false;
    }


    private void resetTargetViewBackground(View view) {
        //view.getBackground().clearColorFilter();
        ((HexagonGrid) view).changeColor(Color.RED);
        view.invalidate();

    }

    private void changeTargetViewBackground(View view, int color) {

        ((HexagonGrid) view).changeColor(color);
        //view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        view.invalidate();
    }

}
