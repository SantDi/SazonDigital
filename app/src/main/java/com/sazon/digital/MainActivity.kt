@file:OptIn(ExperimentalMaterial3Api::class)
package com.sazon.digital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sazon.digital.data.Recipe
import com.sazon.digital.ui.VideoPlayer
import com.sazon.digital.ui.WebPane
import com.sazon.digital.ui.store.StoreViewModel
import com.sazon.digital.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

enum class Pane { Perfil, Galeria, Video, Web, Tienda }

@Composable
fun AppScaffold() {
    var title by rememberSaveable { mutableStateOf("Perfil") }
    var selected by rememberSaveable { mutableStateOf(Pane.Perfil) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sazón Digital · $title") },
            )
        }
    ) { innerPadding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Surface(
                tonalElevation = 3.dp,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Sazón",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Divider()
                    DrawerItems(selected = selected) { sel ->
                        selected = sel
                        title = when (sel) {
                            Pane.Perfil -> "Perfil"
                            Pane.Galeria -> "Galería"
                            Pane.Video -> "Video"
                            Pane.Web -> "Web"
                            Pane.Tienda -> "Tienda"
                        }
                    }
                }
            }


            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    Modifier
                        .widthIn(max = 900.dp)
                        .fillMaxWidth()
                ) {
                    TwoPaneScreen(
                        selected = selected,
                        onSelect = { sel ->
                            selected = sel
                            title = when (sel) {
                                Pane.Perfil -> "Perfil"
                                Pane.Galeria -> "Galería"
                                Pane.Video -> "Video"
                                Pane.Web -> "Web"
                                Pane.Tienda -> "Tienda"
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun DrawerItems(selected: Pane, onSelect: (Pane) -> Unit) {
    val vm: StoreViewModel = hiltViewModel()
    val cart by vm.cart.collectAsState()
    val count = cart.size

    // Lista de secciones del menú
    val entries = listOf(
        Triple(Pane.Perfil, R.drawable.ic_lucide_square_user, "Perfil"),
        Triple(Pane.Galeria, R.drawable.ic_lucide_images, "Galería"),
        Triple(Pane.Video, R.drawable.ic_simple_youtube, "Video"),
        Triple(Pane.Web, R.drawable.ic_lucide_globe, "Web"),
        Triple(Pane.Tienda, R.drawable.ic_lucide_shopping_bag, "Tienda"),
    )

    // Menú vertical con íconos y texto debajo
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(120.dp) // 👈 ancho compacto del menú
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sazón Digital",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Ítems del menú
        entries.forEach { (pane, icon, label) ->
            val isSelected = selected == pane
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelect(pane) }
                    .background(
                        if (isSelected)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.surface
                    )
                    .padding(vertical = 10.dp, horizontal = 6.dp)
            ) {
                if (pane == Pane.Tienda && count > 0) {
                    BadgedBox(badge = { Badge { Text(count.toString()) } }) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = label,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = label,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = label,
                    fontSize = 11.sp, // 👈 texto más pequeño
                    lineHeight = 12.sp,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(Modifier.fillMaxSize()) {
                    AppScaffold()
                }
            }
        }
    }
}

@Composable
fun TwoPaneScreen(modifier: Modifier = Modifier, selected: Pane, onSelect: (Pane) -> Unit) {
    Box(modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = selected,
            transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(220)) },
            label = "content-anim"
        ) { state ->
            when (state) {
                Pane.Perfil -> PerfilPane()
                Pane.Galeria -> GaleriaPane()
                Pane.Video -> VideoPane()
                Pane.Web -> WebPane()
                Pane.Tienda -> TiendaPane()
            }
        }
    }
}

@Composable
fun PerfilPane() {ElevatedCard(
    shape = RoundedCornerShape(24.dp),
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Row(Modifier.padding(20.dp)) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1568605114967-8130f3a36994",
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )
        //  identificador del perfil de la app
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text("Alex Chef", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Food Dev · 8 años de experiencia", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(12.dp))


            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(onClick = {}, label = { Text("Pastas",  maxLines = 1, overflow = TextOverflow.Ellipsis) })
                AssistChip(onClick = {}, label = { Text("Postres", maxLines = 1, overflow = TextOverflow.Ellipsis) })

            }
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AssistChip(onClick = {}, label = { Text("Parrilla",maxLines = 1, overflow = TextOverflow.Ellipsis) })
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = { /* TODO */ }, shape = RoundedCornerShape(24.dp)) {
                Text("Ver recetas")
            }
        }
    }
} }

