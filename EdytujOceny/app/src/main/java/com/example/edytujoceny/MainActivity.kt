package com.example.edytujoceny

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.edytujoceny.ui.theme.EdytujOcenyTheme

@Entity(tableName = "grades")
data class Grade(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val value: Double,
)

sealed class Screens(val route: String) {
    data object MojeOceny : Screens("MojeOceny")
    data object Edytuj : Screens("Edytuj")
    data object DodajNowy : Screens("DodajNowy")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EdytujOcenyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding -> Navigation(Modifier.padding(innerPadding)) }
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
        content = { NavGraph(navController = navController, modifier = modifier) }
    )
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screens.MojeOceny.route,
        modifier = modifier
    ) {
        composable(route = Screens.MojeOceny.route) { MojeOceny(navController, modifier) }
        composable(route = Screens.Edytuj.route) { Edytuj(navController, modifier) }
        composable(route = Screens.DodajNowy.route) { DodajNowy(navController, modifier) }
    }
}

@Composable
fun MojeOceny(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = { Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Moje oceny", fontSize = 24.sp)
        }},
        content = { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding)
            ) {

            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Średnia ocen: ", fontSize = 20.sp)
                    Text("5.0", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Button(onClick = {navController.navigate(Screens.DodajNowy.route)}, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
                    Text("Nowy wpis", fontSize = 20.sp)
                }
            }
        },
    )
}

@Composable
fun Edytuj(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = { Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Edytuj ocenę", fontSize = 24.sp)
        }},
        content = { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding)
            ) {

            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp)
            ) {
                Button(onClick = { navController.navigate(Screens.MojeOceny.route) }, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
                    Text("Usuń wpis", fontSize = 20.sp)
                }
            }
        },
    )
}

@Composable
fun DodajNowy(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = { Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Dodaj nową ocenę", fontSize = 24.sp)
        }},
        content = { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding)
            ) {

            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp)
            ) {
                Button(onClick = { navController.navigate(Screens.MojeOceny.route) }, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
                    Text("Dodaj ocenę", fontSize = 20.sp)
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EdytujOcenyTheme {
        MojeOceny(navController = rememberNavController(), modifier = Modifier.padding(10.dp))
    }
}