package br.com.autodoc.rxfirebox.firestore;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class SetValueOnSubscriber<T> implements CompletableOnSubscribe {

    private final Map<String, T>  value;
    private final DocumentReference reference;

    public SetValueOnSubscriber(Map<String, T> value, DocumentReference reference) {
        this.value = value;
        this.reference = reference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        reference.set((Map<String, Object>) value)
                .addOnSuccessListener(new RxCompletionListener(e))
                .addOnFailureListener(new RxCompletionListener(e));

    }

    private static class RxCompletionListener<T> implements OnSuccessListener<Void>,OnFailureListener {

        private final CompletableEmitter subscriber;

        public RxCompletionListener(CompletableEmitter subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onSuccess(@NonNull Void aVoid) {
            subscriber.onComplete();
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            subscriber.onError(e);
        }
    }
}
