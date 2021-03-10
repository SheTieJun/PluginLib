package com.shetj.utils

import com.shetj.utils.CloseUtils.Companion.closeIO
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*

class FileIOUtils private constructor() {
    companion object {
        private val LINE_SEP = System.getProperty("line.separator")
        private var sBufferSize = 8192
        //region writeFile 写文件
        /**
         * 将输入流写入文件
         *
         * @param filePath 路径
         * @param is       输入流
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromIS(filePath: String, `is`: InputStream?): Boolean {
            return writeFileFromIS(
                getFileByPath(
                    filePath
                ), `is`, false
            )
        }

        /**
         * 将输入流写入文件
         *
         * @param filePath 路径
         * @param is       输入流
         * @param append   是否追加在文件末
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromIS(
            filePath: String,
            `is`: InputStream?,
            append: Boolean
        ): Boolean {
            return writeFileFromIS(
                getFileByPath(
                    filePath
                ), `is`, append
            )
        }
        /**
         * 将输入流写入文件
         *
         * @param file   文件
         * @param is     输入流
         * @param append 是否追加在文件末
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        /**
         * 将输入流写入文件
         *
         * @param file 文件
         * @param is   输入流
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        @JvmOverloads
        fun writeFileFromIS(
            file: File?,
            `is`: InputStream?,
            append: Boolean = false
        ): Boolean {
            if (!createOrExistsFile(file) || `is` == null) return false
            var os: OutputStream? = null
            return try {
                os = BufferedOutputStream(FileOutputStream(file, append))
                val data = ByteArray(sBufferSize)
                var len: Int
                while (`is`.read(data, 0, sBufferSize)
                        .also { len = it } != -1
                ) {
                    os.write(data, 0, len)
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                closeIO(`is`, os)
            }
        }

        /**
         * 将字节数组写入文件
         *
         * @param filePath 文件路径
         * @param bytes    字节数组
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByStream(
            filePath: String,
            bytes: ByteArray?
        ): Boolean {
            return writeFileFromBytesByStream(
                getFileByPath(
                    filePath
                ), bytes, false
            )
        }

        /**
         * 将字节数组写入文件
         *
         * @param filePath 文件路径
         * @param bytes    字节数组
         * @param append   是否追加在文件末
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByStream(
            filePath: String,
            bytes: ByteArray?,
            append: Boolean
        ): Boolean {
            return writeFileFromBytesByStream(
                getFileByPath(
                    filePath
                ), bytes, append
            )
        }
        /**
         * 将字节数组写入文件
         *
         * @param file   文件
         * @param bytes  字节数组
         * @param append 是否追加在文件末
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        /**
         * 将字节数组写入文件
         *
         * @param file  文件
         * @param bytes 字节数组
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        @JvmOverloads
        fun writeFileFromBytesByStream(
            file: File?,
            bytes: ByteArray?,
            append: Boolean = false
        ): Boolean {
            if (bytes == null || !createOrExistsFile(file)) return false
            var bos: BufferedOutputStream? = null
            return try {
                bos = BufferedOutputStream(FileOutputStream(file, append))
                bos.write(bytes)
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                closeIO(bos)
            }
        }

        /**
         * 将字节数组写入文件
         *
         * @param filePath 文件路径
         * @param bytes    字节数组
         * @param isForce  是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByChannel(
            filePath: String,
            bytes: ByteArray?,
            isForce: Boolean
        ): Boolean {
            return writeFileFromBytesByChannel(
                getFileByPath(
                    filePath
                ), bytes, false, isForce
            )
        }

        /**
         * 将字节数组写入文件
         *
         * @param filePath 文件路径
         * @param bytes    字节数组
         * @param append   是否追加在文件末
         * @param isForce  是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByChannel(
            filePath: String,
            bytes: ByteArray?,
            append: Boolean,
            isForce: Boolean
        ): Boolean {
            return writeFileFromBytesByChannel(
                getFileByPath(
                    filePath
                ), bytes, append, isForce
            )
        }

        /**
         * 将字节数组写入文件
         *
         * @param file    文件
         * @param bytes   字节数组
         * @param isForce 是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByChannel(
            file: File?,
            bytes: ByteArray?,
            isForce: Boolean
        ): Boolean {
            return writeFileFromBytesByChannel(file, bytes, false, isForce)
        }

        /**
         * 将字节数组写入文件
         *
         * @param file    文件
         * @param bytes   字节数组
         * @param append  是否追加在文件末
         * @param isForce 是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByChannel(
            file: File?,
            bytes: ByteArray?,
            append: Boolean,
            isForce: Boolean
        ): Boolean {
            if (bytes == null) return false
            var fc: FileChannel? = null
            return try {
                fc = FileOutputStream(file, append).channel
                fc.position(fc.size())
                fc.write(ByteBuffer.wrap(bytes))
                if (isForce) fc.force(true)
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                closeIO(fc)
            }
        }

        /**
         * 将字节数组写入文件
         *
         * @param filePath 文件路径
         * @param bytes    字节数组
         * @param isForce  是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByMap(
            filePath: String,
            bytes: ByteArray?,
            isForce: Boolean
        ): Boolean {
            return writeFileFromBytesByMap(filePath, bytes, false, isForce)
        }

        /**
         * 将字节数组写入文件
         *
         * @param filePath 文件路径
         * @param bytes    字节数组
         * @param append   是否追加在文件末
         * @param isForce  是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByMap(
            filePath: String,
            bytes: ByteArray?,
            append: Boolean,
            isForce: Boolean
        ): Boolean {
            return writeFileFromBytesByMap(
                getFileByPath(
                    filePath
                ), bytes, append, isForce
            )
        }

        /**
         * 将字节数组写入文件
         *
         * @param file    文件
         * @param bytes   字节数组
         * @param isForce 是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByMap(
            file: File?,
            bytes: ByteArray?,
            isForce: Boolean
        ): Boolean {
            return writeFileFromBytesByMap(file, bytes, false, isForce)
        }

        /**
         * 将字节数组写入文件
         *
         * @param file    文件
         * @param bytes   字节数组
         * @param append  是否追加在文件末
         * @param isForce 是否写入文件
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromBytesByMap(
            file: File?,
            bytes: ByteArray?,
            append: Boolean,
            isForce: Boolean
        ): Boolean {
            if (bytes == null || !createOrExistsFile(file)) return false
            var fc: FileChannel? = null
            return try {
                fc = FileOutputStream(file, append).channel
                val mbb = fc.map(
                    FileChannel.MapMode.READ_WRITE,
                    fc.size(),
                    bytes.size.toLong()
                )
                mbb.put(bytes)
                if (isForce) mbb.force()
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                closeIO(fc)
            }
        }

        /**
         * 将字符串写入文件
         *
         * @param filePath 文件路径
         * @param content  写入内容
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromString(filePath: String, content: String?): Boolean {
            return writeFileFromString(
                getFileByPath(
                    filePath
                ), content, false
            )
        }

        /**
         * 将字符串写入文件
         *
         * @param filePath 文件路径
         * @param content  写入内容
         * @param append   是否追加在文件末
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        fun writeFileFromString(
            filePath: String,
            content: String?,
            append: Boolean
        ): Boolean {
            return writeFileFromString(
                getFileByPath(
                    filePath
                ), content, append
            )
        }
        /**
         * 将字符串写入文件
         *
         * @param file    文件
         * @param content 写入内容
         * @param append  是否追加在文件末
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        /**
         * 将字符串写入文件
         *
         * @param file    文件
         * @param content 写入内容
         * @return `true`: 写入成功<br></br>`false`: 写入失败
         */
        @JvmOverloads
        fun writeFileFromString(
            file: File?,
            content: String?,
            append: Boolean = false
        ): Boolean {
            if (file == null || content == null) return false
            if (!createOrExistsFile(file)) return false
            var bw: BufferedWriter? = null
            return try {
                bw = BufferedWriter(FileWriter(file, append))
                bw.write(content)
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                closeIO(bw)
            }
        }
        //endregion writeFile 写文件
        //region readFile 读取文件
        ///////////////////////////////////////////////////////////////////////////
        // the divide line of write and read
        ///////////////////////////////////////////////////////////////////////////
        /**
         * 读取文件到字符串链表中
         *
         * @param filePath 文件路径
         * @return 字符串链表中
         */
        fun readFile2List(filePath: String): List<String>? {
            return readFile2List(
                getFileByPath(filePath),
                null
            )
        }

        /**
         * 读取文件到字符串链表中
         *
         * @param filePath    文件路径
         * @param charsetName 编码格式
         * @return 字符串链表中
         */
        fun readFile2List(
            filePath: String,
            charsetName: String?
        ): List<String>? {
            return readFile2List(
                getFileByPath(filePath),
                charsetName
            )
        }

        /**
         * 读取文件到字符串链表中
         *
         * @param file        文件
         * @param charsetName 编码格式
         * @return 字符串链表中
         */
        fun readFile2List(
            file: File?,
            charsetName: String?
        ): List<String>? {
            return readFile2List(file, 0, 0x7FFFFFFF, charsetName)
        }

        /**
         * 读取文件到字符串链表中
         *
         * @param filePath 文件路径
         * @param st       需要读取的开始行数
         * @param end      需要读取的结束行数
         * @return 字符串链表中
         */
        fun readFile2List(
            filePath: String,
            st: Int,
            end: Int
        ): List<String>? {
            return readFile2List(
                getFileByPath(filePath),
                st,
                end,
                null
            )
        }

        /**
         * 读取文件到字符串链表中
         *
         * @param filePath    文件路径
         * @param st          需要读取的开始行数
         * @param end         需要读取的结束行数
         * @param charsetName 编码格式
         * @return 字符串链表中
         */
        fun readFile2List(
            filePath: String,
            st: Int,
            end: Int,
            charsetName: String?
        ): List<String>? {
            return readFile2List(
                getFileByPath(filePath),
                st,
                end,
                charsetName
            )
        }
        /**
         * 读取文件到字符串链表中
         *
         * @param file        文件
         * @param st          需要读取的开始行数
         * @param end         需要读取的结束行数
         * @param charsetName 编码格式
         * @return 字符串链表中
         */
        /**
         * 读取文件到字符串链表中
         *
         * @param file 文件
         * @return 字符串链表中
         */
        /**
         * 读取文件到字符串链表中
         *
         * @param file 文件
         * @param st   需要读取的开始行数
         * @param end  需要读取的结束行数
         * @return 字符串链表中
         */
        @JvmOverloads
        fun readFile2List(
            file: File?,
            st: Int = 0,
            end: Int = 0x7FFFFFFF,
            charsetName: String? = null
        ): List<String>? {
            if (!isFileExists(file)) return null
            if (st > end) return null
            var reader: BufferedReader? = null
            return try {
                var line: String
                var curLine = 1
                val list: MutableList<String> =
                    ArrayList()
                reader = if (isSpace(charsetName)) {
                    BufferedReader(
                        InputStreamReader(
                            FileInputStream(file)
                        )
                    )
                } else {
                    BufferedReader(
                        InputStreamReader(
                            FileInputStream(file), charsetName
                        )
                    )
                }
                while (reader.readLine().also { line = it } != null) {
                    if (curLine > end) break
                    if (st <= curLine && curLine <= end) list.add(line)
                    ++curLine
                }
                list
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                closeIO(reader)
            }
        }

        /**
         * 读取文件到字符串中
         *
         * @param filePath 文件路径
         * @return 字符串
         */
        fun readFile2String(filePath: String): String? {
            return readFile2String(
                getFileByPath(
                    filePath
                ), null
            )
        }

        /**
         * 读取文件到字符串中
         *
         * @param filePath    文件路径
         * @param charsetName 编码格式
         * @return 字符串
         */
        fun readFile2String(filePath: String, charsetName: String?): String? {
            return readFile2String(
                getFileByPath(
                    filePath
                ), charsetName
            )
        }
        /**
         * 读取文件到字符串中
         *
         * @param file        文件
         * @param charsetName 编码格式
         * @return 字符串
         */
        /**
         * 读取文件到字符串中
         *
         * @param file 文件
         * @return 字符串
         */
        @JvmOverloads
        fun readFile2String(
            file: File?,
            charsetName: String? = null
        ): String? {
            if (!isFileExists(file)) return null
            var reader: BufferedReader? = null
            return try {
                val sb = StringBuilder()
                reader = if (isSpace(charsetName)) {
                    BufferedReader(
                        InputStreamReader(
                            FileInputStream(file)
                        )
                    )
                } else {
                    BufferedReader(
                        InputStreamReader(
                            FileInputStream(file), charsetName
                        )
                    )
                }
                var line: String?
                if (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(LINE_SEP).append(line)
                    }
                }
                sb.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                closeIO(reader)
            }
        }

