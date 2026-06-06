class Doctor{
    String name;
    String speciality;
    int experience;

    Doctor(String name, String speciality, int experience){
        this.name = name;
        this.speciality = speciality;
        this.experience = experience;
    }

    void diagnose(){
        System.out.println(name + " is diagnosing a patient.");
    }
    void treat(){
        System.out.println(name + " is treating a patient.");
    }
    void displayDetails(){
        System.out.println("Name: " + name);
        System.out.println("Speciality: " + speciality);
        System.out.println("Experience: " + experience + " years");
    }
}