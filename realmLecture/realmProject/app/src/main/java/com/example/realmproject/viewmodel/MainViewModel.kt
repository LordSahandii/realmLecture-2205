package com.example.realmproject.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmproject.database.Database
import com.example.realmproject.models.Characters
import com.example.realmproject.models.Specs
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.launch

/**
 * MAINVIEW MODEL TO CREATE AN INSTANCE OF DATABASE
 */
class MainViewModel: ViewModel() {

    // List of Characters
    var listOfGood: List<Characters> by mutableStateOf(listOf())
    var listOfEvil: List<Characters> by mutableStateOf(listOf())

    // Getting an instance of DATABASE class
    val database = Database()

    // creating the character
    fun createCharacter(character: Characters) {
        viewModelScope.launch {
            database.createAsync(character)
        }
    }

    // updating the character
     fun updateByID(name: String, turnedEvil: Boolean){
         viewModelScope.launch {
             database.updateByID(name, turnedEvil)
             getEvil()
             getGood()
         }
     }

    // updating the list of evils
    fun getEvil(){
        viewModelScope.launch {
            listOfEvil = database.getALlEvil()
        }
    }

    // updating the list of goods
    fun getGood(){
        viewModelScope.launch {
            listOfGood = database.getALlGood()
        }
    }

    // delete a single character
    fun delete(name: String){
        viewModelScope.launch {
            database.deleteAsync(name)
        }
    }

}

