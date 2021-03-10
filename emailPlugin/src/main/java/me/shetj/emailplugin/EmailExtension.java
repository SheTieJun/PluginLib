package me.shetj.emailplugin;


import org.gradle.api.Project;

/**
 * 邮件相关信息
 */
public class EmailExtension {
    public String emailTitle;
    public String emailFrom;
    public String emailTo;
    public String emailHost;
    public String emailUser;
    public String emailPassword;
    public String emailMessage;
    public String emailFile;

    public EmailExtension() {
    }

    public EmailExtension(String emailTitle, String emailFrom, String emailTo, String emailHost, String emailUser, String emailPassword, String emailMessage) {
        this.emailTitle = emailTitle;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.emailHost = emailHost;
        this.emailUser = emailUser;
        this.emailPassword = emailPassword;
        this.emailMessage = emailMessage;
    }

    public EmailExtension(String emailTitle, String emailFrom, String emailTo, String emailHost, String emailUser, String emailPassword, String emailMessage, String emailFile) {
        this.emailTitle = emailTitle;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.emailHost = emailHost;
        this.emailUser = emailUser;
        this.emailPassword = emailPassword;
        this.emailMessage = emailMessage;
        this.emailFile = emailFile;
    }

    public static EmailExtension getConfig(Project project) {
        EmailExtension emailExtension = project.getExtensions().findByType(EmailExtension.class);
        if (emailExtension == null) {
            emailExtension = new EmailExtension();
        }
        return emailExtension;
    }
}
