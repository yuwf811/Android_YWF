package com.example.administrator.networktest;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class App {
    private String id;
    private String name;
    private String version;

    public String getId(){
        return  id;
    }

    public String getName(){
        return name;
    }

    public String getVersion(){
        return version;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setVersion(String version){
        this.version = version;
    }
}
