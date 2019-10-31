package com.example.sevens;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class BitmapBank {

    private ActivityMain m_app;
    private static ArrayList<Bitmap> BitmapForState;
    private static Bitmap hammer;
    private static Integer[] IDForState = {R.drawable.hexagon, R.drawable.hexagon, R.drawable.hexagon, R.drawable.hexagon, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo};
    private static Integer hammer_Image_id = R.drawable.hammer;
    private static Resources resources;

    public BitmapBank(Context context){

        m_app = (ActivityMain) context;
        resources = m_app.getResources();
        BitmapForState = new ArrayList<>();


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap;
        for (int i = 0; i < IDForState.length; i++)
        {
            bitmap = BitmapFactory.decodeResource(resources, IDForState[i], options);
            BitmapForState.add(bitmap);
        }
        hammer = BitmapFactory.decodeResource(resources, hammer_Image_id, options);
    }

    public static Bitmap getHammer()
    {
        return hammer;
    }

    public static Bitmap getBitmapForState(int state)
    {
        if(state >= BitmapForState.size())
            return null;
        return BitmapForState.get(state);
    }
}
