package com.example.foodsharing.repository.remote

import android.util.Log
import com.example.foodsharing.model.FoodModel
import com.example.foodsharing.repository.DatabaseProvider
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import java.util.*

class FirebaseDataProviderKt : DatabaseProvider {
    var db = FirebaseFirestore.getInstance()

    private val result = MutableStateFlow<List<FoodModel>?>(null)
    private var subscribeOnDb = false


    override fun pushRequest(model: FoodModel) {

// Create a new user with a first and last name
        val firebaseModel: MutableMap<String, Any> = HashMap()
        firebaseModel["title"] = model.title
        firebaseModel["latitude"] = model.address[0]
        firebaseModel["longitude"] = model.address[1]
        firebaseModel["data"] = model.data

// Add a new document with a generated ID
        db.collection("foods")
            .add(firebaseModel)
            .addOnSuccessListener(object : OnSuccessListener<DocumentReference> {
                override fun onSuccess(documentReference: DocumentReference) {
                    Log.d(
                        javaClass.simpleName,
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                }
            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    Log.w(javaClass.simpleName, "Error adding document", e)
                }
            })
    }

    override fun observeFoods(): Flow<List<FoodModel>> {
        if (!subscribeOnDb) getDataFromFirebase()
        return result.filterNotNull()
    }

    override fun getDataFromFirebase() {
        if (subscribeOnDb) return
        handleFoodsReference(
            {
                getFoodsCollection().addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e(javaClass.simpleName, "Observe Foods exception: $e")
                    } else if (snapshot != null) {
                        val foods = mutableListOf<FoodModel>()

                        for (doc: QueryDocumentSnapshot in snapshot) {

                            if(doc["latitude"] as Double? !=null) {
                                val model = FoodModel(
                                    doc.get("url") as String?,
                                    doc["title"] as String?,
                                    doc["latitude"] as Double?,
                                    doc["longitude"] as Double?,
                                    doc["data"] as String?
                                )

                                foods.add(model)
                            }else{
                                doc.reference.delete();
                            }
                        }
                        result.value = foods
                    }
                }
                subscribeOnDb = true
            }, {
                Log.e(javaClass.simpleName, "Error subscribe, message: ${it.message}")
            }

        )
    }

    private inline fun handleFoodsReference(
        referenceHandler: (CollectionReference) -> Unit,
        exceptionHandler: (Throwable) -> Unit = {}
    ) {
        kotlin.runCatching {
            getFoodsCollection()
        }.fold(referenceHandler, exceptionHandler)
    }

    private fun getFoodsCollection() = db.collection("foods")

}