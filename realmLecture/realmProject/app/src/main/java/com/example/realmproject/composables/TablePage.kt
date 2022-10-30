package com.example.realmproject.composables

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmproject.database.Database
import com.example.realmproject.models.Characters
import com.example.realmproject.models.Specs
import com.example.realmproject.viewmodel.MainViewModel

// reference: https://alexzh.com/jetpack-compose-building-grids/
/**
 * The horizontally scrollable table with header and content.
 * @param columnCount the count of columns in the table
 * @param cellWidth the width of column, can be configured based on index of the column.
 * @param data the data to populate table.
 * @param modifier the modifier to apply to this layout node.
 * @param headerCellContent a block which describes the header cell content.
 * @param cellContent a block which describes the cell content.
 */
@Composable
fun <T> Table(
    columnCount: Int,
    cellWidth: (index: Int) -> Dp,
    data: List<T>,
    headerCellContent: @Composable (index: Int) -> Unit,
    cellContent: @Composable (index: Int, item: T) -> Unit,
) {
    Surface(
       // modifier = modifier
    ) {
        LazyRow(
            modifier = Modifier.padding(16.dp)
        ) {
            items((0 until columnCount).toList()) { columnIndex ->
                Column {
                    (0..data.size).forEach { index ->
                        Surface(
                            border = BorderStroke(1.dp, Color.LightGray),
                            contentColor = Color.Transparent,
                            modifier = Modifier.width(cellWidth(columnIndex))
                        ) {
                            if (index == 0) {
                                headerCellContent(columnIndex)
                            } else {
                                cellContent(columnIndex, data[index - 1])
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createObejcts():Characters{
    // you can change these information
    var Frodo = Characters().apply {
        name = "Frodo"
        age = 22
        isEvil = false
        specs = Specs.HOBBITS
    }
    return Frodo
}

/**
 * @param mainViewModel
 * shows the data from Realm database in tables
 */
@Composable
fun MiddleEarthTable(mainViewModel: MainViewModel) {

    // to create an object call this function: (uncomment the lines below)
//    var character = createObejcts()
//    mainViewModel.createCharacter(character)

    // to delete from realm database use the line below
//    mainViewModel.delete("character")

    // to make a character evil, or good use this function and update it
    //mainViewModel.updateByID(character.name, false)

    //retrieve the lists of good and evil
    mainViewModel.getEvil()
    mainViewModel.getGood()

    // the width of the cells
    val cellWidth: (Int) -> Dp = { index ->
        when (index) {
            2 -> 250.dp
            3 -> 350.dp
            else -> 150.dp
        }
    }
    // headers title of table
    val headerCellTitle: @Composable (Int) -> Unit = { index ->
        val value = when (index) {
            0 -> "ID"
            1 -> "Name"
            2 -> "Age"
            3 -> "Evil/Good"
            4 -> "Race"
            else -> ""
        }

        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Black,
            textDecoration = TextDecoration.Underline
        )
    }
    // the placeholder for each row
    val cellText: @Composable (Int, Characters) -> Unit = { index, item ->
        val value = when (index) {
            0 -> item._id.toString()
            1 -> item.name
            2 -> item.age.toString()
            3 -> if (item.isEvil) "EVIL" else "GOOD"
            4 -> item.specs.toString()
            else -> ""
        }

        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
    // the column to show two tables
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(text = "GOOD", modifier = Modifier.size(60.dp,30.dp).align(Alignment.CenterHorizontally),fontWeight = FontWeight.ExtraBold,maxLines = 1, fontSize = 20.sp)

        Spacer(modifier = Modifier.size(10.dp))

        Table(
            columnCount = 4,
            cellWidth = cellWidth,
            data = mainViewModel.listOfGood,
            headerCellContent = headerCellTitle,
            cellContent = cellText
        )

        Spacer(modifier = Modifier.size(40.dp))

        Text(text = "EVIL", modifier = Modifier.size(60.dp,30.dp).align(Alignment.CenterHorizontally), fontWeight = FontWeight.ExtraBold,maxLines = 1, fontSize = 20.sp )

        Spacer(modifier = Modifier.size(10.dp))

        Table(
            columnCount = 4,
            cellWidth = cellWidth,
            data = mainViewModel.listOfEvil,
            headerCellContent = headerCellTitle,
            cellContent = cellText
        )
    }


}