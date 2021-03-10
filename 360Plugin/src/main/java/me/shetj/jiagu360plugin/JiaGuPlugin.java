package me.shetj.jiagu360plugin;


import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

class JiaGuPlugin implements Plugin<Project> {

    public static final String PLUGIN_EXTENSION_NAME = "jiaGuHelper";
    public static final String ANDROID_EXTENSION_N = "android";

    @Override
    public void apply(Project project) {
        JG360Extension customExtension = project.getExtensions().create(PLUGIN_EXTENSION_NAME, JG360Extension.class);
        if (customExtension.reinforceJarFile == null){
            return;
        }
        //项目编译完成后，回调
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                DomainObjectSet<ApplicationVariant> appVariants = ((AppExtension) project
                        .getExtensions().findByName(ANDROID_EXTENSION_N)).getApplicationVariants();
                for (ApplicationVariant variant : appVariants) {
                    if (variant.getBuildType().getName().equals("release")) {
                        String variantName =
                                variant.getName().substring(0, 1).toUpperCase() + variant.getName().substring(1);
                        if (!customExtension.isAssemble) {
                            ReinforceTask reinforceTask = project.getTasks()
                                    .create("Apk360JG"+variantName, ReinforceTask.class);
                            reinforceTask.init(variant, project);
                            return;
                        } else {
                            ReinforceTask reinforceTask = project.getTasks()
                                    .create("Apk"+variantName+"And360JG", ReinforceTask.class);
                            reinforceTask.init(variant, project);
                            variant.getAssembleProvider().get().dependsOn(project.getTasks().findByName("clean"));
                            reinforceTask.dependsOn(variant.getAssembleProvider().get());
                            return;
                        }
                    }
                }
            }
        });
    }
}