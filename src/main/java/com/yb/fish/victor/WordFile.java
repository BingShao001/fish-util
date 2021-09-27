package com.yb.fish.victor;

public class WordFile extends ResourceFile{
    public WordFile(String filePath) {
        super(filePath);
    }

    @Override
    void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
