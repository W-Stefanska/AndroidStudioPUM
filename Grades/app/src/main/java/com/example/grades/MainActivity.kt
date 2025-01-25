package com.example.grades

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.grades.ui.theme.GradesTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Entity(tableName = "grades")
data class Grade(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val value: Double,
)

@Dao
interface GradeDao {
    @Query("SELECT * FROM grades ORDER BY id ASC, name ASC")
    fun getGrades(): Flow<List<Grade>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(grade: Grade)

    @Query("SELECT * FROM grades WHERE name = :name")
    fun getGradeByName(name: String): List<Grade>

    @Query("DELETE FROM grades")
    suspend fun deleteAll()

    @Update
    suspend fun update(grade: Grade)

    @Delete
    suspend fun delete(grade: Grade)
}

@Database(entities = [Grade::class], version = 1, exportSchema = false)
abstract class GradeDatabase : RoomDatabase() {
    abstract fun gradeDao(): GradeDao

    companion object {
        @Volatile
        private var Instance: GradeDatabase? = null

        fun getDatabase(context: Context): GradeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GradeDatabase::class.java, "grade_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

class GradesRepository(private val gD: GradeDao) {
    fun getGrades() = gD.getGrades()
    suspend fun clear() = gD.deleteAll()
    suspend fun add(grade: Grade) = gD.insert(grade)
}

class GradeViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GradeViewModel(application) as T
    }
}

class GradeViewModel(application: Application) : ViewModel() {

    private val repository: GradesRepository
    private val _gradesState = MutableStateFlow<List<Grade>>(emptyList())
    val gradesState: StateFlow<List<Grade>>
        get() = _gradesState

    init {
        val db = GradeDatabase.getDatabase(application)
        val dao = db.gradeDao()
        repository = GradesRepository(dao)

        fetchGrades()
    }

    private fun fetchGrades() {
        viewModelScope.launch {
            repository.getGrades().collect { grades ->
                _gradesState.value = grades
            }
        }
    }

    fun clearGrade() {
        viewModelScope.launch {
            repository.clear()
        }
    }

    fun addGrade(grade: Grade) {
        viewModelScope.launch {
            repository.add(grade)
        }
    }
}

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
            GradesTheme {
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
    val viewModel: GradeViewModel = viewModel(
        LocalViewModelStoreOwner.current!!,
        "GradeViewModel",
        GradeViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val grades by viewModel.gradesState.collectAsStateWithLifecycle()

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
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(0.7f)
                ) {
                    items(grades.size) {
                        Text(
                            text = "${grades[it].name} ${grades[it].value}",
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                        )
                    }
                }
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
    GradesTheme {
        MojeOceny(navController = rememberNavController(), modifier = Modifier.padding(10.dp))
    }
}