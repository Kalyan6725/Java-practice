package entity;

public class Todo {
    private int id;
    private String task;
    private boolean completed;

    public Todo() {
    }
    public Todo(int id, String task) {
        this.id = id;
        this.task = task;
        this.completed = false;
    }

    public Todo(int id, String task,boolean completed) {
        this.id = id;
        this.task = task;
        this.completed = completed;
    }

    public Todo(String content, boolean done) {
        this.task=content;
        this.completed=done;
    }

    public int getId() {
        return id;
    }
    public String getTask() {
        return task;
    }
    public boolean isCompleted() {
        return completed;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toString() {
        return "Todo{id=" + id + ", task='" + task + "', completed=" + completed + "}";
    }
}