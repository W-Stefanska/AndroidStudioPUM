package com.example.jetpacklisty

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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpacklisty.ui.theme.JetpackListyTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import kotlin.random.Random

data class Exercise (
    val content: String,
    val points: Int,
)

data class Subject (
    val name: String,
)

data class Summary (
    val subject: Subject,
    val average: Double,
    val appearances: Int
)

data class ExerciseList (
    val exercises: MutableList<Exercise>,
    val subject: Subject,
    val grade: Float,
)

val listyZadan = mutableListOf<ExerciseList>()
val rosemary = mutableListOf<Summary>()

val Subjects = mutableListOf(
    Subject("Matematyka"),
    Subject("PUM"),
    Subject("Fizyka"),
    Subject("Elektronika"),
    Subject("Algorytmy")
)

fun generatorZadan() {
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
                grade = (Random.nextInt(6, 11)).toFloat()/2
            )
        )
    }
}

fun podsumujListy(): MutableList<Summary> {
    for (i in 0..4) {
        var sum = 0.0
        var counter = 0
        for (j in 0..19) {
            if (listyZadan[j].subject.name == Subjects[i].name) {
                sum += listyZadan[j].grade
                counter++
            }
        }
        val average = if (counter > 0) sum / counter else 0.0
        rosemary.add(Summary(Subjects[i], average, counter))
    }
    rosemary.removeAll { it.appearances == 0 }
    return rosemary
}

sealed class Screens(val route: String) {
    data object E1 : Screens("E1")
    data object E2 : Screens("E2")
    data object E3 : Screens("E3")
}

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: Int,
) {
    data object E1 : BottomBar(Screens.E1.route, "Listy zadań", R.drawable.lista)
    data object E2 : BottomBar(Screens.E2.route, "Oceny", R.drawable.ocena)
}

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generatorZadan()
        podsumujListy()

        enableEdgeToEdge()
        setContent {
            JetpackListyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Navigation(modifier = Modifier.padding(innerPadding))
                    }
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomMenu(navController = navController)},
        content = { NavGraph(navController = navController, modifier = modifier) }
    )
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screens.E1.route,
        modifier = modifier
    ) {
        composable(route = Screens.E1.route) { E1(navController) }
        composable(route = Screens.E2.route) { E2() }
        composable(
                route = "${Screens.E3.route}/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments!!.getInt("index")
            val exerciseList = listyZadan.getOrNull(index ?: -1)
            if (exerciseList != null) {
                E3(index)
            } else {
                Text("Nie znaleziono listy zadań.")
            }
        }
    }
}

@Composable
fun BottomMenu(navController: NavHostController) {
    val screens = listOf(
        BottomBar.E1, BottomBar.E2
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar{
        screens.forEach{screen ->
            NavigationBarItem(
                label = { Text(text = screen.title)},
                icon = {Icon(imageVector = getImageVector(screen.icon), contentDescription = "icon")},
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {navController.navigate(screen.route)}
            )
        }
    }
}

@Composable
fun getImageVector(iconResId: Int): ImageVector {
    return ImageVector.vectorResource(id = iconResId)
}

@Composable
public fun NavHost(
    navController: NavHostController,
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit
): Unit {}

@Composable
fun E1(navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Moje Listy Zadań", modifier = Modifier.fillMaxWidth().padding(50.dp, 40.dp), fontSize = 30.sp, textAlign = TextAlign.Center)
        LazyColumn(
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            items(listyZadan.size) {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                        .background(color = Color.hsl(220F, 0.7F, 0.7F))
                        .clickable { navController.navigate("${Screens.E3.route}/$it") }
                        .padding(30.dp),
                ) {
                    var count = 0

                    fun calc() {
                        for (i in 0..(listyZadan.size-1)) {
                            if (listyZadan[it].subject == listyZadan[i].subject) {
                                count++
                                if (i == it) {break}
                            }
                        }
                    }
                    calc()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(listyZadan[it].subject.name, fontSize = 20.sp)
                        Text("Lista: " + count, fontSize = 20.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ilość zadań: " + listyZadan[it].exercises.size.toString())
                        Text("Ocena: " + listyZadan[it].grade.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun E2() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Moje Oceny", modifier = Modifier.fillMaxWidth().padding(50.dp, 40.dp), fontSize = 30.sp, textAlign = TextAlign.Center)
        LazyColumn(
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            items(rosemary.size) {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                        .background(color = Color.hsl(220F, 0.7F, 0.7F))
                        .padding(30.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(rosemary[it].subject.name, fontSize = 20.sp)
                        Text("Ocena: " + String.format("%.2f", rosemary[it].average).toDouble(), fontSize = 20.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Liczba list: " + rosemary[it].appearances)
                    }
                }
            }
        }
    }
}

@Composable
fun E3(it: Int) {
    val arg = listyZadan[it]
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp).fillMaxWidth()
    ) {
        var count = 0
        fun calc() {
            for (n in 0..(listyZadan.size - 1)) {
                if (listyZadan[n].subject == arg.subject) {
                    count++
                    if (listyZadan[n] == arg) {
                        break
                    }
                }
            }
        }

        calc()
        Text("${arg.subject.name} - Lista $count", modifier = Modifier.fillMaxWidth().padding(50.dp, 40.dp), fontSize = 30.sp, textAlign = TextAlign.Center)
        LazyColumn(
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            items(arg.exercises.size) {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                        .background(color = Color.hsl(220F, 0.7F, 0.7F))
                        .padding(30.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Zadanie ${it+1}", fontSize = 20.sp)
                        Text("Ilość punktów: ${arg.exercises[it].points}", fontSize = 20.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(arg.exercises[it].content)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    JetpackListyTheme {
        Navigation()
    }
}