        /**
         * 读取文件到字节数组中
         *
         * @param filePath 文件路径
         * @return 字符数组
         */
        fun readFile2BytesByStream(filePath: String): ByteArray? {
            return readFile2BytesByStream(
                getFileByPath(
                    filePath
                )
            )
        }

        /**
         * 读取文件到字节数组中
         *
         * @param file 文件
         * @return 字符数组
         */
        fun readFile2BytesByStream(file: File?): ByteArray? {
            if (!isFileExists(file)) return null
            var fis: FileInputStream? = null
            var os: ByteArrayOutputStream? = null
            return try {
                fis = FileInputStream(file)
                os = ByteArrayOutputStream()
                val b = ByteArray(sBufferSize)
                var len: Int
                while (fis.read(b, 0, sBufferSize)
                        .also { len = it } != -1
                ) {
                    os.write(b, 0, len)
                }
                os.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                closeIO(fis, os)
            }
        }

        /**
         * 读取文件到字节数组中
         *
         * @param filePath 文件路径
         * @return 字符数组
         */
        fun readFile2BytesByChannel(filePath: String): ByteArray? {
            return readFile2BytesByChannel(
                getFileByPath(
                    filePath
                )
            )
        }

        /**
         * 读取文件到字节数组中
         *
         * @param file 文件
         * @return 字符数组
         */
        fun readFile2BytesByChannel(file: File?): ByteArray? {
            if (!isFileExists(file)) return null
            var fc: FileChannel? = null
            return try {
                fc = RandomAccessFile(file, "r").channel
                val byteBuffer = ByteBuffer.allocate(fc.size().toInt())
                while (true) {
                    if (fc.read(byteBuffer) <= 0) break
                }
                byteBuffer.array()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                closeIO(fc)
            }
        }

