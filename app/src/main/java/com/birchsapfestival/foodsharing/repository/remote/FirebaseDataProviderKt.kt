package com.birchsapfestival.foodsharing.repository.remote

import android.net.Uri
import android.util.Log
import com.birchsapfestival.foodsharing.NoAuthException
import com.birchsapfestival.foodsharing.model.FoodModel
import com.birchsapfestival.foodsharing.model.User
import com.birchsapfestival.foodsharing.repository.DatabaseProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import java.util.*


private const val FOODS_COLLECTION = "foods"
private const val USERS_COLLECTION = "users"

class FirebaseDataProviderKt() : DatabaseProvider {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Create a storage reference from our app
    private val storageRef = storage.reference
    private val currentUser
        get() = auth.currentUser


    private val result = MutableStateFlow<List<FoodModel>?>(null)
    private var subscribeOnDb = false


    override fun pushRequest(model: FoodModel) {

// Create a new user with a first and last name
        val firebaseModel: MutableMap<String, Any> = HashMap()
        firebaseModel["email"] = model.email ?: "null"
        firebaseModel["phoneNumber"] = model.phoneNumber ?: "null"
        firebaseModel["title"] = model.title
        firebaseModel["latitude"] = model.address[0]
        firebaseModel["longitude"] = model.address[1]
        firebaseModel["data"] = model.data
        firebaseModel["id"] = model.id
        firebaseModel["type"] = model.type
            firebaseModel["uri"] = uploadImage(model.uri) ?: "null"

// Add a new document with a generated ID
        db.collection("foods").document(model.id.toString())
            .set(firebaseModel)
            .addOnSuccessListener {
                Log.d(
                    javaClass.simpleName,
                    "DocumentSnapshot added with ID: " + model.id
                )
            }
            .addOnFailureListener {
                Log.w(javaClass.simpleName, "Error adding document ${model.id} with exception $it")
            }

    }

    private fun downloadImage():Uri? {
        var uri:Uri? = null
        storageRef.child("images/images1.jpg").downloadUrl.addOnSuccessListener {
            uri = it
        }.addOnFailureListener {

        }
        return uri
    }

    private  fun uploadImage(uri:Uri):String? {
        // Create a child reference
// imagesRef now points to "images"
        var uriResult:String? = null
        val imagesRef: StorageReference = storageRef.child("images/images1.jpg")
        imagesRef.putFile(uri)
        imagesRef.downloadUrl.addOnSuccessListener {
            uriResult= it.toString()
        }
        Thread.sleep(3000)
//imagesRef.downloadUrl.addOnSuccessListener { zzaa.u }
        return uriResult //imagesRef.downloadUrl.toString() //imagesRef.name//toString()
    }

    override fun deleteRequest(id: Long) {

        db.collection("foods").document(id.toString()).delete().addOnSuccessListener {
            Log.d(javaClass.simpleName, "Food is deleted with id $id")
        }.addOnFailureListener {
            Log.d(javaClass.simpleName, "Error delete food, message: ${it.message}")
        }
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

                            if (doc["latitude"] as Double? != null) {// && doc["type"] as String? == "thing"
                                val model = FoodModel(
                                    doc.get("email") as String?,
                                    doc.get("phoneNumber") as String?,
                                    doc["title"] as String?,
                                    doc["latitude"] as Double?,
                                    doc["longitude"] as Double?,
                                    doc["data"] as String?,
                                    doc["id"] as Long?,
                                    doc["type"] as String?
                                )

                                foods.add(model)
                            } else {
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
        runCatching {
            getFoodsCollection()
        }.fold(referenceHandler, exceptionHandler)
    }


    override fun getCurrentUser(): User? =
        currentUser?.run { User(displayName, email, phoneNumber) }

    private fun getUserFoodsCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(FOODS_COLLECTION)
    } ?: throw  NoAuthException()

    private fun getFoodsCollection() = db.collection("foods")
}