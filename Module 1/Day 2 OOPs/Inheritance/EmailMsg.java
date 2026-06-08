class EmailMsg extends SimpleMsg {
    EmailMsg() {
        super();
    }

    void sendEmailMsg() {
        System.out.println("Email message sent: " + msg);
    }
}