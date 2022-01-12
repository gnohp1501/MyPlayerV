package com.example.myplayerv.entities;

public class Icon {
    private int imaView;
    private String iconTitle;

    public Icon(int imaView, String iconTitle) {
        this.imaView = imaView;
        this.iconTitle = iconTitle;
    }

    public int getImaView() {
        return imaView;
    }

    public void setImaView(int imaView) {
        this.imaView = imaView;
    }

    public String getIconTitle() {
        return iconTitle;
    }

    public void setIconTitle(String iconTitle) {
        this.iconTitle = iconTitle;
    }
}
