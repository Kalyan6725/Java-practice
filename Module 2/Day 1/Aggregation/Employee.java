class Employee {
    private String name;

    Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPosition(String name) {
        this.name=name;
    }
    
    @Override
    public String toString(){
        return "{Emp name:"+name+"}";
    }
}