package com.yb.fish.victor;
/**
* Compressor 压缩文件
* @author bing
* @create 27/09/2021
* @version 1.0
**/
public class Compressor implements Visitor{

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("start compress pdf.");
    }

    @Override
    public void visit(PPTFile pptFile) {
        System.out.println("start compress ppt.");
    }

    @Override
    public void visit(WordFile wordFile) {
        System.out.println("start compress wordFile.");
    }
}
