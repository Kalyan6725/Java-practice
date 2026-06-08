//Takes names as input and if the person name mateches invite to party else throw an exception
class ExceptionInArrays{
    public static void main(String[] args) {
        for(int i=0;i<args.length;i++){
            System.out.println(args[i]);
        }
        // String[] arr = new String[5];
        // arr={"Kalyan","Harsh","Apuroop","Vishnu","Kedar"};
        // try{
        //     for(String val:arr){
        //         if(val.equals("Kalyan")||val.equals("Harsh")){
        //             System.out.println("Inviting "+val+" to the party");
        //         }
        //         else{
        //             throw new NameNotFound("Not inviting "+val+" to the party");
        //         }
        //     }
        // }
        // catch(NameNotFound e){
        //     System.out.println(e.getMessage());
        // }
        // finally{
        //     System.out.println("Party is on Saturday");
        // }
    }
}