package com.example.quizs

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.quizs.databinding.ActivityMainBinding
import com.example.quizs.databinding.ActivityQuizBinding
import com.example.quizs.databinding.ScoreDailogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityQuizBinding

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
    }

    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val miutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text =
                    String.format("%02d:%02d", miutes, remainingSeconds)
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }

        }.start()
    }

    private fun loadQuestions() {

        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextview.text =
                "Question ${currentQuestionIndex + 1}/ ${questionModelList.size}"
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        val clickBtn = view as Button
        if (clickBtn.id == R.id.next_btn) {

            if (selectedAnswer.isEmpty()) {

                Toast.makeText(
                    applicationContext,
                    "Please Select Answer To Continue",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (selectedAnswer == questionModelList[currentQuestionIndex].correct)
                score++
            currentQuestionIndex++
            loadQuestions()
        } else {

            selectedAnswer = clickBtn.text.toString()
            clickBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }


    private fun finishQuiz() {
        val totalQuestions = questionModelList.size
        val parcentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDailogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = parcentage
            scoreProgressText.text = "$parcentage %"
            if (parcentage > 60) {
                scoreTitle.text = "Congrats! You Have Passed...."
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = "Sorry! You Have Failed..."
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }

}