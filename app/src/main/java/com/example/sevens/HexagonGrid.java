package com.example.sevens;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Random;


public class HexagonGrid extends View {
    private static final int DEFAULT_ROW_NUM = 5;
    private static final int DEFAULT_COL_NUM = 5;
    private static final int DEFAULT_COLOR = Color.GREEN;

    private ArrayList<ArrayList<Hexagon>> m_hexagons;
    float m_width;
    float m_height;
    float m_radius;
    float m_maxRadius;
    float padding_x;
    float padding_y;
    float margin_x;
    float margin_y;
    float grid_zero_x;
    float grid_zero_y;


    ActivityMain m_app;
    int m_rowNum;
    int m_colNum;
    int m_hex_color;
    ArrayList<Pair<Integer, Integer>> m_trims_arr;
    boolean isNeedGenerate = true;

    HexagonGrid other_for_synch;


    public float dragTouchCoord_x;
    public float dragTouchCoord_y;
    public boolean isDragTouchCoordSet;


    public HexagonGrid(Context context) {
        super(context);
        m_app = (ActivityMain) context;
        init(null);
    }

    public HexagonGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_app = (ActivityMain) context;
        init(attrs);

    }

    public HexagonGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_app = (ActivityMain) context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {

        m_hexagons = new ArrayList<>();
        m_trims_arr = new ArrayList<>();
        m_rowNum = DEFAULT_ROW_NUM;
        m_colNum = DEFAULT_COL_NUM;
        m_hex_color = DEFAULT_COLOR;
        System.out.println(m_app);
        isDragTouchCoordSet = false;
        m_maxRadius = -1;
        other_for_synch = null;
        setBackgroundResource(R.drawable.back2);

        if (set == null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.HexGrid);
        m_hex_color = ta.getColor(R.styleable.HexGrid_hex_color, DEFAULT_COLOR);
        m_rowNum = ta.getInteger(R.styleable.HexGrid_row_num, DEFAULT_ROW_NUM);
        m_colNum = ta.getInteger(R.styleable.HexGrid_col_num, DEFAULT_COL_NUM);
        ta.recycle();
    }
/*
    public void setMaxRadius(float max_radius)
    {
        m_maxRadius = max_radius;
        if(m_radius > m_maxRadius)
        {
            isNeedGenerate = true;
        }
    }
*/

    public void synchronizeRadius (HexagonGrid other)
    {
        other_for_synch = other;
    }


    private boolean isNeedSynchronize()
    {
        m_maxRadius =  getMaxRadius();
        if (m_maxRadius <= 0 )
            return false;

        if( m_radius > m_maxRadius) {
            isNeedGenerate = true;
            return true;
        }

        if( m_radius < m_maxRadius) {
            other_for_synch.invalidate();
            return false;
        }

        return false;
    }
    public float getMaxRadius() {
        if(other_for_synch == null)
            return -1;
        return other_for_synch.getRadius();
    }
/*
    public void setWidthAndHeight(float width, float height) {
        m_width = width;
        m_height = height;
    }
*/
    public float getRadius()
    {
        return m_radius;
    }



    private float calculateWidth() {

        float horiz_radius = m_radius * (float)(Math.sqrt(3) / 2);
        return 2 * horiz_radius + (m_colNum - 1) * (2 * horiz_radius + padding_x) + 2 * margin_x;
    }

    private float calculateHeight() {
        return 2 * m_radius  + (m_rowNum - 1) * ((float)1.5 * m_radius + padding_y) + 2 * margin_y;
    }

    private void centering ()
    {
        grid_zero_x = (m_width - calculateWidth()) / 2;
        grid_zero_y = (m_height - calculateHeight()) / 2;
    }


/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        System.out.println("HexGrid onMeasure");
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) calculateWidth(), (int) calculateHeight());

    }

