package ru.carport.app.presenters

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import ru.carport.app.MainActivity
import ru.carport.app.R

class ManagePermissionsPresenter(val activity: MainActivity, val list: List<String>, val code: Int) {
    fun checkPermissions() {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        }
    }

    private fun isPermissionsGranted(): Int {
        var counter = 0;
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }

    private fun deniedPermission(): List<String> {
        return list.filter { ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_DENIED }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.permission_header)
        builder.setMessage(R.string.permission_text)
        builder.setPositiveButton("OK", { dialog, which -> requestPermissions() })
        builder.setNeutralButton(R.string.cancel, null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun requestPermissions() {
        val deniedPermission = deniedPermission()
        ActivityCompat.requestPermissions(activity, deniedPermission.toTypedArray(), code)
    }
}