        /**
         * 读取文件到字节数组中
         *
         * @param filePath 文件路径
         * @return 字符数组
         */
        fun readFile2BytesByMap(filePath: String): ByteArray? {
            return readFile2BytesByMap(
                getFileByPath(
                    filePath
                )
            )
        }

        /**
         * 读取文件到字节数组中
         *
         * @param file 文件
         * @return 字符数组
         */
        fun readFile2BytesByMap(file: File?): ByteArray? {
            if (!isFileExists(file)) return null
            var fc: FileChannel? = null
            return try {
                fc = RandomAccessFile(file, "r").channel
                val size = fc.size().toInt()
                val mbb =
                    fc.map(FileChannel.MapMode.READ_ONLY, 0, size.toLong()).load()
                val result = ByteArray(size)
                mbb[result, 0, size]
                result
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                closeIO(fc)
            }
        }
        //endregion readFile
        //region 判断是否存在或者创建
        /**
         * 设置缓冲区尺寸
         *
         * @param bufferSize 缓冲区大小
         */
        fun setBufferSize(bufferSize: Int) {
            sBufferSize = bufferSize
        }

        private fun getFileByPath(filePath: String): File? {
            return if (isSpace(filePath)) null else File(filePath)
        }

        private fun createOrExistsFile(filePath: String): Boolean {
            return createOrExistsFile(
                getFileByPath(
                    filePath
                )
            )
        }

        private fun createOrExistsFile(file: File?): Boolean {
            if (file == null) return false
            if (file.exists()) return file.isFile
            return if (!createOrExistsDir(file.parentFile)) false else try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        private fun createOrExistsDir(file: File?): Boolean {
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        private fun isFileExists(file: File?): Boolean {
            return file != null && file.exists()
        }

        private fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        } //endregion 判断是否存在或者创建
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}