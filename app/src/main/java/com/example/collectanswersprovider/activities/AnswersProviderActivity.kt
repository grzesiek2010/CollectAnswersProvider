package com.example.collectanswersprovider.activities

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.collectanswersprovider.*
import com.example.collectanswersprovider.activities.viewmodels.AnswersProviderActivityViewModel
import kotlinx.android.synthetic.main.activity_answers_provider.*


class AnswersProviderActivity : AppCompatActivity() {
    private val viewModel: AnswersProviderActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers_provider)

        viewModel.addQuestionsToPopulate(intent.extras)

        if (viewModel.questionsToPopulate.isEmpty()) {
            Toast.makeText(this, R.string.no_detected_questions, Toast.LENGTH_LONG).show()
        }

        return_answer_button.setOnClickListener { returnAnswer() }
    }

    private fun returnAnswer() {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            clipData = ClipData.newRawUri(null, null)
        }

        for (questionToPopulate in viewModel.questionsToPopulate) {
            val key = viewModel.getExtraKey(questionToPopulate)
            when {
                questionToPopulate.contains(INTEGER, true) -> intent.putExtra(key, viewModel.getIntegerAnswer())
                questionToPopulate.contains(DECIMAL, true) -> intent.putExtra(key, viewModel.getDecimalAnswer())
                questionToPopulate.contains(TEXT, true) -> intent.putExtra(key, viewModel.getTextAnswer())
                questionToPopulate.contains(IMAGE, true) -> intent.clipData?.addItem(ClipData.Item(key, null, viewModel.getImageAnswer()))
                questionToPopulate.contains(AUDIO, true) -> intent.clipData?.addItem(ClipData.Item(key, null, viewModel.getAudioAnswer()))
                questionToPopulate.contains(VIDEO, true) -> intent.clipData?.addItem(ClipData.Item(key, null, viewModel.getVideoAnswer()))
                questionToPopulate.contains(FILE, true) -> intent.clipData?.addItem(ClipData.Item(key, null, viewModel.getFileAnswer()))
            }
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}