package br.com.autodoc.rxfirebox.firestore;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class AddValueOnSubscriber implements CompletableOnSubscribe {

    private final Object value;
    private final CollectionReference reference;

    public AddValueOnSubscriber(Object value, CollectionReference reference) {
        this.value = value;
        this.reference = reference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        reference.add(value)
                .addOnCompleteListener(new RxCompletionListener(e));
    }

    private static class RxCompletionListener implements OnCompleteListener<DocumentReference>{

        private final CompletableEmitter subscriber;

        public RxCompletionListener(CompletableEmitter subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onComplete(@NonNull Task<DocumentReference> task) {
            if(task.getException() != null){
                subscriber.onError(task.getException());
            }else {
                subscriber.onComplete();
            }
        }
    }
}
