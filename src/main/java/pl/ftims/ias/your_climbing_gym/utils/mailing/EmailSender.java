package pl.ftims.ias.your_climbing_gym.utils.mailing;


public interface EmailSender {
    void sendEmail(String to, String subject, String content);
}
