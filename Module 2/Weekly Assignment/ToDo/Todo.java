public class Todo {

    private int id;
    private String task;
    private boolean completed;

    public Todo(int id, String task) {
        this.id = id;
        this.task = task;
        this.completed = false;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toString() {
        return "Todo{id=" + id + ", task='" + task + "', completed=" + completed + "}";
    }
}