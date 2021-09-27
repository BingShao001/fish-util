package com.yb.fish.victor;

public abstract class ResourceFile {
    private String filePath;

    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }

    abstract void accept(Visitor visitor);

}