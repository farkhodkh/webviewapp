package ru.carport.app.presenters

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.farkhodkhaknazarov.carport.interfaceComponents.CarPortWebChromeClient
import com.farkhodkhaknazarov.carport.interfaceComponents.CarPortWebView
import ru.carport.app.App
import ru.carport.app.MainActivity
import ru.carport.app.R
import javax.inject.Inject

class WebViewPresenter() {
    @Inject
    lateinit var webChromeClient: CarPortWebChromeClient

    @Inject
    lateinit var webViewClient: CarPortWebView

    @Inject
    lateinit var managePermissions: ManagePermissionsPresenter

    companion object {
        val PermissionsRequestCode = 100

        fun getPermissionList() = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    lateinit var activity: MainActivity


    lateinit var webView: WebView

    var uploadMessage: ValueCallback<Array<Uri>>? = null


    val RequestSelectFile = 100

    val FileChooserResultCode = 1

    fun prepareWebView(activity: MainActivity) {

        App.getComponent().injectPresenter(this)

        checkAppPermission(activity)

        this.activity = activity

        webView = activity.findViewById<WebView>(R.id.wv_car_port)
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setGeolocationDatabasePath(activity.getFilesDir().getPath());
        webView.setWebViewClient(webViewClient)
        webView.setWebChromeClient(webChromeClient);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
//        webView.loadUrl("https://client.carport.ru/");
        webView.loadUrl("https://devclient.datcar.ru/");

        webChromeClient.webViewPresenter = this
        webChromeClient.mainActivity = activity


    }

    fun checkAppPermission(mainActivity: MainActivity) {
        val list = getPermissionList()

        managePermissions = ManagePermissionsPresenter(mainActivity, list, PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode === RequestSelectFile) {
            uploadMessage?.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    data
                )
            );
            uploadMessage = null;
        } else if (requestCode === FileChooserResultCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var results: Array<Uri> = arrayOf()
                if (data != null) {
                    results = arrayOf(Uri.parse(data?.getDataString()));
                } else {
                    try {
                        val file_path = webChromeClient.mCameraPhotoPath
                        results = arrayOf(Uri.parse(file_path))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                uploadMessage?.onReceiveValue(results);
                uploadMessage = null
            }
        }
    }
}