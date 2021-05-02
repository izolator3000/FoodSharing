package com.example.foodsharing.repository.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.repository.DatabaseProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class FirebaseDataProvider implements DatabaseProvider {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public List<Map<String, Object>> getDataFromFirebase() {
        List<Map<String, Object>> foods = new ArrayList<>();
        db.collection("foods")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (QueryDocumentSnapshot document : snapshot) {
                        foods.add(document.getData());
                        Log.d(getClass().getSimpleName(), document.getId() + "->" + document.getData());
                    }
                }).addOnFailureListener(e -> {
            Log.e(getClass().getSimpleName(), e.getMessage());
        });

        Log.e(getClass().getSimpleName(), foods.toString());
        return foods;
    }

    @Override
    public void pushRequest(FoodModel model) {

// Create a new user with a first and last name
        Map<String, Object> firebaseModel = new HashMap<>();
        firebaseModel.put("title", model.getTitle());
        firebaseModel.put("addressTitle", model.getAddress_title());
        firebaseModel.put("data", model.getData());

// Add a new document with a generated ID
        db.collection("foods")
                .add(firebaseModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(getClass().getSimpleName(), "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(getClass().getSimpleName(), "Error adding document", e);
                    }
                });
    }
}
