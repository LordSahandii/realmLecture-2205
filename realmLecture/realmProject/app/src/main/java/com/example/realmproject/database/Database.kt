package com.example.realmproject.database

import android.util.Log
import com.example.realmproject.models.Characters
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialRealm
import io.realm.kotlin.notifications.RealmChange
import io.realm.kotlin.notifications.UpdatedRealm
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Realm Database
 */
class Database() {
        // Configuring the realm database
        private val configuration = RealmConfiguration.create(schema = setOf(Characters::class))
        val realm = Realm.open(configuration)

        /**
         * Write to the realm database Asynchronously using Coroutine
         */
        suspend fun createAsync(character: Characters) {
            //CoroutineScope(Dispatchers.Main).launch {
                realm.write {
                    copyToRealm(character)
                //}
            }
        }

        /**
         * Find a single object by its name and turn it to Evil or Good
         */
        suspend fun updateByID(name: String, turnedEvil: Boolean){
            realm.query<Characters>("name = $0", name).first().find()?.also {
                found ->
                    realm.write { findLatest(found)?.isEvil = turnedEvil}
            }

        }

    /**
     * Delete an object
     */
    suspend fun deleteAsync(name: String) {
            realm.write {
                // fetch the frog by primary key value, passed in as argument number 0
                val characters: Characters =
                    this.query<Characters>("name = $0", name).find().first()
                // call delete on the results of a query to delete the object permanently
                delete(characters)
            }
        }

    /**
     * Get all the Characters that are Evil
     * @return List of Evil
     */
    fun getALlEvil():List<Characters> {
            return realm.query<Characters>("isEvil = $0", true).find()
        }

    /**
     * Get all the characters that are Good
     * @return list of Good
     */
    fun getALlGood(): List<Characters> {
            return realm.query<Characters>("isEvil = $0", false).find()
        }

    /**
     * Subscribe to read the changes in realm asynchronously
     */
    suspend fun readAsync() {
            CoroutineScope(Dispatchers.Main).launch {
                realm.asFlow()
                    .collect { realmChange: RealmChange<Realm> ->
                        when (realmChange) {
                            is InitialRealm<*> -> Log.d("Initial Realm",realmChange.toString())
                            is UpdatedRealm<*> -> Log.d("Realm updated", realmChange.toString())
                        }
                    }
            }
        }

}