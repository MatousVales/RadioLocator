package com.example.matous.radiolocator.Models;

import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Matous on 26.02.2016.
 */
public class Measurement {

    private int ID;
    private int count;
    boolean isSelected = false;

    public Measurement(int n, int c){
        this.ID = n;
        this.count = c;
    }

    public int getNumber() {
        return ID;
    }

    public int getCount() {
        return count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
