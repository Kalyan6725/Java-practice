class EmailNotification implements NotificationService{
    @Override
    public void notify() {
        System.out.println("Notified by Email");
    }
}