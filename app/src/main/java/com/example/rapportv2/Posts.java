package com.example.rapportv2;

public class Posts {

    public String uid;
    public String username;
    public String description;
    public String date;
    public String city;
    public long counter;
    public String job;
    public String postimage;
    public String time;



    public Posts() {
    }

    public Posts(String uid, String username, String description, String date,
                 String city, long counter, String job, String postimage, String time)
    {
        this.uid = uid;
        this.username = username;
        this.description = description;
        this.date = date;
        this.city = city;
        this.counter = counter;
        this.job = job;
        this.postimage = postimage;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public long getCounter() {
        return counter;
    }

    public String getJob() {
        return job;
    }

    public String getPostimage() {
        return postimage;
    }

    public String getTime() {
        return time;
    }
}
