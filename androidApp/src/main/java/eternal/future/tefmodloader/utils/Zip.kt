package eternal.future.tefmodloader.utils

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object Zip {
    fun copyZipFromResources(fileName: String, destinationDir: String): String {
        Log.d("utility.Zip","开始从资源复制ZIP文件: $fileName 到目录: $destinationDir")
        val inputStream: InputStream =
            object {}.javaClass.getResourceAsStream("/$fileName") ?:
            throw FileNotFoundException("Resource not found: $fileName").also {
                Log.e("utility.Zip", it.message.toString())
            }
        val destFile = File(destinationDir, fileName)

        if (!destFile.parentFile.exists()) {
            Log.i("utility.Zip","目标目录不存在，正在创建: ${destFile.parent}")
            destFile.parentFile.mkdirs()
        }

        try {
            FileOutputStream(destFile).use { fos ->
                Log.v("utility.Zip","开始将输入流写入到文件: ${destFile.absolutePath}")
                inputStream.copyTo(fos)
                Log.v("utility.Zip","成功将输入流写入到文件: ${destFile.absolutePath}")
            }
        } catch (e: IOException) {
            Log.e("utility.Zip","复制ZIP文件时发生IO异常", e)
        }

        Log.d("utility.Zip","完成从资源复制ZIP文件: $fileName 到目录: $destinationDir")
        return destFile.absolutePath
    }

    fun unzipSpecificFilesIgnorePath(zipFilePath: String, destPath: String, vararg fileNames: String) {
        Log.d("utility.Zip","开始解压指定文件: ${fileNames.joinToString(", ")} 从ZIP文件: $zipFilePath 到: $destPath")

        FileInputStream(zipFilePath).use { fis ->
            ZipInputStream(fis).use { zis ->
                var entry: ZipEntry?
                while (zis.nextEntry.also { entry = it } != null) {
                    val fileName = entry!!.name
                    if (fileNames.any { fileName.contains(it) }) {

                        Log.v("utility.Zip","开始解压文件: $fileName 到: $destPath")
                        FileOutputStream(destPath).use { fos ->
                            zis.copyTo(fos)
                        }
                        Log.v("utility.Zip","成功解压文件: $fileName 到: $destPath")
                    }
                    zis.closeEntry()
                }
            }
        }
        Log.d("utility.Zip","完成解压指定文件: ${fileNames.joinToString(", ")} 从ZIP文件: $zipFilePath 到目录: $destPath")
    }
}