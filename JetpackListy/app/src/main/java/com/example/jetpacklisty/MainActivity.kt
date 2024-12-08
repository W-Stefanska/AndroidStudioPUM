package com.example.jetpacklisty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpacklisty.ui.theme.JetpackListyTheme
import androidx.navigation.compose.NavHost
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
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomMenu(navController = navController)},
        content = { NavGraph(navController = navController) }
    )
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.E1.route,
    ) {
        composable(route = Screens.E1.route) { E1() }
        composable(route = Screens.E2.route) { E2() }
        composable(route = Screens.E3.route) { E3() }
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
fun E1() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp).fillMaxWidth()
    ) {
        Text("Moje Listy Zadań", modifier = Modifier.fillMaxWidth().padding(50.dp, 40.dp), fontSize = 30.sp, textAlign = TextAlign.Center)
        LazyColumn(
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            items(listyZadan.size) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                        .background(color = Color.LightGray)
                        .padding(30.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(listyZadan[it].subject.name)
                        Text(listyZadan[it].grade.toString())
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("a")
                        Text("b")
                    }
                }
            }
        }
    }
}

@Composable
fun E2() {
    Text(text = "E2")
}

@Composable
fun E3() {

}

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    JetpackListyTheme {
        E1()
        Navigation()
    }
}