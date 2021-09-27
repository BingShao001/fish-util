package com.yb.fish.victor;

public class PPTFile extends ResourceFile {
    public PPTFile(String filePath) {
        super(filePath);
    }

    @Override
    void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
