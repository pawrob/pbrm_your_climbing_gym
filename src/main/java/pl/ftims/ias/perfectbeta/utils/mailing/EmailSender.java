package pl.ftims.ias.perfectbeta.utils.mailing;


public interface EmailSender {
    void sendEmail(String to, String subject, String content);
}
