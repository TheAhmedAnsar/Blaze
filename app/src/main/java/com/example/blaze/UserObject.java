package com.example.blaze;

public class UserObject {

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public UserObject(String uid, String name, String phone, String profileurl) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.profileurl = profileurl;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserObject(String uid) {
        this.uid = uid;
    }

    private String uid;



    private  String name;
    private String phone;

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    private String lastseen;

    public String getProfileurl() {
        return profileurl;
    }

    private String profileurl;


    public  String getPhone(){return  phone ;}

    public String getName(){ return  name; }

    public void setName(String name) {
        if (name==null)
            return;
//        this.name.add(name.trim());
        this.name = name.trim();
    }
}
