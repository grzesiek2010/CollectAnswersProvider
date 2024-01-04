package com.example.collectanswersprovider

import android.net.Uri
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.example.collectanswersprovider.CollectAnswersProviderApplication.Companion.applicationContext
import java.io.File
import kotlin.random.Random

class AnswersProviderActivityViewModel : ViewModel() {
    val questionsToPopulate = mutableListOf<String>();

    fun addQuestionsToPopulate(bundle : Bundle?) {
        bundle?.keySet()?.forEach() {
            if (it.startsWith(QUESTION, true)) {
                questionsToPopulate.add(it)
            }
        }
    }

    fun getExtraKey(questionToPopulate : String) = if (questionsToPopulate.size == 1) VALUE else questionToPopulate

    fun getIntegerAnswer() = Random.nextInt(0, 100)

    fun getDecimalAnswer() = Random.nextDouble(0.0, 100.0)

    fun getTextAnswer() : String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..10).map { Random.nextInt(0, charPool.size) }
            .map { charPool[it] }
            .joinToString("")
    }

    fun getImageAnswer(): Uri = getUri(SAMPLE_IMAGE)

    fun getAudioAnswer(): Uri = getUri(SAMPLE_AUDIO)

    fun getVideoAnswer(): Uri = getUri(SAMPLE_VIDEO)

    fun getFileAnswer(): Uri = getUri(SAMPLE_FILE)

    private fun getUri(fileName : String): Uri = FileProvider.getUriForFile(applicationContext(), "com.example.collectanswersprovider.fileprovider", getFile(fileName))

    private fun getFile(fileName : String): File = File("${applicationContext().filesDir}/$fileName")
}