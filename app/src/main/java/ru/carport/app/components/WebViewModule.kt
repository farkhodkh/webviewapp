package ru.carport.app.components

import com.farkhodkhaknazarov.carport.interfaceComponents.CarPortWebChromeClient
import com.farkhodkhaknazarov.carport.interfaceComponents.CarPortWebView
import dagger.Module
import dagger.Provides
import ru.carport.app.presenters.WebViewPresenter

@Module
class WebViewModule {

    @Provides
    fun providesCarPortWebChromeClient(): CarPortWebChromeClient = CarPortWebChromeClient()

    @Provides
    fun providesCarPortWebView(): CarPortWebView = CarPortWebView()

    @Provides
    fun providesWebViewPresenter(): WebViewPresenter = WebViewPresenter();
}