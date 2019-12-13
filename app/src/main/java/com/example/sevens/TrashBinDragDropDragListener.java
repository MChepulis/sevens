package com.example.sevens;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class  TrashBinDragDropDragListener implements View.OnDragListener {

    private Context context = null;

    public TrashBinDragDropDragListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        // Get the drag drop action.

        int dragAction = dragEvent.getAction();
        if (dragAction == dragEvent.ACTION_DRAG_STARTED) {
            // Check whether the dragged view can be placed in this target view or not.

            ClipDescription clipDescription = dragEvent.getClipDescription();

            // Return true because the target view can accept the dragged object.
            return clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        } else if (dragAction == dragEvent.ACTION_DRAG_ENTERED) {
            // When the being dragged view enter the target view, change the target view background color.
            changeTargetViewBackground(view, Color.GREEN);
            return true;
        } else if (dragAction == dragEvent.ACTION_DRAG_EXITED) {
            // When the being dragged view exit target view area, clear the background color.
            resetTargetViewBackground(view);
            return true;
        } else if (dragAction == dragEvent.ACTION_DRAG_ENDED) {


            // When the drop ended reset target view background color.
            resetTargetViewBackground(view);
            // Make the dragged view object visible.
            View srcView = (View) dragEvent.getLocalState();
            srcView.setVisibility(View.VISIBLE);

            // Get drag and drop action result.
            boolean result = dragEvent.getResult();

            // result is true means drag and drop action success.
            if (result) {
                //System.out.println("Drag and drop action complete successfully.");
                //Toast.makeText(context, "Drag and drop action complete successfully.", Toast.LENGTH_LONG).show();
            } else {
                resetTargetViewBackground(view);
                //System.out.println("Drag and drop action failed.");
                //Toast.makeText(context, "Drag and drop action failed.", Toast.LENGTH_LONG).show();
            }

            return true;

        } else if (dragAction == dragEvent.ACTION_DROP) {
            // When drop action happened.

            // Get clip data in the drag event first.
            ClipData clipData = dragEvent.getClipData();

            // Get drag and drop item count.
            int itemCount = clipData.getItemCount();

            // If item count bigger than 0.
            if (itemCount > 0) {
                // Get clipdata item.
                ClipData.Item item = clipData.getItemAt(0);
                String dragDropString = item.getText().toString();
                // Show a toast popup.
                //Toast.makeText(context, "Dragged object is a " + dragDropString, Toast.LENGTH_LONG).show();
                resetTargetViewBackground(view);
                changeTargetViewBackground(view, Color.GREEN);
            }
            return true;
        } else if (dragAction == dragEvent.ACTION_DRAG_LOCATION) {
            return true;
        } else {
            Toast.makeText(context, "Drag and drop unknow action type.", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private void resetTargetViewBackground(View view) {
        ((ImageView)view).clearColorFilter();
        view.invalidate();
    }


    private void changeTargetViewBackground(View view, int color) {
        ((ImageView)view).setColorFilter(color, PorterDuff.Mode.SRC_IN);
        view.invalidate();
    }
}
