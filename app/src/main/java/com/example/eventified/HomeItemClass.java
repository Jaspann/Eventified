package com.example.eventified;

public class HomeItemClass {
    public static final int LayoutOne = 0;
    public static final int LayoutTwo = 1;

    int viewType;

    String text;

    public HomeItemClass(int viewType, String text)
    {
        this.text = text;
        this.viewType = viewType;
    }

     int icon;
     String text_one, text_two;

    public HomeItemClass(int viewType, int icon, String text_one,
                     String text_two)
    {
        this.icon = icon;
        this.text_one = text_one;
        this.text_two = text_two;
        this.viewType = viewType;
    }
}