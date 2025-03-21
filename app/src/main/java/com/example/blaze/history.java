package com.example.blaze;

public class history {




    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }

    private String message;

    public history(String senderuid) {
        this.senderuid = senderuid;
    }

    private String senderuid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public history() {
    }

    public String getPushvalue() {
        return pushvalue;
    }

    public void setPushvalue(String pushvalue) {
        this.pushvalue = pushvalue;
    }

    private String phone;
    private String image;

    public history(String message, String senderuid, String phone, String image, String name, String pushvalue, String timestamp) {
        this.message = message;
        this.senderuid = senderuid;
        this.phone = phone;
        this.image = image;
        this.name = name;
        this.pushvalue = pushvalue;
        this.timestamp = timestamp;
    }

    private String name;
    private String pushvalue;
    String timestamp;




}
