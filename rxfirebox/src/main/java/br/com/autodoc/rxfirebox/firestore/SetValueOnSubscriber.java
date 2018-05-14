package br.com.autodoc.rxfirebox.firestore;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class SetValueOnSubscriber implements CompletableOnSubscribe {

    private final Object value;
    private final DocumentReference reference;

    public SetValueOnSubscriber(Object value, DocumentReference reference) {
        this.value = value;
        this.reference = reference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        reference.set(value)
                .addOnCompleteListener(new RxCompletionListener(e));

    }

    private static class RxCompletionListener implements OnCompleteListener<Void> {

        private final CompletableEmitter subscriber;

        public RxCompletionListener(CompletableEmitter subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.getException() != null){
                subscriber.onError(task.getException());
            }else {
                subscriber.onComplete();
            }
        }
    }
}
