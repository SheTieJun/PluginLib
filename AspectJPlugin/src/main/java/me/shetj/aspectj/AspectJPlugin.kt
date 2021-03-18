package me.shetj.aspectj

import com.android.build.gradle.AppExtension
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.compile.JavaCompile
import java.io.File

class AspectJPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val appExtension = project
                .extensions.findByName("android") as? AppExtension
        val appVariants = appExtension?.applicationVariants
        appVariants?.all { variant ->

            project.dependencies.apply {
                println("gradleVersion:${project.gradle.gradleVersion}")
                val type = project.gradle.gradleVersion.let {
                    if (it.split(".")[0].toInt() >= 4) {
                        "implementation"
                    } else {
                        "compile"
                    }
                }
                add(type, "org.aspectj:aspectjrt:1.9.6")
            }
            variant.outputs.all { out ->
                println(out.name)
                val fullName = out.name
                val javaCompileProvider = variant.javaCompileProvider
                javaCompileProvider.get().doLast { javaCompile ->
                    javaCompile as JavaCompile
                    val (logger, arrayOf, kotlinArgs) = getAjxRunMsg(javaCompile, appExtension, project, fullName)
                    ajxRun(arrayOf, kotlinArgs, project.logger)
                }
            }
        }
    }

    private fun getAjxRunMsg(javaCompile: JavaCompile, appExtension: AppExtension, project: Project, fullName: String): Triple<Logger, Array<String>, Array<String>> {
        val logger = javaCompile.logger
        val arrayOf = arrayOf(
                "-showWeaveInfo",
                "-1.8",
                "-inpath",
                javaCompile.destinationDir.toString(),
                "-aspectpath",
                javaCompile.classpath.asPath,
                "-d",
                javaCompile.destinationDir.toString(),
                "-classpath",
                javaCompile.classpath.asPath,
                "-bootclasspath",
                appExtension.bootClasspath.joinToString(File.pathSeparator)
        )

        arrayOf.forEach {
            println(it)
        }

        val kotlinArgs = arrayOf("-showWeaveInfo",
                "-1.8",
                "-inpath", "${project.buildDir.path}/tmp/kotlin-classes/$fullName",
                "-aspectpath", javaCompile.classpath.asPath,
                "-d", "${project.buildDir.path}/tmp/kotlin-classes/$fullName",
                "-classpath", javaCompile.classpath.asPath,
                "-bootclasspath", appExtension.bootClasspath.joinToString(
                File.pathSeparator))
        kotlinArgs.forEach {
            println(it)
        }
        return Triple(logger, arrayOf, kotlinArgs)
    }

    private fun ajxRun(arrayOf: Array<String>, kotlinArgs: Array<String>, logger: Logger) {
        val handler = MessageHandler(true)
        print("开始执行AspectJ")
        val main = Main()
        main.run(arrayOf, handler) // 关键句子执行AOP
        val main2 = Main()
        main2.run(kotlinArgs, handler)
        println("结束AspectJ")
        handler.getMessages(null, true).forEach { msg ->
            when (msg.kind) {
                IMessage.ABORT, IMessage.ERROR, IMessage.FAIL -> {
                    logger.error(msg.message, msg.thrown)
                    main.quit()
                    main2.quit()
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