package com.example.memory;

import android.widget.Button;
import android.widget.ImageButton;

public class ButtonImage {

    private int buttonID;
    private ImageButton btn;
    private int imageID;

    public ButtonImage(int buttonID, int imageID) {
        this.buttonID = buttonID;
        this.imageID = imageID;
    }

    public ButtonImage(int buttonID, ImageButton btn, int imageID) {
        this.buttonID = buttonID;
        this.btn = btn;
        this.imageID = imageID;
    }

    public int getButtonID() {
        return buttonID;
    }

    public ImageButton getBtn() {
        return btn;
    }

    public int getImageID() {
        return imageID;
    }
}
