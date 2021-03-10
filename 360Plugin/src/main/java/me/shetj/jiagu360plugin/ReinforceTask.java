package me.shetj.jiagu360plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.BaseVariant;
import com.android.build.gradle.internal.dsl.SigningConfig;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

import me.shetj.utils.CommandUtils;


public class ReinforceTask extends DefaultTask {

    private BaseVariant mVariant;
    private Project mTargetProject;

    public static class ReinforceModel {

        public File reinforceJarFile;
        public String reinforceUsername;
        public String reinforcePassword;

        public File signStoreFile;
        public String signPassword;
        public String signKeyAlias;
        public String signKeyPassword;

        public File mulpkgFile;
        public File inputApkFile;
        public File outputApkFile;
    }


    public void init(BaseVariant variant, Project project) {
        this.mVariant = variant;
        this.mTargetProject = project;
        setDescription("reinforce for apk");
        setGroup("android");
    }

    @TaskAction
    public void reinforceApk() {
        System.out.println("==============start JiaGu");

        ReinforceModel request = initReinforceModel();

        String loginCmdData = "java -jar %s -login %s %s";
        String loginExec = String
                .format(loginCmdData, request.reinforceJarFile.getPath(), request.reinforceUsername,
                        request.reinforcePassword);
        String loginMessage = CommandUtils.exec(loginExec);
        System.out.println("==========exe JiaGu exec: " + loginExec);
        System.out.println("==========exe JiaGu Result: " + loginMessage);
        //into mulpkg
        String mulpkgCmd = "java -jar %s -importmulpkg %s";
        String mulpkgExec = String
                .format(mulpkgCmd, request.reinforceJarFile.getPath(), request.mulpkgFile.getPath());
        String mulpkgResult = CommandUtils.exec(mulpkgExec);
        System.out.println("=========exe  mulpkg  exec : " + mulpkgExec);
        System.out.println("=========exe  mulpkg  Result : " + mulpkgResult);

        //region 导入签名
        String signCmdData = "java -jar %s -importsign %s %s %s %s";
        String signExec = String
                .format(signCmdData, request.reinforceJarFile.getPath(), request.signStoreFile.getPath(),
                        request.signPassword,
                        request.signKeyAlias, request.signKeyPassword);

        //endregion 导入签名
        String signResult = CommandUtils.exec(signExec);
        System.out.println("===========exe JiaGu Sign signExec: " + signExec);
        System.out.println("===========exe JiaGu Sign Result: " + signResult);
        //自动签名,mulpkg
        String suffixCmd = " -autosign -automulpkg ";

        if (!request.outputApkFile.exists()) {
            request.outputApkFile.mkdirs();
        }
        //jiagu.jar ,input apk file path, output apk dir
        String reinforceCmdData = "java -jar %s -jiagu %s %s";
        String reinforceExec = String
                .format(reinforceCmdData, request.reinforceJarFile.getPath(), request.inputApkFile.getPath(),
                        request.outputApkFile.getPath());
        //reinforce
        String jiaguResult = CommandUtils.exec(reinforceExec + suffixCmd);
        System.out.println("===========exe APK JiaGu signExec: " + reinforceExec + suffixCmd);
        System.out.println("=========exe APK JiaGu Result : " + jiaguResult);
    }

    private ReinforceModel initReinforceModel() {
        JG360Extension extension = JG360Extension.getConfig(mTargetProject);

        ReinforceModel request = new ReinforceModel();
        request.inputApkFile = extension.inputApkFile;
        request.outputApkFile = extension.outputFile;
        request.reinforceJarFile = extension.reinforceJarFile;
        request.reinforceUsername = extension.reinforceUsername;
        request.reinforcePassword = extension.reinforcePassword;
        request.mulpkgFile = extension.mulpkgFile;

        NamedDomainObjectContainer<SigningConfig> signingConfigs = ((AppExtension) mTargetProject
                .getExtensions().findByName("android")).getSigningConfigs();

        if (signingConfigs == null) {
            throw new IllegalArgumentException("please config your sign info.");
        }

        for (SigningConfig config : signingConfigs) {
            request.signStoreFile = config.getStoreFile();
            request.signPassword = config.getStorePassword();
            request.signKeyAlias = config.getKeyAlias();
            request.signKeyPassword = config.getKeyPassword();
        }

        if (request.reinforceJarFile == null || !request.reinforceJarFile.exists()){
            throw new GradleException("360 file is not exist!, you should download it!");
        }

        if (request.inputApkFile == null || !request.inputApkFile.exists()) {
            throw new GradleException("apk file is not exist!");
        }

        if (request.mulpkgFile == null || !request.mulpkgFile.exists()) {
            throw new GradleException("mulpkg file is not exist!");
        }

        if (request.signStoreFile == null || !request.signStoreFile.exists()) {
            throw new IllegalArgumentException("please config your sign info.");
        }
        return request;
    }


}