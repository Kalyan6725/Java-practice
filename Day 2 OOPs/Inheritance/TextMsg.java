class TextMsg extends SimpleMsg {
    TextMsg() {
        super();
    }

    void sendTextMsg() {
        System.out.println("Text message sent: " + msg);
    }
}