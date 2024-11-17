package com.example.listyzadan

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.listyzadan.databinding.ActivityMainBinding
import kotlin.random.Random


data class Exercise (
    val content: String,
    val points: Int,
)

data class Subject (
    val name: String,
)

data class ExerciseList (
    val exercises: MutableList<Exercise>,
    val subject: Subject,
    val grade: Float,
)

val listyZadan = mutableListOf<ExerciseList>()

val Subjects = mutableListOf(
    Subject("Matematyka"),
    Subject("PUM"),
    Subject("Fizyka"),
    Subject("Elektronika"),
    Subject("Algorytmy")
)

class MainActivity : AppCompatActivity() {
    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        navHostFragment.findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavView.setupWithNavController(navController)
        for (i in 1..20) {
            val thyme = MutableList(Random.nextInt(1, 10)) {
                Exercise(
                    content = "Lorem Ipsum",
                    points = Random.nextInt(1, 10)
                )
            }
            listyZadan.add(
                ExerciseList(
                    exercises = thyme,
                    subject = Subjects[Random.nextInt(0, Subjects.size)],
                    grade = (Random.nextInt(6, 10)).toFloat()/2
                )
            )
        }
    }
}