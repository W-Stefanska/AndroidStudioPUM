@file:Suppress("SpellCheckingInspection")

package com.example.quizlista

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Question(
    val questionId: Int,
    val questionText: String,
    val answers: List<Answer>
)

data class Answer(
    val answerId: Int,
    val answerText: String
)

val questions = listOf(
    Question(
        questionId = 1,
        questionText = "W jakim stanie skupienia substancje mają największą energię wewnętrzną?",
        answers = listOf(
            Answer(1, "Gazowym"),
            Answer(2, "Ciekłym"),
            Answer(3, "Stałym"),
            Answer(4, "Wszystkie mają taką samą energię")
        )
    ),
    Question(
        questionId = 2,
        questionText = "Co wyraża zasada zachowania energii?",
        answers = listOf(
            Answer(1, "Całkowita energia układu pozostaje stała"),
            Answer(2, "Energia zależy od masy ciała"),
            Answer(3, "Energia nie zmienia swojej formy"),
            Answer(4, "Energia może być tworzona i niszczona")
        )
    ),
    Question(
        questionId = 3,
        questionText = "Który jest teraz rok?",
        answers = listOf(
            Answer(1, "2024"),
            Answer(2, "1999"),
            Answer(3, "2034"),
            Answer(4, "1364")
        )
    ),
    Question(
        questionId = 4,
        questionText = "Co się stanie z częstotliwością fali, gdy długość fali się zmniejszy?",
        answers = listOf(
            Answer(1, "Częstotliwość wzrośnie"),
            Answer(2, "Częstotliwość zmaleje"),
            Answer(3, "Nie zmieni się"),
            Answer(4, "Fala zaniknie")
        )
    ),
    Question(
        questionId = 5,
        questionText = "Jaką jednostkę ma ładunek elektryczny?",
        answers = listOf(
            Answer(1, "C"),
            Answer(2, "A"),
            Answer(3, "J"),
            Answer(4, "W")
        )
    ),
    Question(
        questionId = 6,
        questionText = "Który z materiałów najlepiej przewodzi ciepło?",
        answers = listOf(
            Answer(1, "Aluminium"),
            Answer(2, "Szkło"),
            Answer(3, "Plastik"),
            Answer(4, "Drewno")
        )
    ),
    Question(
        questionId = 7,
        questionText = "Co jest podstawową przyczyną ruchu planet wokół Słońca?",
        answers = listOf(
            Answer(1, "Siła grawitacji"),
            Answer(2, "Tarcie"),
            Answer(3, "Ciśnienie"),
            Answer(4, "Siła magnetyczna")
        )
    ),
    Question(
        questionId = 8,
        questionText = "Jaka jest prędkość światła w próżni?",
        answers = listOf(
            Answer(1, "300 000 km/s"),
            Answer(2, "100 000 km/s"),
            Answer(3, "150 000 km/s"),
            Answer(4, "500 000 km/s")
        )
    ),
    Question(
        questionId = 9,
        questionText = "Co się dzieje z ciałem o masie 2 kg, jeśli działa na nie siła 10 N?",
        answers = listOf(
            Answer(1, "Przyspiesza o 5 m/s²"),
            Answer(2, "Przyspiesza o 10 m/s²"),
            Answer(3, "Nic się nie dzieje"),
            Answer(4, "Przyspiesza o 2 m/s²")
        )
    ),
    Question(
        questionId = 10,
        questionText = "Gdzie znajduje się Uniwersytet Wrocławski?",
        answers = listOf(
            Answer(1, "Wrocław"),
            Answer(2, "Kraków"),
            Answer(3, "Warszawa"),
            Answer(4, "Poznań")
        )
    )
)

private var numQuestion = 0
var randQuest = questions.shuffled()
var randAnswer = randQuest[numQuestion].answers.shuffled()

class MainActivity : AppCompatActivity() {

    private val showQuestion: TextView by lazy{findViewById(R.id.qcontent)}
    private val showQuestionCount: TextView by lazy{findViewById(R.id.pytanie)}

    private val r1: RadioButton by lazy{findViewById(R.id.radioButton1)}
    private val r2: RadioButton by lazy{findViewById(R.id.radioButton2)}
    private val r3: RadioButton by lazy{findViewById(R.id.radioButton3)}
    private val r4: RadioButton by lazy{findViewById(R.id.radioButton4)}

    private val submitButton: Button by lazy{findViewById(R.id.submit)}
    private val radioGroup: RadioGroup by lazy{findViewById((R.id.rg))}
    private val viewScore: TextView by lazy{findViewById(R.id.score)}
    private val proBar: ProgressBar by lazy{findViewById(R.id.progressBar)}

    private var score = 0


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("nQ", numQuestion)
        outState.putInt("nS", score)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            numQuestion = savedInstanceState.getInt("nQ")
            score = savedInstanceState.getInt("nS")
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var showQuestionCountText = "Pytanie ${numQuestion+1}/10"
        proBar.progress = 10

        fun set() {
            r1.text = randAnswer[0].answerText
            r1.tag = randAnswer[0].answerId
            r2.text = randAnswer[1].answerText
            r2.tag = randAnswer[1].answerId
            r3.text = randAnswer[2].answerText
            r3.tag = randAnswer[2].answerId
            r4.text = randAnswer[3].answerText
            r4.tag = randAnswer[3].answerId
        }

        set()

        showQuestion.text = randQuest[numQuestion].questionText
        showQuestionCount.text = showQuestionCountText

        submitButton.setOnClickListener {
            if (radioGroup.checkedRadioButtonId != -1){
                val selectedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                val selectedId = selectedRadioButton.tag as Int

                if (selectedId == 1) {
                    score++
                }

                if (numQuestion < 9) {
                    numQuestion++
                    proBar.progress +=10

                    showQuestion.text = randQuest[numQuestion].questionText
                    showQuestionCountText = "Pytanie ${numQuestion + 1}/10"
                    showQuestionCount.text = showQuestionCountText

                    randAnswer = randQuest[numQuestion].answers.shuffled()
                    set()

                    radioGroup.clearCheck()
                } else {
                    viewScore.text = "Wynik: ${score*10} punktów."
                    viewScore.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        numQuestion = 0
                        score = 0
                        randQuest = questions.shuffled()
                        randAnswer = randQuest[numQuestion].answers.shuffled()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                }
            } else {
                Toast.makeText(this, "Wybierz odpowiedź!", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}