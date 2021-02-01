package com.example.collectanswersprovider.activities

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
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
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)

        for (questionToPopulate in viewModel.questionsToPopulate) {
            val key = viewModel.getExtraKey(questionToPopulate)
            when {
                questionToPopulate.contains(INTEGER, true) -> intent.putExtra(key, viewModel.getIntegerAnswer())
                questionToPopulate.contains(DECIMAL, true) -> intent.putExtra(key, viewModel.getDecimalAnswer())
                questionToPopulate.contains(TEXT, true) -> intent.putExtra(key, viewModel.getTextAnswer())
                questionToPopulate.contains(IMAGE, true) -> addItem(intent, key, viewModel.getImageAnswer())
                questionToPopulate.contains(AUDIO, true) -> addItem(intent, key, viewModel.getAudioAnswer())
                questionToPopulate.contains(VIDEO, true) -> addItem(intent, key, viewModel.getVideoAnswer())
                questionToPopulate.contains(FILE, true) -> addItem(intent, key, viewModel.getFileAnswer())
            }
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun addItem(intent: Intent, key: String, uri: Uri) {
        intent.putExtra(key, uri)
        if (intent.clipData == null) {
            intent.clipData = ClipData.newRawUri(null, uri)
        } else {
            intent.clipData?.addItem(ClipData.Item(uri))
        }
    }
}
