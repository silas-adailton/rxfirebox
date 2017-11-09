package br.com.autodoc.rxfirebox.database;


import com.google.firebase.database.DatabaseReference;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class UpdateValueOnSubscriber<T> implements CompletableOnSubscribe {

    private final Map<String, T>  value;
    private final DatabaseReference databaseReference;

    UpdateValueOnSubscriber(Map<String, T> value, DatabaseReference databaseReference) {
        this.value = value;
        this.databaseReference = databaseReference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        databaseReference.updateChildren((Map<String, Object>) value);
        e.onComplete();
    }
    
}
