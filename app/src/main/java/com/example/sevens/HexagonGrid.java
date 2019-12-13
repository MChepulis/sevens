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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class HexagonGrid extends View {
    private static final int DEFAULT_ROW_NUM = 5;
    private static final int DEFAULT_COL_NUM = 5;
    private static final int DEFAULT_COLOR = Color.GREEN;
    private static final int MIN_CHAIN_LENGTH = 3;

    static final int MERGE_SCORE = 1;
    static final int EXPLOSION_SCORE = 2;
    static final int HAMMER_SCORE = 3;

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
    int m_emptyHexCount;
    int m_score;


    ActivityMain m_app;
    int m_rowNum;
    int m_colNum;
    int m_hex_color;
    boolean isNeedGenerate;
    boolean isNeedInit;
    ViewHammerButton hammer;

    HexagonGrid other_for_synch;


    public float dragTouchCoord_x;
    public float dragTouchCoord_y;
    public boolean isDragTouchCoordSet;

    private ArrayList<Pair<Integer, Integer>> m_indexChain;


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
        m_indexChain = new ArrayList<>();
        m_rowNum = DEFAULT_ROW_NUM;
        m_colNum = DEFAULT_COL_NUM;
        m_hex_color = DEFAULT_COLOR;
        isDragTouchCoordSet = false;
        m_maxRadius = -1;
        other_for_synch = null;
        isNeedGenerate = true;
        isNeedInit = true;
        m_emptyHexCount = 0;
        hammer = null;
        m_score = 0;

        padding_x = 10;
        padding_y = padding_x;
        margin_x = 20;
        margin_y = margin_x;
        m_rowNum = 0;
        m_colNum = 0;
        //setBackgroundResource(R.drawable.back2);

        if (set == null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.HexGrid);
        m_hex_color = ta.getColor(R.styleable.HexGrid_hex_color, DEFAULT_COLOR);
        //m_rowNum = ta.getInteger(R.styleable.HexGrid_row_num, DEFAULT_ROW_NUM);
        //m_colNum = ta.getInteger(R.styleable.HexGrid_col_num, DEFAULT_COL_NUM);
        setGridParams(m_colNum, m_rowNum);
        ta.recycle();
    }
