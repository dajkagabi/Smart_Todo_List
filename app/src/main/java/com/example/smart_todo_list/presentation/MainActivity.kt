package com.example.smart_todo_list.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.rememberSwipeToDismissBoxState
import androidx.wear.compose.material.*
import com.example.smart_todo_list.data.TodoItem
import com.example.smart_todo_list.presentation.theme.Smart_Todo_ListTheme
import com.example.smart_todo_list.presentation.todo.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}

/** Ez a függvény rajzolja ki az egész alkalmazást.
    Megfigyeli az adatbázis változásait és kezeli a bevitelt.
 */
@Composable
fun TodoApp(viewModel: TodoViewModel = viewModel()) {
    /** CollectAsStateWithLifecycle:
     * Figyeli az adatbázist, és ha változik, akkor frissíti a listát. */

    val todoItems by viewModel.todoItems.collectAsStateWithLifecycle()
    
    /** remember + mutableStateOf:
     *  Olyan változók, amiknek az értéke megmarad a képernyő frissítésekor is.
     */

    // Ide kerül az új teendő szövege
    var newTaskText by remember { mutableStateOf("") }
    // Itt tároljuk a kiválasztott kategóriát
    var selectedCategory by remember { mutableStateOf("Altalanos") }
    // Épp szerkesztünk-e egy elemet?
    var editingItemId by remember { mutableStateOf<Int?>(null) }


    Smart_Todo_ListTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            val listState = rememberScalingLazyListState()

            // ScalingLazyColumn: Speciális lista az órák kerek kijelzőjéhez.
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                // Felülről kezdődik a lista
                anchorType = ScalingLazyListAnchorType.ItemStart
            ) {
                // Fejléc
                item {
                    ListHeader { Text("Okos Teendok") }
                }

                // Beviteli rész : Új teendő beírása és kategória választás
                item {
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        // Szövegbeviteli mező és Mentés gomb
                        TodoInputRow(
                            value = newTaskText,
                            onValueChange = { newTaskText = it },
                            onSave = {
                                if (newTaskText.isNotBlank()) {
                                    // Meghívjuk a ViewModel mentés funkcióját a választott kategóriával
                                    viewModel.addTodo(newTaskText, selectedCategory)
                                    newTaskText = "" // Kiürítjük a mezőt a mentés után
                                }
                            }
                        )
                        
                        // Kategória választó pl. munka, otthon, bolt, stb.
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("Otthon", "Munka", "Bolt").forEach { cat ->
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        // Ha ez a kategória van kiválasztva, más színt kap
                                        .background(if (selectedCategory == cat) MaterialTheme.colors.secondary else Color.DarkGray)
                                        .clickable { selectedCategory = cat }
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(cat, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }

                // Lista elemek: Az adatbázisból érkező teendők megjelenítése
                items(todoItems, key = { it.id }) { item ->
                    val swipeState = rememberSwipeToDismissBoxState()

                    // SwipeToDismissBox: Elhúzással való törlés funkció
                    SwipeToDismissBox(
                        state = swipeState,
                        // Törlés az adatbázisból elhúzáskor
                        onDismissed = { viewModel.deleteTodo(item) }
                    ) { isBackground: Boolean ->
                        if (!isBackground) {
                            if (editingItemId == item.id) {
                                // Szerkesztés mód: Itt átírhatjuk a teendő nevét (dupla katt)
                                var editText by remember { mutableStateOf(item.title) }
                                TodoInputRow(
                                    value = editText,
                                    onValueChange = { editText = it },
                                    onSave = {
                                        if (editText.isNotBlank()) {
                                            // Frissítjük a meglévő elemet a ViewModelben
                                            viewModel.toggleTodo(item.copy(title = editText))
                                        }
                                        editingItemId = null // Bezárjuk a szerkesztőt
                                    }
                                )
                            } else {
                                // Megjelenés: A teendő adatai (név, kategória, időpont)
                                SplitToggleChip(
                                    checked = item.isDone,
                                    onCheckedChange = { viewModel.toggleTodo(item.copy(isDone = it)) },
                                    onClick = { editingItemId = item.id }, // Kattintásra indul a szerkesztés
                                    label = { Text(item.title, maxLines = 1) },
                                    secondaryLabel = {
                                        // Idő megjelenítése: Átalakítjuk a számot olvasható HH:mm formátumra
                                        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(item.timestamp))
                                        Text("${item.category} - $timeStr", fontSize = 10.sp)
                                    },
                                    toggleControl = {
                                        Checkbox(checked = item.isDone)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Egyedi komponens, ami egy szövegmezőt és egy felirat alapú gombot tartalmaz.
 */
@Composable
fun TodoInputRow(
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Alapvető szövegbeviteli mező
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
                .padding(8.dp),
            textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSave() }),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text("Irj ide...", style = MaterialTheme.typography.body2, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        // Sima feliratos gomb szimbólum nélkül
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .clickable { onSave() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("OK", color = Color.White, fontSize = 12.sp)
        }
    }
}
