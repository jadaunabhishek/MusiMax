package com.example.onlinemusicplayer;

//write and retreive data, we use this constructor

public class readwriteuserdata {
    public String FirstName, PhoneNumber, LastName;

    public readwriteuserdata(){};

    public readwriteuserdata(String textfirstname, String textphonenumber, String textlastname) {

        this.FirstName = textfirstname;
        this.PhoneNumber = textphonenumber;
        this.LastName = textlastname;
    }
}
