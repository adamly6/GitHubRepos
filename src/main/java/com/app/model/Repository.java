package com.app.model;

public class Repository {

    private String name;
    private boolean fork;
    private Owner owner;

    public String getName() {
        return name;
    }

    public boolean isFork() {
        return fork;
    }

    public Owner getOwner() {
        return owner;
    }


}