*/

    public void createGrid() {
        float hex_rad;
        grid_zero_x = 0;
        grid_zero_y = 0;
        padding_x = 10;
        padding_y = padding_x;
        margin_x = 20;
        margin_y = margin_x;


        int hor_long_count = m_colNum;
        int hor_short_count = m_colNum - 1;

        float[] x_pos_long = new float[hor_long_count];
        float[] x_pos_short = new float[hor_short_count];
        ArrayList<Float> y_pos = new ArrayList<>();

        float vert_max_hex = (m_height - 2 * margin_y - (m_rowNum - 1) * padding_y) / (2 + ((m_rowNum - 1)) * (float)1.5);
        float horiz_max_hex = (m_width - 2 * margin_x - (m_colNum - 1) * padding_x) / ((2 + 2 * (m_colNum - 1))* (float)Math.sqrt(3) / 2);

        hex_rad = Math.min(vert_max_hex, horiz_max_hex);
        if(m_maxRadius > 0) {
            hex_rad = Math.min(hex_rad, m_maxRadius);
        }
        m_radius = hex_rad;
        float horiz_radius = hex_rad * (float)(Math.sqrt(3) / 2);

        float vert_step = hex_rad * (float)1.5 + padding_y;
        float horiz_step = horiz_radius * 2 + padding_x;


        if( hor_long_count > 0) {
            x_pos_long[0] = horiz_radius + margin_x;
            for (int i = 1; i < hor_long_count; i++) {
                x_pos_long[i] =  x_pos_long[i-1] + horiz_step;
            }
        }

        if( hor_short_count > 0) {
            x_pos_short[0] = horiz_radius * 2 +  margin_x + padding_y / 2;
            for (int i = 1; i < hor_short_count; i++) {
                x_pos_short[i] =  x_pos_short[i-1] + horiz_step;
            }
        }

        if(m_rowNum > 0)
        {
            y_pos.add(hex_rad + margin_y) ;
            for (int i = 1; i < m_rowNum; i++) {
                y_pos.add(y_pos.get(i - 1)  + (vert_step));
            }
        }

        centering();
        for (int i = 0; i < m_rowNum; i++) {
            if (i % 2 == 0) {
                int max = hor_long_count;
                for (int j = 0; j < max; j++) {
                    m_hexagons.get(i).get(j).setParams(grid_zero_x + x_pos_long[j], grid_zero_y + y_pos.get(i), hex_rad);
                    ;
                }
            } else {
                int max = hor_short_count;
                for (int j = 0; j < max; j++) {

                    m_hexagons.get(i).get(j).setParams(grid_zero_x + x_pos_short[j], grid_zero_y + y_pos.get(i), hex_rad);
                }
            }
        }

    }


    public void changeColor(Integer new_color) {
        for (int i = 0; i < m_hexagons.size(); i++) {

            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).setColor(new_color);
            }
        }
    }


    public void deleteHex(int i, int j) {
        m_trims_arr.add(new Pair(i, j));
    }


    private void TrimGrid() {
        Integer i, j;
        for (int k = 0; k < m_trims_arr.size(); k++) {
            i = m_trims_arr.get(k).first;
            j = m_trims_arr.get(k).second;
            m_hexagons.get(i).get(j).setExist(false);
        }

    }


    public void ProcessDragMes(DragEvent event) {
        System.out.println("ProcessDragMes");
        float drag_x = event.getX();
        float drag_y = event.getY();
        Hexagon cur_hex;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                cur_hex = m_hexagons.get(i).get(j);
                if (cur_hex.isContain(drag_x, drag_y)) {
                    cur_hex.setColor(Color.BLUE);
                }
            }
        }
    }


    public void ProcessDragMesDrop(DragEvent event) {
        System.out.println("ProcessDragMes");
        float drag_x = event.getX();
        float drag_y = event.getY();
        Hexagon cur_hex;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                cur_hex = m_hexagons.get(i).get(j);
                if (cur_hex.isContain(drag_x, drag_y)) {
                    cur_hex.ApdateState();
                }
            }
        }
    }


    public void Process_Drag_Hex(ArrayList<Pair<Float, Float>> coord_of_centers) {
        System.out.println("ProcessDragMes");
        Pair<Float, Float> cur_drag_center;
        Hexagon cur_hex;
        System.out.println("Size = " + coord_of_centers.size());
        for (int k = 0; k < coord_of_centers.size(); k++) {
            cur_drag_center = coord_of_centers.get(k);
            float drag_x = cur_drag_center.first;
            float drag_y = cur_drag_center.second;
            System.out.println(k + ")hex = " + drag_x + "\t" + drag_y);
            for (int i = 0; i < m_hexagons.size(); i++) {
                for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                    cur_hex = m_hexagons.get(i).get(j);
                    if (cur_hex.isContain(drag_x, drag_y)) {
                        cur_hex.setColor(Color.BLUE);
                    }
                }
            }
        }
    }


    public void Process_Drag_Hex_Drop(ArrayList<Pair<Float, Float>> coord_of_centers) {
        System.out.println("ProcessDragMes");
        Pair<Float, Float> cur_drag_center;
        Hexagon cur_hex;
        System.out.println("Size = " + coord_of_centers.size());
        for (int k = 0; k < coord_of_centers.size(); k++) {
            cur_drag_center = coord_of_centers.get(k);
            float drag_x = cur_drag_center.first;
            float drag_y = cur_drag_center.second;
            System.out.println(k + ")hex = " + drag_x + "\t" + drag_y);
            for (int i = 0; i < m_hexagons.size(); i++) {
                for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                    cur_hex = m_hexagons.get(i).get(j);
                    if (cur_hex.isContain(drag_x, drag_y)) {
                        cur_hex.ApdateState();
                    }
                }
            }
        }

    }


    public void setGridParams(int newColNum, int newRowNum) {

        m_rowNum = newRowNum;
        m_colNum = newColNum;
        m_trims_arr.clear();
        initGrid();
        isNeedGenerate = true;
    }


    private void initGrid() {
        //System.out.println(m_colNum + " " + m_rowNum);
        m_hexagons.clear();
        for (int i = 0; i < m_rowNum; i++) {
            ArrayList<Hexagon> tmp = new ArrayList<>();
            int maxColNum;
            if (i % 2 == 0)
                maxColNum = m_colNum;
            else
                maxColNum = m_colNum - 1;

            for (int j = 0; j < maxColNum; j++) {
                tmp.add(new Hexagon(m_app));
            }
            m_hexagons.add(tmp);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
       // System.out.println("Hex_Grid onDraw");
        isNeedSynchronize();
        if (isNeedGenerate) {
            m_height = getHeight();
            m_width = getWidth();
            initGrid();
            createGrid();
            TrimGrid();
            isNeedGenerate = false;
        }
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).draw(canvas);
            }
        }
    }


    ArrayList<Hexagon> getAllHexagons() {
        ArrayList<Hexagon> result = new ArrayList<>();
        Hexagon cur_hex;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                cur_hex = m_hexagons.get(i).get(j);
                if (cur_hex.isExist())
                    result.add(cur_hex);
            }
        }
        return result;
    }


    ArrayList<Pair<Float, Float>> getAllHexagonsCenters() {
        ArrayList<Pair<Float, Float>> result = new ArrayList<>();
        Hexagon cur_hex;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                cur_hex = m_hexagons.get(i).get(j);
                if (cur_hex.isExist())
                    result.add(new Pair(cur_hex.getX(), cur_hex.getY()));
            }
        }
        return result;
    }


    public void setAllHexagonExist() {
        m_trims_arr.clear();
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).setExist(true);
            }
        }
    }




}