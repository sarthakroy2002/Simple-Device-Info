package com.neolit.simpledeviceinfo;

public class SysView {
    // TextView 1
    private String mTitle;

    // TextView 1
    private String mResult;

    // create constructor to set the values for all the parameters of each single view
    public SysView(String xTitle, String xResult) {
        mTitle = xTitle;
        mResult = xResult;
    }

    // getter method for returning the ID of the TextView 1
    public String getNumberInDigit() {
        return mTitle;
    }

    // getter method for returning the ID of the TextView 2
    public String getNumbersInText() {
        return mResult;
    }
}
