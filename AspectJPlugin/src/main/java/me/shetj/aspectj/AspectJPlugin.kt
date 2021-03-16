package me.shetj.aspectj

import com.android.build.gradle.AppExtension
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import java.io.File

class AspectJPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("Aspectj切片开始编织Class!")
        val appExtension = project
            .extensions.findByName("android") as? AppExtension
        val appVariants = appExtension?.applicationVariants
        appVariants?.all { variant ->
            project.afterEvaluate {
                project.dependencies.apply {
                    val type = project.gradle.gradleVersion.let {
                        if (it.split(".")[0].toInt() >= 3) {
                            "implementation"
                        } else {
                            "compile"
                        }
                    }
                    add(type, "org.aspectj:aspectjrt:1.9.6")
                }
            }

            val javaCompileProvider = variant.javaCompileProvider
            javaCompileProvider.get().doLast { javaCompile ->
                javaCompile as JavaCompile
                arrayOf(
                    "-showWeaveInfo",
                    "-1.8",
                    "-inpath",
                    javaCompile.destinationDir.toString(),
                    "-aspectpath",
                    javaCompile.classpath.asPath,
                    "-d",
                    javaCompile.destinationDir.toString(),
                    "-classpath",
                    javaCompile.classpath.toString(),
                    "-bootclasspath",
                    appExtension.bootClasspath.joinToString(File.pathSeparator)
                )

                val handler = MessageHandler(true)
                Main().run(null, handler)
                val logger = javaCompile.logger
                handler.getMessages(null, true).forEach { msg ->
                    when (msg.kind) {
                        IMessage.ABORT, IMessage.ERROR, IMessage.FAIL -> {
                            logger.error(msg.message, msg.thrown)
                        }
                        IMessage.WARNING -> {
                            logger.warn(msg.message, msg.thrown)
                        }
                        IMessage.INFO -> {
                            logger.info(msg.message, msg.thrown)
                        }
                        IMessage.DEBUG -> {
                            logger.debug(msg.message, msg.thrown)
                        }
                    }
                }
            }
        }
    }
}