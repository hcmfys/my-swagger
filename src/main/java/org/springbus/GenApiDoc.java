package org.springbus;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

public class GenApiDoc {

    public  static  void main(String[] args) {

        DocsConfig config = new DocsConfig();
        config.setProjectPath("E:\\mj-project\\my-swagger\\"); // root project path
        config.setProjectName("ProjectName"); // project name
        config.setApiVersion("V1.0");       // api version
        config.setDocsPath("E:\\mj-project\\my-swagger\\doc\\"); // api docs target path

        config.setAutoGenerate(Boolean.TRUE);  // auto generate
        Docs.buildHtmlDocs(config); // execute to generate
    }
}
