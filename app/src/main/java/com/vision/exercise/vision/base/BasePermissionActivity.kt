package com.vision.exercise.vision.base

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.viewbinding.ViewBinding
import com.vision.exercise.vision.widget.ConfirmDialog
import permissions.dispatcher.*
import com.vision.exercise.R

@RuntimePermissions
abstract class BasePermissionActivity<T : ViewBinding, V : BaseViewModel> : BaseActivity<T, V>() {
    private var requestPermissionDialog: ConfirmDialog? = null

    fun checkPermission() {
        checkPermissionCameraWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun checkPermissionCamera() {
        permissionGranted()
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForWriteExternal(request: PermissionRequest) {
        showPermissionRationale {
            request.proceed()
        }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        showDialogToSettingApp()
    }

    private fun showDialogToSettingApp() {
        requestPermissionDialog = ConfirmDialog(
            getString(R.string.necessary_permission),
            "${getString(R.string.this_app_needs_camera_permission)}. ${getString(R.string.go_to_settings)}",
            getString(R.string.yes),
            getString(R.string.no),
            {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    val uri = Uri.fromParts("package", packageName, null)
                    data = uri
                    startActivity(this)
                }
            },
            {
                //no-ops
            }
        )
    }

    private fun showPermissionRationale(onClickYesListener: () -> Unit) {
        requestPermissionDialog = ConfirmDialog(
            getString(R.string.permission),
            getString(R.string.this_app_needs_camera_permission),
            getString(R.string.yes),
            getString(R.string.no),
            {
                onClickYesListener.invoke()
            },
            {
                // no ops
            }
        )
    }

    open fun permissionGranted() {}
}