/*
    public void setMaxRadius(float max_radius)
    {
        m_maxRadius = max_radius;
        if(m_radius > m_maxRadius)
        {
            isNeedGenerate = true;
            isNeedTrim = true;
        }
    }
*/

    public void synchronizeRadius(HexagonGrid other) {
        other_for_synch = other;
    }


    private boolean isNeedSynchronize() {
        m_maxRadius = getMaxRadius();
        if (m_maxRadius <= 0)
            return false;

        if (m_radius > m_maxRadius) {
            isNeedGenerate = true;
            return true;
        }

        if (m_radius < m_maxRadius) {
            other_for_synch.invalidate();
            return false;
        }

        return false;
    }


    public float getMaxRadius() {
        if (other_for_synch == null)
            return -1;
        return other_for_synch.getRadius();
    }

    /*
        public void setWidthAndHeight(float width, float height) {
            m_width = width;
            m_height = height;
        }
    */
    public float getRadius() {
        return m_radius;
    }


    private Hexagon getUpHex() {
        Hexagon up = m_hexagons.get(0).get(0);
        Hexagon curr_hex;
        boolean breakFlag = false;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                curr_hex = m_hexagons.get(i).get(j);
                if (curr_hex.isExist()) {
                    up = curr_hex;
                    breakFlag = true;
                    break;
                }
            }
            if (breakFlag)
                break;
        }
        return up;
    }

    private Hexagon getDownHex() {
        Hexagon down = m_hexagons.get(0).get(0);
        Hexagon curr_hex;
        boolean breakFlag = false;

        breakFlag = false;
        for (int i = m_hexagons.size() - 1; i >= 0; i--) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {

                curr_hex = m_hexagons.get(i).get(j);
                if (curr_hex.isExist()) {
                    down = curr_hex;
                    breakFlag = true;
                    break;
                }
            }
            if (breakFlag)
                break;
        }
        return down;
    }

    private Hexagon getLeftHex() {
        Hexagon left = m_hexagons.get(0).get(0);
        Hexagon curr_hex;

        Pair<Integer, Integer> left_ind = new Pair<>(0, 0);
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                curr_hex = m_hexagons.get(i).get(j);
                if (curr_hex.isExist()) {
                    if (!left.isExist()) {
                        left_ind = new Pair<>(i, j);
                        left = curr_hex;
                    }
                    if (curr_hex.getX() < left.getX()) {
                        left_ind = new Pair<>(i, j);
                        left = curr_hex;
                    }
                }
            }

        }
        return left;
    }

    private Hexagon getRightHex() {
        Hexagon right = m_hexagons.get(0).get(0);
        Hexagon curr_hex;

        Pair<Integer, Integer> right_ind = new Pair<>(0, 0);

        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                curr_hex = m_hexagons.get(i).get(j);
                if (curr_hex.isExist()) {
                    if (!right.isExist()) {
                        right_ind = new Pair<>(i, j);
                        right = curr_hex;
                    }
                    if (curr_hex.getX() > right.getX()) {
                        right_ind = new Pair<>(i, j);
                        right = curr_hex;
                    }
                }
            }
        }
        return right;
    }

    private float calculateWidth(Hexagon left, Hexagon right) {
        if (m_colNum == 0)
            return 0;
        float horiz_radius = m_radius * (float) (Math.sqrt(3) / 2);
        return (right.getX() - left.getX()) + 2 * horiz_radius + 2 * margin_x;
    }


    private float calculateHeight(Hexagon up, Hexagon down) {
        if (m_rowNum == 0)
            return 0;
        return (down.getY() - up.getY()) + 2 * m_radius + 2 * margin_y;
    }


    private void centering() {
        Hexagon left = getLeftHex();
        Hexagon right = getRightHex();
        Hexagon up = getUpHex();
        Hexagon down = getDownHex();
        float horiz_radius = m_radius * (float) (Math.sqrt(3) / 2);
        float vert_shift = (m_height - calculateHeight(up, down)) / 2 - (up.getY() - m_radius - margin_y);
        float horiz_shift = (m_width - calculateWidth(left, right)) / 2 - (left.getX() - horiz_radius - margin_x);
        Hexagon curr_hex;

        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                curr_hex = m_hexagons.get(i).get(j);
                curr_hex.setX(curr_hex.getX() + horiz_shift);
                curr_hex.setY(curr_hex.getY() + vert_shift);
            }
        }
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

        int hor_long_count = m_colNum;
        int hor_short_count = m_colNum - 1;

        float[] x_pos_long = new float[hor_long_count];
        float[] x_pos_short = new float[hor_short_count];
        ArrayList<Float> y_pos = new ArrayList<>();

        float vert_max_hex = (m_height - 2 * margin_y - (m_rowNum - 1) * padding_y) / (2 + ((m_rowNum - 1)) * (float) 1.5);
        float horiz_max_hex = (m_width - 2 * margin_x - (m_colNum - 1) * padding_x) / ((2 + 2 * (m_colNum - 1)) * (float) Math.sqrt(3) / 2);

        hex_rad = Math.min(vert_max_hex, horiz_max_hex);
        if (m_maxRadius > 0) {
            hex_rad = Math.min(hex_rad, m_maxRadius);
        }
        m_radius = hex_rad;
        float horiz_radius = hex_rad * (float) (Math.sqrt(3) / 2);

        float vert_step = hex_rad * (float) 1.5 + padding_y;
        float horiz_step = horiz_radius * 2 + padding_x;


        if (hor_long_count > 0) {
            x_pos_long[0] = horiz_radius + margin_x;
            for (int i = 1; i < hor_long_count; i++) {
                x_pos_long[i] = x_pos_long[i - 1] + horiz_step;
            }
        }

        if (hor_short_count > 0) {
            x_pos_short[0] = horiz_radius * 2 + margin_x + padding_y / 2;
            for (int i = 1; i < hor_short_count; i++) {
                x_pos_short[i] = x_pos_short[i - 1] + horiz_step;
            }
        }

        if (m_rowNum > 0) {
            y_pos.add(hex_rad + margin_y);
            for (int i = 1; i < m_rowNum; i++) {
                y_pos.add(y_pos.get(i - 1) + (vert_step));
            }
        }
        for (int i = 0; i < m_rowNum; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < hor_long_count; j++) {
                    m_hexagons.get(i).get(j).setParams(grid_zero_x + x_pos_long[j], grid_zero_y + y_pos.get(i), hex_rad);
                }
            } else {
                for (int j = 0; j < hor_short_count; j++) {
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
        this.invalidate();
    }

    public void ResetAllTmpState() {
        for (int i = 0; i < m_hexagons.size(); i++) {

            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).ResetTmpState();
            }
        }
        this.invalidate();
    }


    public void Process_Drag_With_State(ArrayList<Pair<Float, Float>> coord_of_centers, ArrayList<Integer> newStates) {
        //System.out.println("ProcessDragMes");
        Pair<Float, Float> cur_drag_center;
        Hexagon cur_hex;
        int newState;
        float drag_x;
        float drag_y;
        //System.out.println("Size = " + coord_of_centers.size());
        for (int k = 0; k < coord_of_centers.size(); k++) {
            cur_drag_center = coord_of_centers.get(k);
            newState = newStates.get(k);
            drag_x = cur_drag_center.first;
            drag_y = cur_drag_center.second;
            //System.out.println(k + ")hex = " + drag_x + "\t" + drag_y);
            for (int i = 0; i < m_hexagons.size(); i++) {
                for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                    cur_hex = m_hexagons.get(i).get(j);
                    if (cur_hex.IsEmpty() && cur_hex.isContain(drag_x, drag_y)) {
                        cur_hex.setTmpState(newState);
                    }
                }
            }
        }
        this.invalidate();
    }


    public boolean Process_drop_with_state(ArrayList<Pair<Float, Float>> coord_of_centers, ArrayList<Integer> newStates) {
        //System.out.println("ProcessDragMes");
        boolean result = true;
        boolean result_for_one = false;
        Pair<Float, Float> cur_drag_center;
        ArrayList<Pair<Integer, Integer>> index_for_change = new ArrayList<>();
        ArrayList<Integer> value_for_change = new ArrayList<>();

        Hexagon cur_hex;
        for (int k = 0; k < coord_of_centers.size(); k++) {
            result_for_one = false;
            cur_drag_center = coord_of_centers.get(k);
            float drag_x = cur_drag_center.first;
            float drag_y = cur_drag_center.second;
            for (int i = 0; i < m_hexagons.size(); i++) {
                for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                    cur_hex = m_hexagons.get(i).get(j);
                    if (cur_hex.isExist() && cur_hex.IsEmpty() && cur_hex.isContain(drag_x, drag_y)) {
                        index_for_change.add(new Pair<>(i, j));
                        result_for_one = true;
                    }
                }
            }
            result = result && result_for_one;
        }
        if (result) {
            for (int k = 0; k < index_for_change.size(); k++) {
                m_hexagons.get(index_for_change.get(k).first).get(index_for_change.get(k).second).setState(newStates.get(k));
                m_emptyHexCount--;
            }
            DropProcess(index_for_change);
            this.invalidate();
        }
        return result;
    }


    public void setGridParams(int newColNum, int newRowNum) {

        m_rowNum = newRowNum;
        m_colNum = newColNum;
        initGrid();
        isNeedGenerate = true;
    }


    private void initGrid() {
        //System.out.println(m_colNum + " " + m_rowNum);
        m_emptyHexCount = 0;
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
                m_emptyHexCount++;
            }
            m_hexagons.add(tmp);
        }
    }


    public void OnCenter() {
        isNeedGenerate = true;
        invalidate();
    }

    public void deleteHex(int i, int j) {
        //System.out.println("deleteHex");
        if (i >= 0 && i < m_hexagons.size()) {
            if (j >= 0 && j < m_hexagons.get(i).size()) {
                m_hexagons.get(i).get(j).setExist(false);
                this.invalidate();
            }
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //System.out.println("Hex_Grid onDraw");
        isNeedSynchronize();
        if (isNeedGenerate) {
            m_height = getHeight();
            m_width = getWidth();
            createGrid();
            centering();
            isNeedGenerate = false;
            resetHammerState();
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
                    result.add(new Pair<>(cur_hex.getX(), cur_hex.getY()));
            }
        }
        return result;
    }


    ArrayList<Integer> getAllHexagonsState() {
        ArrayList<Integer> result = new ArrayList<>();
        Hexagon cur_hex;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                cur_hex = m_hexagons.get(i).get(j);
                if (cur_hex.isExist())
                    result.add(cur_hex.getState());
            }
        }
        return result;
    }

    public void RefreshAllHexagonsState() {
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).setState(Hexagon.STATE_EMPTY);
            }
        }
        invalidate();
    }


    public void clearTrimedArr() {
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).setExist(true);
                this.invalidate();
            }
        }
        invalidate();
    }


    public void setHexagonState(int i, int j, int new_state) {
        if (i >= 0 && i < m_hexagons.size()) {
            if (j >= 0 && j < m_hexagons.get(i).size()) {
                m_hexagons.get(i).get(j).setState(new_state);
                this.invalidate();
            }
        }
        invalidate();
    }


    public void swapHexagon(int i1, int j1, int i2, int j2) {
        if (i1 < 0 || i1 >= m_hexagons.size() || j1 < 0 || j1 >= m_hexagons.get(i1).size() || i2 < 0 || i2 >= m_hexagons.size() || j2 < 0 || j2 >= m_hexagons.get(i2).size()) {
            return;
        }
        Hexagon first_hex = m_hexagons.get(i1).get(j1);
        Hexagon second_hex = m_hexagons.get(i2).get(j2);
        int tmp_state = first_hex.getState();
        boolean tmp_Exist_flag = first_hex.isExist();

        m_hexagons.get(i1).get(j1).setState(second_hex.getState());
        m_hexagons.get(i1).get(j1).setExist(second_hex.isExist());
        m_hexagons.get(i2).get(j2).setState(tmp_state);
        m_hexagons.get(i2).get(j2).setExist(tmp_Exist_flag);

        invalidate();


    }


    public boolean isHexExist(int i, int j) {
        if (i < 0 || i >= m_hexagons.size() || j < 0 || j >= m_hexagons.get(i).size()) {
            return false;
        }
        return m_hexagons.get(i).get(j).isExist();
    }

    public ArrayList<Pair<Integer, Integer>> Get_near_Hex_index(int i, int j) {
        if (i < 0 || i >= m_hexagons.size() || j < 0 || j >= m_hexagons.get(i).size()) {
            return null;
        }

        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        if (i % 2 == 0) {
            if (isHexExist(i - 1, j - 1)) {
                result.add(new Pair<>(i - 1, j - 1));
            }
            if (isHexExist(i - 1, j)) {
                result.add(new Pair<>(i - 1, j));
            }
            if (isHexExist(i, j - 1)) {
                result.add(new Pair<>(i, j - 1));
            }
            if (isHexExist(i, j + 1)) {
                result.add(new Pair<>(i, j + 1));
            }
            if (isHexExist(i + 1, j - 1)) {
                result.add(new Pair<>(i + 1, j - 1));
            }
            if (isHexExist(i + 1, j)) {
                result.add(new Pair<>(i + 1, j));
            }
        } else {
            if (isHexExist(i - 1, j)) {
                result.add(new Pair<>(i - 1, j));
            }
            if (isHexExist(i - 1, j + 1)) {
                result.add(new Pair<>(i - 1, j + 1));
            }
            if (isHexExist(i, j - 1)) {
                result.add(new Pair<>(i, j - 1));
            }
            if (isHexExist(i, j + 1)) {
                result.add(new Pair<>(i, j + 1));
            }
            if (isHexExist(i + 1, j)) {
                result.add(new Pair<>(i + 1, j));
            }
            if (isHexExist(i + 1, j + 1)) {
                result.add(new Pair<>(i + 1, j + 1));
            }
        }
        return result;
    }

    public void resetAllIsDoneFlag() {
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).setIsDoneFlag(false);
            }
        }
    }

    private void FindChain(int ind_i, int ind_j) {
        Hexagon cur_hex = m_hexagons.get(ind_i).get(ind_j);
        Hexagon cur_neighbour;
        int curState = cur_hex.getState();
        int i;
        int j;
        ArrayList<Pair<Integer, Integer>> neighbour = Get_near_Hex_index(ind_i, ind_j);
        cur_hex.setIsDoneFlag(true);
        m_indexChain.add(new Pair<>(ind_i, ind_j));
        for (int k = 0; k < neighbour.size(); k++) {
            i = neighbour.get(k).first;
            j = neighbour.get(k).second;
            cur_neighbour = m_hexagons.get(i).get(j);
            if ((cur_neighbour.getState() == curState) && (!cur_neighbour.getIsDoneFlag()) && (!cur_neighbour.IsEmpty())) {
                FindChain(i, j);
            }
        }
    }

    private int getIndexOfMinStage(ArrayList<Integer> hexagons) {
        int result = 0;
        int min_state = Hexagon.STATE_MAX + 1; // всегда больше всех других
        for (int i = 0; i < hexagons.size(); i++) {
            if (hexagons.get(i) < min_state) {
                min_state = hexagons.get(i);
                result = i;
            }
        }
        return result;
    }

    private void DropProcess(ArrayList<Pair<Integer, Integer>> chengedHexIndex) {
        ArrayList<Integer> hexagons_state = new ArrayList<>();
        int i, j;
        for (int k = 0; k < chengedHexIndex.size(); k++) {
            i = chengedHexIndex.get(k).first;
            j = chengedHexIndex.get(k).second;
            hexagons_state.add(m_hexagons.get(i).get(j).getState());
        }

        int ind_i, ind_j;
        while (chengedHexIndex.size() > 0) {
            int min_index = getIndexOfMinStage(hexagons_state);
            ind_i = chengedHexIndex.get(min_index).first;
            ind_j = chengedHexIndex.get(min_index).second;
            chengedHexIndex.remove(min_index);
            hexagons_state.remove(min_index);
            while (isThreePosible(ind_i, ind_j)) ;
        }
        invalidate();
        //  System.out.println("count = " + m_emptyHexCount);
    }

    public boolean isThreePosible(int ind_i, int ind_j) {
        boolean result = false;
        m_indexChain.clear();
        FindChain(ind_i, ind_j);
        if (m_indexChain.size() >= MIN_CHAIN_LENGTH) {
            result = true;
            int i, j;
            for (int k = 1; k < m_indexChain.size(); k++) {
                i = m_indexChain.get(k).first;
                j = m_indexChain.get(k).second;
                addScore(m_hexagons.get(i).get(j).getState(), MERGE_SCORE);
                m_hexagons.get(i).get(j).ResetState();
                m_emptyHexCount++;
            }
            if (m_hexagons.get(ind_i).get(ind_j).getState() == Hexagon.STATE_MAX) {
                explosion(ind_i, ind_j);
            } else {
                if (m_hexagons.get(ind_i).get(ind_j).getState() == Hexagon.STATE_EMPTY) {
                    m_emptyHexCount--;
                }
                addScore(m_hexagons.get(ind_i).get(ind_j).getState(), MERGE_SCORE);
                m_hexagons.get(ind_i).get(ind_j).ApdateState(); // ==  m_indexChain.get(0)
                //m_hexagons.get(ind_i).get(ind_j).setState(Hexagon.STATE_EMPTY);
            }


        }
        resetAllIsDoneFlag();
        return result;
    }

    private void explosion(int ind_i, int ind_j) {
        ArrayList<Pair<Integer, Integer>> neighbour = Get_near_Hex_index(ind_i, ind_j);
        int i, j;
        Hexagon cur_hex = m_hexagons.get(ind_i).get(ind_j);
        cur_hex.ResetState();
        for (int k = 0; k < neighbour.size(); k++) {
            i = neighbour.get(k).first;
            j = neighbour.get(k).second;
            if (m_hexagons.get(i).get(j).getState() != Hexagon.STATE_EMPTY) {
                addScore(m_hexagons.get(i).get(j).getState(), EXPLOSION_SCORE);
                m_hexagons.get(i).get(j).ResetState();
                m_emptyHexCount++;
            }
        }
    }

    public boolean IsCanPlace(int quantity) {
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                if (!m_hexagons.get(i).get(j).isExist())
                    continue;
                if (!m_hexagons.get(i).get(j).IsEmpty())
                    continue;

                switch (quantity) {
                    case 1:
                        return true;
                    case 2:
                        ArrayList<Pair<Integer, Integer>> neighbour = Get_near_Hex_index(i, j);
                        int tmp_i, tmp_j;
                        for (int k = 0; k < neighbour.size(); k++) {
                            tmp_i = neighbour.get(k).first;
                            tmp_j = neighbour.get(k).second;
                            if (m_hexagons.get(tmp_i).get(tmp_j).IsEmpty()) {
                                return true;
                            }
                        }
                        break;
                }
            }
        }

        return false;
    }

    public void setHammer(ViewHammerButton new_hammer) {
        hammer = new_hammer;
        resetHammerState();
    }

    public void resetHammerState() {
        boolean hammerState = (hammer != null) && hammer.getState();
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                m_hexagons.get(i).get(j).setHammerState(hammerState);
            }
        }
    }


    public boolean onTouch(View v, MotionEvent evt) {
        if (hammer == null)
            return true;

        System.out.println("hammerState = " + hammer.getState());

        if (hammer.getState()) {
            float x = evt.getX() + this.getX();
            float y = evt.getY() + this.getY();
            Hexagon cur_hex;
            System.out.println("event = " + evt.getAction());
            if (evt.getAction() == MotionEvent.ACTION_UP) {
                System.out.println("MotionEvent.ACTION_UP");
                for (int i = 0; i < m_hexagons.size(); i++) {
                    for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                        cur_hex = m_hexagons.get(i).get(j);
                        if (!cur_hex.IsEmpty() && cur_hex.isContain(x, y)) {
                            addScore(cur_hex.getState(), HAMMER_SCORE);
                            cur_hex.ResetState();
                            invalidate();
                            hammer.done();
                            return true;
                        }
                    }
                }
                hammer.off();
            }
        }
        return true;
    }

    public void setScore(int newScore) {
        m_score = newScore;
    }

    public int getScore() {
        return m_score;
    }

    private void addScore(int state, int score_Type ) {
        int multiplier;
        switch(score_Type)
        {
            case MERGE_SCORE:
                multiplier = 2;
                break;
            case EXPLOSION_SCORE:
                multiplier = 3;
                break;
            case HAMMER_SCORE:
                multiplier = 0;
                break;
            default:
                multiplier = 1;
        }

        m_score += multiplier * state;
    }

    public String getSavedString(){
        Hexagon cur_hex;
        String result = "";
        int state;
        boolean exist_flag;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                cur_hex = m_hexagons.get(i).get(j);
                state = cur_hex.getState();
                exist_flag = cur_hex.isExist();
                if( !result.equals("") )
                    result = result + "-";
                result = result + "(" + state + ", " + exist_flag + ")";
            }
        }
        result = result + "";
        //System.out.println(result);
        return result;
    }

    public void setStateFromString(String src_)
    {
        if(src_.equals(""))
            return;

        String src = src_.replaceAll("\\s","");

        String[] elems;
        ArrayList<Integer> intArr = new ArrayList<>();
        ArrayList<Boolean> isExistArr = new ArrayList<>();

        String intElem;
        String boolElem;
        System.out.println("src = " + src);
        for (String retval : src.split("-")) {
            elems = retval.split(",");
            System.out.println("retval = " + retval);
            System.out.println("elems = " + elems[0]);
            System.out.println("length = " + elems.length);
            intElem = elems[0].replaceAll("\\p{P}",""); // адалим все скобки и пробельные символы
            boolElem = elems[1].replaceAll("\\p{P}","");
            intArr.add(Integer.parseInt(intElem));
            isExistArr.add(Boolean.parseBoolean(boolElem));
        }
        System.out.println("length = " + isExistArr.size());

        int k = 0;
        Hexagon cur_hex;
        for (int i = 0; i < m_hexagons.size(); i++) {
            for (int j = 0; j < m_hexagons.get(i).size(); j++) {
                cur_hex = m_hexagons.get(i).get(j);
                cur_hex.setState(intArr.get(k));
                cur_hex.setExist(isExistArr.get(k));
                k++;
            }
        }
        invalidate();
    }
}