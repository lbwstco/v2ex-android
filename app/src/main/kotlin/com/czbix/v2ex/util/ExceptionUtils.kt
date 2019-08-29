package com.czbix.v2ex.util

import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.czbix.v2ex.R
import com.czbix.v2ex.common.exception.*
import com.czbix.v2ex.network.HttpStatus

object ExceptionUtils {
    /**
     * warp exception in [.handleException] with [FatalException]
     */
    @JvmStatic
    fun handleExceptionNoCatch(fragment: androidx.fragment.app.Fragment, ex: Exception): Boolean {
        try {
            return handleException(fragment, ex)
        } catch (e: Exception) {
            throw FatalException(e)
        }

    }

    @JvmStatic
    @Throws(Exception::class)
    fun handleException(fragment: androidx.fragment.app.Fragment, e: Exception): Boolean {
        val activity = fragment.activity
        var needFinishActivity = false
        val stringId: Int

        when (e) {
            is RestrictedException -> {
                needFinishActivity = true
                stringId = R.string.toast_access_restricted
            }
            is UnauthorizedException -> {
                needFinishActivity = true
                stringId = R.string.toast_need_sign_in
            }
            is ConnectionException -> {
                stringId = R.string.toast_connection_exception
            }
            is RemoteException -> {
                stringId = R.string.toast_remote_exception
            }
            is RequestException -> {
                if (e.isShouldLogged) {
                    Crashlytics.logException(e)
                }
                when (e.code) {
                    HttpStatus.SC_FORBIDDEN -> stringId = R.string.toast_access_denied
                    else -> throw e
                }
            }
            is IllegalStateException -> {
                var logException = true
                if (e is ExIllegalStateException) {
                    logException = e.shouldLogged
                }
                if (logException) {
                    Crashlytics.logException(e)
                } else {
                    Crashlytics.log(e.message)
                }

                stringId = R.string.toast_parse_failed
            }
            is RuntimeException -> {
                Crashlytics.logException(e)
                stringId = R.string.toast_parse_failed
            }
            else -> {
                throw e
            }
        }

        if (fragment.userVisibleHint) {
            Toast.makeText(activity, stringId, Toast.LENGTH_LONG).show()
        }
        return needFinishActivity
    }
}
