package com.example.wenzitong.entity;

/**
 * Created by 邹特强 on 2017/11/23.
 * 生词本item对应的javaBean类
 */

public class NewWordBean {
    private String wordName;
    private String explanation;
    public NewWordBean(){

    }
    /**
     * 构造器
     * @param wordName 生词
     * @param explanation 生词的解释
     */
    public NewWordBean(String wordName,String explanation){
        this.wordName=wordName;
        this.explanation=explanation;
    }

    public String getWordName() {

        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

}
