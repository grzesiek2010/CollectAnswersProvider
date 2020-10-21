package com.example.collectanswersprovider

import android.app.Application
import android.content.Context
import java.io.File

class CollectAnswersProviderApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: CollectAnswersProviderApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        copySampleFilesToStorage()
    }

    private fun copySampleFilesToStorage() {
        arrayOf(SAMPLE_AUDIO, SAMPLE_VIDEO, SAMPLE_IMAGE, SAMPLE_FILE).forEach {
            instance!!.applicationContext.assets.open(it).use { stream ->
                val file = File("${instance!!.applicationContext.filesDir}/$it")
                if (!file.exists()) {
                    file.outputStream().use { destFile ->
                        stream.copyTo(destFile)
                    }
                }
            }
        }
    }
}