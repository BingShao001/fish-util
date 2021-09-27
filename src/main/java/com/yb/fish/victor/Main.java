package com.yb.fish.victor;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<ResourceFile> resourceFileList = init();
        Visitor extractor = new Extractor();
        Visitor compress = new Compressor();
        for (ResourceFile resourceFile : resourceFileList) {
            // single dispatch java方法重载时是编译时入参类型决定了，调用具体的方法；
            // double dispatch 类似java多态，如方法重写时，有运行时类型决定了，调用具体的方法；
           // extractor.extractContent(resourceFile);
            resourceFile.accept(extractor);
            resourceFile.accept(compress);
        }
    }

    private static List<ResourceFile> init() {
        ResourceFile pdfFile = new PdfFile("bing.pdf");
        ResourceFile pptFile = new PPTFile("bing.ppt");
        ResourceFile wordFile = new WordFile("bing.word");
        List<ResourceFile> resourceFileList = new ArrayList<>();
        resourceFileList.add(pdfFile);
        resourceFileList.add(pptFile);
        resourceFileList.add(wordFile);
        return resourceFileList;
    }
}
