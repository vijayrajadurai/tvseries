
package com.mobi.tvseries.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.tvseries.data.TvSeries
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mobi.tvseries.R
import com.mobi.tvseries.data.Season
import com.mobi.tvseries.data.TvSeriesEntity
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen(onTimeout = {

            })
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    TVSeriesListScreen(viewModel = viewModel()){ tvSeries ->
                        navController.navigate("details/${tvSeries.id}")
                    }
                }

                composable("details/{tvSeriesId}") { backStackEntry ->
                    val tvSeriesIdStr = backStackEntry.arguments?.getString("tvSeriesId") ?: "0"
                    val tvSeriesId = tvSeriesIdStr.toIntOrNull() ?: 0
                    val tvSeries = TvSeries(id=tvSeriesId,"","","","","",0.0,0.0,0)
                    TVSeriesDetailsScreen(viewModel = viewModel(), tvSeriesId = tvSeries)
                }
            }

        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    LaunchedEffect(true) {
        delay(5000)
        onTimeout.invoke()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TVSeriesListScreen(viewModel: TvSeriesViewModel = viewModel(),onTvSeriesSelected: (TvSeries) -> Unit) {

    val seriesList by viewModel.seriesList.observeAsState()
    val isRefreshing by viewModel.isRefreshing.observeAsState(false)
    val loadingState by viewModel.loadingState.observeAsState()


    if (loadingState == LoadingState.Loading) {
        ProgressDialog()
    }

    var searchQuery by remember { mutableStateOf("") }
    viewModel.filterSeries(searchQuery)

    Scaffold(
        topBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            ) {
                // TextField for search
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search TV Series") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    textStyle = MaterialTheme.typography.headlineSmall.copy(color = Color.Black),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    )
                )
            }
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { viewModel.refreshSeries() }
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                LazyVerticalGrid(
                    GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    itemsIndexed(items = seriesList ?: emptyList()) { index, series ->
                        TVSeriesCard(series = series, onTvSeriesSelected = onTvSeriesSelected)
                    }
                }
                Log.d("MyLogComposable", "Debug log message")
            }
        },
        modifier = Modifier.fillMaxSize()
    )

}


@Composable
fun TVSeriesCard(series: TvSeries, onTvSeriesSelected: (TvSeries) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp)
            .clickable {
                onTvSeriesSelected(series)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val painter = rememberImagePainter(data = "https://image.tmdb.org/t/p/w500${series.poster_path}")
            Image(painter = painter, contentDescription = series.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = series.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_star_rate_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = series.vote_average.toString())
            }
        }

    }
}

@Composable
fun TVSeriesDetailsScreen(viewModel: TvSeriesViewModel = viewModel() , tvSeriesId: TvSeries) {

    val seriesDetails by viewModel.seriesDetails.observeAsState()
    val loadingState by viewModel.loadingState.observeAsState()

    viewModel.fetchTvSeriesDetails(tvSeriesId.id)

    if (loadingState == LoadingState.Loading) {
        ProgressDialog()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Poster Image
            seriesDetails?.backdrop_path?.let { posterPath ->
                val painter =
                    rememberImagePainter(data = "https://image.tmdb.org/t/p/w500${posterPath}")
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = seriesDetails?.name ?: "No Name",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Overview: ${seriesDetails?.overview}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Vote Average: ${seriesDetails?.vote_average}")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Seasons:",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

            LazyVerticalGrid(
//            GridCells.Adaptive(minSize = 128.dp),
                GridCells.Fixed(2), // Set the number of columns in the grid
                contentPadding = PaddingValues(8.dp)
            ) {

                items(seriesDetails?.seasons ?: emptyList()) { season ->
                    SeriesSeasonCard(season = season)
                }
            }

        }
    }
}

@Composable
fun SeriesSeasonCard(season: Season) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val painter = rememberImagePainter(data = "https://image.tmdb.org/t/p/w500${season.posterPath}")
            Image(painter = painter, contentDescription = season.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = season.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_star_rate_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = season.voteAverage.toString())
            }
        }

    }
}


@Composable
fun ProgressDialog() {
    Dialog(
        onDismissRequest = { },
        content = {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun SeriesListPreview() {

}