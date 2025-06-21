package eternal.future.tefmodloader.utils

import android.util.Log

object EFLog {

    private var printCallerInfo = true
    private const val TAG = "TEFModLoader"

    private fun getCallerInfo(): Triple<String, String, Int> {
        val stackTrace = Thread.currentThread().stackTrace
        for (i in 3 until stackTrace.size) {
            val element = stackTrace[i]
            if ("EFLog" != element.className && EFLog::class.java.name != element.className) {
                var methodName = "Unknown"
                if (i + 1 < stackTrace.size) {
                    methodName = stackTrace[i + 1].methodName
                }
                return Triple("${element.className}.${methodName}", element.fileName ?: "Unknown", element.lineNumber)
            }
        }
        return Triple("Unknown.Unknown", "Unknown", -1)
    }


    fun v(throwable: Throwable? = null, tag: String = TAG, message: () -> String) {
        log(Log.VERBOSE, tag, message(), throwable)
    }

    fun v(messageString: String, throwable: Throwable? = null, tag: String = TAG) {
        log(Log.VERBOSE, tag, messageString, throwable)
    }

    fun d(throwable: Throwable? = null, tag: String = TAG, message: () -> String) {
        log(Log.DEBUG, tag, message(), throwable)
    }

    fun d(messageString: String, throwable: Throwable? = null, tag: String = TAG) {
        log(Log.DEBUG, tag, messageString, throwable)
    }

    fun i(throwable: Throwable? = null, tag: String = TAG, message: () -> String) {
        log(Log.INFO, tag, message(), throwable)
    }

    fun i(messageString: String, throwable: Throwable? = null, tag: String = TAG) {
        log(Log.INFO, tag, messageString, throwable)
    }

    fun w(throwable: Throwable? = null, tag: String = TAG, message: () -> String) {
        log(Log.WARN, tag, message(), throwable)
    }

    fun w(messageString: String, throwable: Throwable? = null, tag: String = TAG) {
        log(Log.WARN, tag, messageString, throwable)
    }

    fun e(throwable: Throwable? = null, tag: String = TAG, message: () -> String) {
        log(Log.ERROR, tag, message(), throwable)
    }

    fun e(messageString: String, throwable: Throwable? = null, tag: String = TAG) {
        log(Log.ERROR, tag, messageString, throwable)
    }

    private fun log(severity: Int, tag: String, message: String, throwable: Throwable?) {
        val callerInfo = if (printCallerInfo) getCallerInfo() else Triple("", "", -1)

        when (severity) {
            Log.VERBOSE -> Log.v(tag,"[${callerInfo.first}, ${callerInfo.second}:${callerInfo.third}]: $message")
            Log.DEBUG -> Log.d(tag, "[${callerInfo.first}, ${callerInfo.second}:${callerInfo.third}]: $message")
            Log.INFO -> Log.i(tag,"[${callerInfo.first}, ${callerInfo.second}:${callerInfo.third}]: $message")
            Log.WARN -> Log.w(tag,"[${callerInfo.first}, ${callerInfo.second}:${callerInfo.third}]: $message")
            Log.ERROR -> Log.e(tag, "[${callerInfo.first}, ${callerInfo.second}:${callerInfo.third}]: $message")
            else -> Log.v(tag, "[${callerInfo.first}, ${callerInfo.second}:${callerInfo.third}]: $message")
        }

        throwable?.let {
            Log.e(tag, it.stackTraceToString())
        }
    }
}