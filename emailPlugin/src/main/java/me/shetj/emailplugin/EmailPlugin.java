package me.shetj.emailplugin;


import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class EmailPlugin implements Plugin<Project> {

    public static final String PLUGIN_EXTENSION_NAME = "EmailHelper";

    @Override
    public void apply(Project project) {
        EmailExtension emailExtension = project.getExtensions().create(PLUGIN_EXTENSION_NAME, EmailExtension.class);
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                EmailExtension config = EmailExtension.getConfig(project);
                if (config.emailFrom == null||config.emailTo == null) {
                    return;
                }
                SendEmailTask sendEmail = project.getTasks().create("sendEmail", SendEmailTask.class);
                sendEmail.init(project);
            }
        });
    }
}
