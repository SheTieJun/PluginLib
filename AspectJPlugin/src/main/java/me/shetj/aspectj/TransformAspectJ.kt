package me.shetj.aspectj

import com.android.build.api.transform.*
import com.android.build.api.variant.VariantInfo
import com.android.build.gradle.AppExtension
import com.android.utils.FileUtils
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import java.io.File


class TransformAspectJ : Transform {

    private var android: AppExtension? = null
    private var project: Project? = null

    constructor(project: Project, android: AppExtension) {
        this.android = android
        this.project = project
    }

    override fun getName(): String = "AspectJPlugin"

    override fun getInputTypes() = setOf(QualifiedContent.DefaultContentType.CLASSES)

    /**
     * PROJECT: 只有项目内容
     *
     * SUB_PROJECTS: 只有子项目
     *
     * EXTERNAL_LIBRARIES: 只有外部库
     *
     * TESTED_CODE: 测试代码
     *
     * PROVIDED_ONLY: 只提供本地或远程依赖项
     */
    override fun getScopes() = mutableSetOf(QualifiedContent.Scope.PROJECT)

    // ...but also have the rest on our class path
    // these will not be touched by the transformation
    override fun getReferencedScopes() = mutableSetOf(
            QualifiedContent.Scope.SUB_PROJECTS,
            QualifiedContent.Scope.EXTERNAL_LIBRARIES
    )

    /**
     * NOTCHANGED、没有改变
     *
     * ADDED、添加
     *
     * CHANGED、修改
     *
     * REMOVED 移除
     */
    override fun isIncremental() = false

    // only run on debug builds
    override fun applyToVariant(variant: VariantInfo) = variant.isDebuggable

    /**
     * 进行具体转换逻辑.
     * 1. transformInvocation.inputs 会传入工程内所有的 class 文件。
     * 2. 其输出内容是由TransformOutProvider来做处理，
     *  TransformOutputProvider#getContentLocation()方法可以获取文件的输出目录，
     * 3. 子 module 的 java 文件在编译过程中也会生成一个 jar 包然后编译到
     */
    override fun transform(invocation: TransformInvocation) {


//          val mWaitableExecutor = WaitableExecutor.useGlobalSharedThreadPool() //并发编程？

        val incremental = invocation.isIncremental
        if (!incremental) {
            invocation.outputProvider.deleteAll()
        }

        /**
         * 获取输出目录
         */
        val output = invocation.outputProvider.getContentLocation(
                name, outputTypes,
                scopes, Format.DIRECTORY
        )
        if (!isIncremental) {
            if (output.isDirectory) FileUtils.deleteDirectoryContents(output)
        }
        if (!output.exists()) {
            FileUtils.mkdirs(output)
        }
        val input = mutableListOf<File>()  //需要处理的.class
        val classPath = mutableListOf<File>() //指定在何处查找用户类文件,class和source 的位置: classpath 的作用是在当解析一个类的时候，当这个类是不在inpath 中，会从classpath 中寻找。
        val aspectPath = mutableListOf<File>() //将jar文件和目录中的二进制文件编织到所有源中:定义了切面规则的class
        invocation.inputs.forEach { source ->
            //处理源码文件
            source.directoryInputs.forEach { dir ->
                print("${dir.name}")
                input.add(dir.file)
                classPath.add(dir.file)
            }


            //处理jar
            source.jarInputs.forEach { jar ->
                print("${jar.name}")
                input.add(jar.file)
                classPath.add(jar.file)
            }
        }

        /**
         * [getReferencedScopes]
         */
        invocation.referencedInputs.forEach { source ->
            //处理源码文件
            source.directoryInputs.forEach { dir ->
                print("${dir.name}")
                classPath.add(dir.file)
            }
            //处理jar
            source.jarInputs.forEach { jar ->
                print("${jar.name}")
                classPath.add(jar.file)
                aspectPath.add(jar.file)
            }

        }

        weave(classPath, aspectPath, input, output)
    }

    fun weave(
            classPath: Iterable<File>,
            aspectPath: Iterable<File>,
            input: Iterable<File>,
            output: File
    ) {
        val runInAProcess = OperatingSystem.current().isWindows
        val bootClassPath = android!!.bootClasspath

        println("::isIncremental:   $isIncremental")
        println(if (runInAProcess) ":: weaving in a process..." else ":: weaving...")
        println(":: boot class path:  $bootClassPath")
        println(":: 指定在何处查找:class path:       $classPath")//指定在何处查找用户类文件,class和source 的位置: classpath 的作用是在当解析一个类的时候，当这个类是不在inpath 中，会从classpath 中寻找。
        println(":: 定义了切面规则的class:aspect path:      $aspectPath") //将jar文件和目录中的二进制文件编织到所有源中:定义了切面规则的class
        println(":: 需要处理的: input:            $input") //需要处理的.class
        println(":: output:           $output")

        val arguments = listOf(
                "-showWeaveInfo",
                "-1.8",
                "-bootclasspath", bootClassPath.asArgument,
                "-classpath", classPath.asArgument,
                "-aspectpath", aspectPath.asArgument,
                "-inpath", input.asArgument,
                "-d", output.absolutePath
        )

        val handler = MessageHandler(true)
        val main = Main()
        main.run(arguments.toTypedArray(), handler)

        val logger = project!!.logger
        for (msg in handler.getMessages(null, true)) {
            when (msg.kind) {
                IMessage.ABORT, IMessage.ERROR, IMessage.FAIL -> {
                    logger.error(msg.message, msg.thrown)
                    main.quit()
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
        main.quit()
    }


    private val Iterable<File>.asArgument get() = joinToString(File.pathSeparator)
}