package com.example.grades

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

    @Query("SELECT * FROM grades WHERE id = :id")
    suspend fun getGradeById(id: Int): Grade

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
    suspend fun update(grade: Grade) = gD.update(grade)
    suspend fun getById(id: Int) = gD.getGradeById(id)
    suspend fun delete(grade: Grade) = gD.delete(grade)
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

    private val _grade = MutableLiveData<Grade>()
    val grade: LiveData<Grade> get() = _grade

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

    fun delete(grade: Grade) {
        viewModelScope.launch {
            repository.delete(grade)
        }
    }

    fun addGrade(grade: Grade) {
        viewModelScope.launch {
            repository.add(grade)
        }
    }

    fun getGradeById(id: Int) {
        viewModelScope.launch {
            val result = repository.getById(id)
            _grade.postValue(result)
        }
    }

    fun update(grade: Grade) {
        viewModelScope.launch {
            repository.update(grade)
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
        composable(route = Screens.Edytuj.route) { Edytuj(navController, modifier, -1) }
        composable(route = Screens.DodajNowy.route) { DodajNowy(navController, modifier) }
        composable(
            route = "edytuj/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            Edytuj(navController, modifier, id ?: -1)
        }
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

    val avg = grades.map { it.value }.average()

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
                                .clickable {
                                    navController.navigate("edytuj/${grades[it].id}")
                                }
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
                    if (avg.isNaN()) {
                        Text("Brak ocen", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    else { Text(String.format("%.2f", avg), fontSize = 20.sp, fontWeight = FontWeight.Bold) }

                }
                Button(onClick = {navController.navigate(Screens.DodajNowy.route)}, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
                    Text("Nowy wpis", fontSize = 20.sp)
                }
            }
        },
    )
}

@Composable
fun Edytuj(navController: NavHostController, modifier: Modifier = Modifier, id: Int) {
    val viewModel: GradeViewModel = viewModel(
        LocalViewModelStoreOwner.current!!,
        "GradeViewModel",
        GradeViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    LaunchedEffect(id) {
        viewModel.getGradeById(id)
    }

    val grade = viewModel.grade.value

    if (grade == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
        return
    }

    var textInput by remember { mutableStateOf(grade?.name.toString()) }
    var numberInput by remember { mutableStateOf(grade?.value.toString()) }

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
                modifier = Modifier.fillMaxWidth().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Nazwa przedmiotu") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = numberInput,
                    onValueChange = { input ->
                        val regex = """^([1-4](\.\d{0,1})?|5(\.0?)?)$""".toRegex()
                        if (input.isEmpty() || regex.matches(input)) {
                            numberInput = input
                        }
                    },
                    label = { Text("Ocena") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp)
            ) {
                Button(onClick = {
                    if (textInput.isEmpty() || numberInput.isEmpty()) {
                        return@Button
                    }
                    else {
                        val upd = Grade(grade.id, textInput, numberInput.toDouble())
                        viewModel.update(upd)
                        navController.navigate(Screens.MojeOceny.route)
                    }
                    }, modifier = Modifier.fillMaxWidth().height(70.dp).padding(bottom = 10.dp) ) {
                    Text("Edytuj wpis", fontSize = 20.sp)
                }
                Button(onClick = {
                    viewModel.delete(grade)
                    navController.navigate(Screens.MojeOceny.route) }, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
                    Text("Usuń wpis", fontSize = 20.sp)
                }
            }
        },
    )
}

@Composable
fun DodajNowy(navController: NavHostController, modifier: Modifier = Modifier) {
    var textInput by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("") }

    val viewModel: GradeViewModel = viewModel(
        LocalViewModelStoreOwner.current!!,
        "GradeViewModel",
        GradeViewModelFactory(LocalContext.current.applicationContext as Application)
    )

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
                modifier = Modifier.fillMaxWidth().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Nazwa przedmiotu") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = numberInput,
                    onValueChange = { input ->
                        val regex = """^([1-4](\.\d{0,1})?|5(\.0?)?)$""".toRegex()
                        if (input.isEmpty() || regex.matches(input)) {
                            numberInput = input
                        }
                    },
                    label = { Text("Ocena") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp)
            ) {
                Button(onClick = {
                    if (textInput.isEmpty() || numberInput.isEmpty()) {
                        return@Button
                    }
                    else {
                        val grade = Grade(0, textInput, numberInput.toDouble())
                        viewModel.addGrade(grade)
                        navController.navigate(Screens.MojeOceny.route)
                    }
                    }, modifier = Modifier.fillMaxWidth().height(70.dp).padding(bottom = 10.dp) ) {
                    Text("Dodaj ocenę", fontSize = 20.sp)
                }
                Button(onClick = {
                    navController.navigate(Screens.MojeOceny.route)
                    }, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
                    Text("Anuluj", fontSize = 20.sp)
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