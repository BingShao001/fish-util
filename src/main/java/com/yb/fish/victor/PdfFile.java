package com.yb.fish.victor;

public class PdfFile extends ResourceFile {

    public PdfFile(String filePath) {
        super(filePath);
    }

    @Override
    void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
