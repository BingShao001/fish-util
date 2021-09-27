package com.yb.fish.victor;
/**
* Extractor 解析文件内容
* @author bing
* @create 27/09/2021
* @version 1.0
**/
public class Extractor implements Visitor{

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("start extract pdf.");
    }

    @Override
    public void visit(PPTFile pptFile) {
        System.out.println("start extract ppt.");
    }

    @Override
    public void visit(WordFile wordFile) {
        System.out.println("start extract wordFile.");
    }
}
