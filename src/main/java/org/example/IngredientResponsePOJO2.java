package org.example;

import java.util.ArrayList;

public class IngredientResponsePOJO2 {
        private boolean success;
        private ArrayList<IngredientResponsePOJO> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<IngredientResponsePOJO> getData() {
        return data;
    }

    public void setData(ArrayList<IngredientResponsePOJO> data) {
        this.data = data;
    }
}
