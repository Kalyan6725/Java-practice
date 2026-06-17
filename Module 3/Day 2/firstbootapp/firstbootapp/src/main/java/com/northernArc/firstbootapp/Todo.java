package com.northernArc.firstbootapp;

public class Todo {
    private int id;
    private String content;
    private boolean completed;
    public Todo(int id,String content,boolean completed){
        this.id=id;
        this.content=content;
        this.completed=completed;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompletd() {
        return completed;
    }

    public void setCompletd(boolean completd) {
        this.completed = completd;
    }
    public String toString(){
        return "{id:"+this.getId()+" content:"+this.getContent()+" completed:"+this.isCompletd()+"}";
    }
}
