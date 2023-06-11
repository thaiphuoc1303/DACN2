package com.vision.exercise.vision.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vision.exercise.vision.model.Exercise
import io.reactivex.rxjava3.core.Single

class RealTimeDataBase {
    private val database = Firebase.database

    fun getListExercise(): Single<List<Exercise>> {
        return Single.create {
            database.getReference("exercise").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val list = arrayListOf<Exercise>()
                        for (data in dataSnapshot.children) {
                            val steps = arrayListOf<Exercise.Step>()
                            for (step in data.child("steps").children) {
                                step.getValue(Exercise.Step::class.java)?.let { s ->
                                    steps.add(s)
                                }
                            }
                            data.getValue(Exercise::class.java) ?.let { exercise ->
                                exercise.steps = steps.sortedBy { st ->
                                    st.position
                                }
                                list.add(exercise)
                            }
                        }
                        it.onSuccess(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        it.onError(Throwable("Exception"))
                    }
                }
            )
        }
    }
}