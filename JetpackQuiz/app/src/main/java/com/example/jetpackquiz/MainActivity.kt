package com.example.jetpackquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.jetpackquiz.ui.theme.JetpackQuizTheme

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

class MainActivity : ComponentActivity() {
    private var score = 0
    private var selectedAnswer = -1

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("nQ", numQuestion)
        outState.putInt("nS", score)
        outState.putInt("sA", selectedAnswer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            numQuestion = savedInstanceState.getInt("nQ")
            score = savedInstanceState.getInt("nS")
            selectedAnswer = savedInstanceState.getInt("sA")
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackQuizTheme {
                val numQuestion = remember { mutableIntStateOf(0) }
                val score = remember { mutableIntStateOf(0) }
                val randQuest = remember { questions.shuffled() }
                val randAnswer = remember { mutableStateOf(randQuest[0].answers.shuffled()) }

                Quiz(
                    nr = numQuestion.intValue + 1,
                    sA = -1,
                    onAnswerSelected = { index -> selectedAnswer = index },
                    randAnswer = randAnswer.value,
                    randQuest = randQuest,
                    numQuestion = numQuestion.intValue,
                    increment = { correct ->
                        if (correct) score.intValue++
                        if (numQuestion.intValue < randQuest.size - 1) {
                            numQuestion.intValue++
                            randAnswer.value = randQuest[numQuestion.intValue].answers.shuffled()
                        }
                        else {
                            numQuestion.intValue = 0
                            randAnswer.value = randQuest[numQuestion.intValue].answers.shuffled()
                        }
                    },
                    score = score.intValue
                )
            }
        }
    }
}

@Composable
fun Quiz(
     nr: Int,
     sA: Int,
     onAnswerSelected: (Int) -> Unit,
     randAnswer: List<Answer>,
     randQuest: List<Question>,
     numQuestion: Int,
     increment: (Boolean) -> Unit,
     score: Int
) {
    val proBar = remember { mutableFloatStateOf(0.0f) }
    val selA = remember { mutableIntStateOf(sA) }
    val showDialog = remember { mutableStateOf(false) }
    proBar.floatValue = (nr.toFloat() / questions.size.toFloat())

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Pytanie $nr",
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 50.dp)
        )
        LinearProgressIndicator(
            progress = { proBar.floatValue },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 50.dp)
        )
        Text(
            text = randQuest[numQuestion].questionText,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .background(color = Color.LightGray)
                .padding(20.dp),
        )
        randAnswer.fastForEachIndexed { index, answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                RadioButton(
                    selected = selA.intValue == index,
                    onClick = {
                        selA.intValue = index
                        onAnswerSelected(index) },
                    )
                Text(answer.answerText)
            }
        }
        Spacer(
            modifier = Modifier.weight(0.2f)
        )
        Button(
            onClick = {
                if (selA.intValue != -1) {
                    val correct = randAnswer[selA.intValue].answerId == 1
                    increment(correct)
                    selA.intValue = -1
                    if (numQuestion == questions.size-1) {
                        showDialog.value = true
                    }
                }},
            modifier = Modifier.fillMaxWidth().padding(20.dp).padding(bottom = 20.dp)
        ) {
          Text("Dalej")
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {  },
                confirmButton = {
                    Button(onClick = {
                        showDialog.value = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Anuluj")
                    }
                },
                title = { Text("Koniec Quizu!") },
                text = { Text("Gratulacje, ukończyłeś quiz! Twój wynik: $score/${questions.size}") }
            )
        }

    }
}
/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackQuizTheme {
        Quiz(
            nr = 1,
            sA = 1,
            onAnswerSelected = { }

        )
    }
}*/