package com.example.brain;

public class SimpleData {
    private String name;
    private String sketches;
    public SimpleData(String name, String sketches) {
        this.name  = name;
        this.sketches = sketches;
    }
    public String getName(){
        return this.name;
    }
    public String getSketches() {
        return this.sketches;
    }
}
