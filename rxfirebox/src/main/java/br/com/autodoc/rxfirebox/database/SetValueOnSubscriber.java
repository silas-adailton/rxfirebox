package br.com.autodoc.rxfirebox.database;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class SetValueOnSubscriber<T> implements CompletableOnSubscribe {

    private final T value;
    private final DatabaseReference databaseReference;

    SetValueOnSubscriber(T value, DatabaseReference databaseReference) {
        this.value = value;
        this.databaseReference = databaseReference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        databaseReference.setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {
                e.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
                e.onError(exception);
            }
        });
    }
}
