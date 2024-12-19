package com.example.listastudentow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listastudentow.DataProvider.students
import com.example.listastudentow.ui.theme.ListaStudentowTheme
import kotlin.random.Random

data class Student (
    val nr: Int,
    val firstname: String,
    val lastname: String,
    val avg: Double,
    val year: Int
)

object DataProvider {

    private val firstNames = listOf(
        "Adam", "Ewa", "Jan", "Anna", "Piotr", "Maria", "Tomasz", "Małgorzata", "Krzysztof", "Alicja",
        "Andrzej", "Joanna", "Michał", "Barbara", "Kamil", "Magdalena", "Robert", "Monika", "Mateusz", "Natalia"
    )

    private val lastNames = listOf(
        "Nowak", "Kowalski", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamiński", "Lewandowski", "Zieliński", "Szymański",
        "Woźniak", "Dąbrowski", "Kozłowski", "Jankowski", "Mazur", "Kwiatkowski", "Krawczyk", "Piotrowski", "Grabowski",
        "Nowakowski", "Pawłowski"
    )

    val students = (0..40).map {
        Student(
            nr = Random.nextInt(1000, 9999),
            firstname = firstNames.random(),
            lastname = lastNames.random(),
            avg = Random.nextInt(6, 11).toDouble()/2,
            year = Random.nextInt(1, 4)
        )
    }
}

class StudentViewModel : ViewModel() {
    private var _students = mutableListOf<Student>()
    val studList: List<Student>
        get() = _students

    init {
        reinitialize()
    }

    private fun reinitialize() {
        _students.clear()
        _students.addAll(students)
        _students.sortBy { it.nr }
    }
}

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ListaStudentowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavGraph(navController = navController, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

sealed class Screens(val route: String) {
    data object Master : Screens("Master")
    data object Detail : Screens("Detail")
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = Screens.Master.route,
        modifier = modifier
    ) {
        composable(Screens.Master.route) {
            Master(navController = navController)
        }
        composable(
            route = "${Screens.Detail.route}/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val viewModel: StudentViewModel = viewModel()
            val student = viewModel.studList[index]
            Detail(student)
        }
    }
}

@Composable
fun Master(navController: NavHostController) {
    val viewModel: StudentViewModel = viewModel()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Lista Studentów", modifier = Modifier.fillMaxWidth().padding(50.dp, 40.dp), fontSize = 30.sp, textAlign = TextAlign.Center)
        LazyColumn(
            modifier = Modifier.padding(bottom = 30.dp)
        ) {
            items(viewModel.studList.size) {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                        .background(color = Color.hsl(220F, 0.7F, 0.7F))
                        .clickable { navController.navigate("${Screens.Detail.route}/$it") }
                        .padding(30.dp),
                ) {
                    Row {
                        Text(viewModel.studList[it].nr.toString(), fontSize = 25.sp)
                    }
                    Row{
                        Text(viewModel.studList[it].firstname + " ")
                        Text(viewModel.studList[it].lastname)
                    }
                }
            }
        }
    }
}

@Composable
fun Detail(stu: Student) {
    Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
    ) {
        Column( modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth()
            .background(color = Color.hsl(220F, 0.7F, 0.7F)).padding(10.dp)
            ) {
            Row {
                Text(stu.nr.toString(), fontSize = 25.sp)
            }
            Row{
                Text(stu.firstname + " ", fontSize = 20.sp)
                Text(stu.lastname, fontSize = 20.sp)
            }
        }
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row {
                Text("Ocena: " + stu.avg)
            }
            Row {
                Text("Rok studiów: " + stu.year)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListaStudentowTheme {

    }
}