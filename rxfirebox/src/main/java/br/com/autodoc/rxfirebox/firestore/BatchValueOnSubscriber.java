package br.com.autodoc.rxfirebox.firestore;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class BatchValueOnSubscriber implements CompletableOnSubscribe {

    private final WriteBatch value;

    public BatchValueOnSubscriber(WriteBatch value) {
        this.value = value;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
       //value.commit().addOnCompleteListener(new RxCompletionListener(e));
        value.commit();
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
