package me.shetj.jiagu360plugin;

import org.gradle.api.Project;

import java.io.File;

public class JG360Extension {

    public boolean isAssemble;
    public File reinforceJarFile;
    public String reinforceUsername;
    public String reinforcePassword;
    public File inputApkFile;
    public File outputFile;
    public File mulpkgFile;

    public JG360Extension() {
    }


    public JG360Extension(File inputApkFile, File reinforceJarFile,
                          String reinforceUsername, String reinforcePassword, File outputFile, File mulpkgFile) {
        this.inputApkFile = inputApkFile;
        this.reinforceJarFile = reinforceJarFile;
        this.reinforceUsername = reinforceUsername;
        this.reinforcePassword = reinforcePassword;
        this.outputFile = outputFile;
        this.mulpkgFile = mulpkgFile;
    }

    public JG360Extension(boolean isAssemble, File reinforceJarFile, String reinforceUsername, String reinforcePassword,
                          File inputApkFile, File outputFile, File mulpkgFile) {
        this.isAssemble = isAssemble;
        this.reinforceJarFile = reinforceJarFile;
        this.reinforceUsername = reinforceUsername;
        this.reinforcePassword = reinforcePassword;
        this.inputApkFile = inputApkFile;
        this.outputFile = outputFile;
        this.mulpkgFile = mulpkgFile;
    }

    public static JG360Extension getConfig(Project project) {
        JG360Extension extension = project.getExtensions().findByType(JG360Extension.class);
        if (extension == null) {
            extension = new JG360Extension();
        }
        return extension;
    }


}