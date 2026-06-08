class SuperHero{
    private String name;
    String superpower;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public void useSuperPower(){
        System.out.println(name +" uses "+ superpower);
    }
    public static void saveTheWorld(){
        System.out.println("All saves world with power: ");
    }
}