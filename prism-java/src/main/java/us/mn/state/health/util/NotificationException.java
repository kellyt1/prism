package us.mn.state.health.util;

public class NotificationException extends Exception {
    private Notification notification;

    public NotificationException(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}
