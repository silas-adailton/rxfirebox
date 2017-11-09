package br.com.autodoc.rxfirebox.database;


import com.google.firebase.database.DatabaseReference;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class SetValueOnSubscriber<T> implements CompletableOnSubscribe {

    private final T value;
    private final DatabaseReference databaseReference;

    SetValueOnSubscriber(T value, DatabaseReference databaseReference) {
        this.value = value;
        this.databaseReference = databaseReference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        databaseReference.setValue(value);
        e.onComplete();
    }
}
