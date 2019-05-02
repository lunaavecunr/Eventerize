package com.luna.eventerize

import android.app.Application
import com.luna.eventerize.data.repository.EventerizeRepo
import com.parse.Parse
import com.parse.ParseInstallation
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class EventerizeApp: Application() {

    lateinit var repository: EventerizeRepo

    override fun onCreate() {
        super.onCreate()
        app = this
        repository = EventerizeRepo()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("11558475925")
                .clientKey("154811896954758")
                .server("https://bigoud.games/eventerize/")
                .clientBuilder(getOkHttpClientBuilder())
                .build()
        )
        ParseInstallation.getCurrentInstallation().saveInBackground()
    }

    fun getOkHttpClientBuilder() : OkHttpClient.Builder
    {
        // Load CAs from an InputStream
        val certificateFactory = CertificateFactory.getInstance("X.509")

        // Load self-signed certificate (*.crt file)
        val inputStream =  applicationContext.resources.openRawResource(R.raw.bigoudinc)
        val certificate = certificateFactory.generateCertificate(inputStream)
        inputStream.close()

        // Create a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", certificate)

        // Create a TrustManager that trusts the CAs in our KeyStore.
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
        trustManagerFactory.init(keyStore)

        val trustManagers = trustManagerFactory.trustManagers
        val x509TrustManager = trustManagers[0] as X509TrustManager

        // Create an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(x509TrustManager), null)
        var sslSocketFactory = sslContext.socketFactory

        // Create an instance of OkHttpClient
        var m_client = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, x509TrustManager)
            .hostnameVerifier(myHostNameVerifier())

        return m_client
    }

    private fun myHostNameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, _ ->
            if (hostname == "bigoud.games") {
                return@HostnameVerifier true
            }

            false
        }
    }

    companion object {
        private var app: EventerizeApp = EventerizeApp()

        fun getInstance(): EventerizeApp = app
    }
}