@Composable
fun GaleriaPane() { var query by rememberSaveable { mutableStateOf("") }
    var dialogTitle by remember { mutableStateOf<String?>(null) }

    // propiedades de las comidas en la galeria

    val all = listOf(
        Recipe(title = "Pasta al pesto", imageUrl = "https://images.unsplash.com/photo-1521389508051-d7ffb5dc8bbf", description = "Fideos con pesto de albahaca."),
        Recipe(title = "Hamburguesa", imageUrl = "https://images.unsplash.com/photo-1550547660-d9450f859349", description = "Clásica doble con queso."),
        Recipe(title = "Brownie", imageUrl = "https://images.unsplash.com/photo-1601972599720-b1e4e02c45a9", description = "Brownie húmedo de chocolate."),
        Recipe(title = "Pizza", imageUrl = "https://images.unsplash.com/photo-1542281286-9e0a16bb7366", description = "Margarita al horno de leña."),
        Recipe(title = "Sushi", imageUrl = "https://images.unsplash.com/photo-1548946526-f69e2424cf45", description = "Mix nigiri y maki."),
        Recipe(title = "Tacos", imageUrl = "https://images.unsplash.com/photo-1604467794349-0b74285c2f17", description = "Tacos al pastor con piña.")
    )
    val demo = remember(query) {
        if (query.isBlank()) all else all.filter { it.title.contains(query, ignoreCase = true) }
    }
    //  identificador de las comidas en galeria

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Buscar por nombre/ingredientes") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(demo) { r ->
                ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.clickable { dialogTitle = r.title + " — " + r.description }) {
                    AsyncImage(
                        model = r.imageUrl,
                        contentDescription = r.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(Modifier.padding(12.dp)) {
                        Text(r.title, style = MaterialTheme.typography.titleMedium)
                        Text("Tocar para ver descripción", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    if (dialogTitle != null) {
        AlertDialog(
            onDismissRequest = { dialogTitle = null },
            confirmButton = { TextButton(onClick = { dialogTitle = null }) { Text("Cerrar") } },
            title = { Text("Detalle") },
            text = { Text(dialogTitle ?: "") }
            // vinculo de detalles de las comidas en galeria
        )
    }}

@Composable
fun VideoPane() { Column(Modifier.fillMaxSize()) {
    Text("Video de ejemplo (Media3)", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    VideoPlayer(
        url = "https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4",
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
    )
    // identificador de la seccion de video
}
}

@Composable
fun TiendaPane(vm: StoreViewModel = hiltViewModel()) { val products by vm.products.collectAsState()
    val cart by vm.cart.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    var detailId by rememberSaveable { mutableStateOf<Long?>(null) }
    val detail = products.firstOrNull { it.id == detailId }

    val snackbar = remember { SnackbarHostState() }
    var showCart by rememberSaveable { mutableStateOf(false) }

    //  propiedades del carrito

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; vm.setQuery(it) },
                placeholder = { Text("Buscar en tienda") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            // Botón para abrir carrito
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Productos", style = MaterialTheme.typography.titleLarge)
                FilledTonalButton(onClick = { showCart = true }) {
                    Text("Ver carrito (${cart.size})")
                }
                // identificador del carrito
            }

            Spacer(Modifier.height(8.dp))

            // Grilla de productos
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { p ->
                    val inCart = cart.any { it.productId == p.id }
                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.clickable { detailId = p.id }
                    ) {
                        AsyncImage(
                            model = p.imageUrl,
                            contentDescription = p.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentScale = ContentScale.Crop
                        )


                        Column(Modifier.padding(12.dp)) {
                            Text(p.name, style = MaterialTheme.typography.titleMedium)
                            Text("$ " + (p.price / 100) + "." + (p.price % 100).toString().padStart(2,'0'),
                                style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { vm.toggleCart(p.id) }, shape = RoundedCornerShape(24.dp)) {
                                Text(if (inCart) "Quitar" else "Agregar")
                            }
                        }  //  identificador de la tienda

                    }
                }
            }
        }

        // Host para snackbars (checkout simulado)
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
    }

    // Detalle de producto (diálogo)
    if (detail != null) {
        AlertDialog(
            onDismissRequest = { detailId = null },
            confirmButton = {
                val inCart = cart.any { it.productId == detail!!.id }
                TextButton(onClick = { vm.toggleCart(detail!!.id); detailId = null }) {
                    Text(if (inCart) "Quitar del carrito" else "Agregar al carrito")
                }
            },
            dismissButton = { TextButton(onClick = { detailId = null }) { Text("Cerrar") } },
            title = { Text(detail!!.name) },
            text = {
                Column {
                    AsyncImage(
                        model = detail!!.imageUrl,
                        contentDescription = detail!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(detail!!.description)
                }
            }
        )
    }

    // Pantalla de Carrito (full-screen dialog)
    if (showCart) {
        AlertDialog(
            onDismissRequest = { showCart = false },
            confirmButton = {
                TextButton(onClick = {
                    // "Checkout" mock: vaciar carrito
                    // Como el repo expone toggle, removemos cada item presente
                    cart.forEach { vm.toggleCart(it.productId) }
                    showCart = false
                }) { Text("Finalizar compra") }
            },
            dismissButton = { TextButton(onClick = { showCart = false }) { Text("Cerrar") } },
            title = { Text("Carrito") },
            text = {
                val detailed = products.filter { p -> cart.any { it.productId == p.id } }
                val total = detailed.sumOf { it.price }
                Column {
                    if (detailed.isEmpty()) {
                        Text("Tu carrito está vacío.")
                    } else {
                        detailed.forEach { p ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(p.name, style = MaterialTheme.typography.titleMedium)
                                    Text("$ " + (p.price / 100) + "." + (p.price % 100).toString().padStart(2,'0'),
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                                TextButton(onClick = { vm.toggleCart(p.id) }) { Text("Quitar") }
                            }
                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Total: $ " + (total / 100) + "." + (total % 100).toString().padStart(2, '0'),
                            style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        )
    }}
