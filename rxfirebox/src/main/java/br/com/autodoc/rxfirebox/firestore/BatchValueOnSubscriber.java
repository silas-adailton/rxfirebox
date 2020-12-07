package br.com.autodoc.rxfirebox.firestore;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.WriteBatch;

import br.com.autodoc.rxfirebox.Executor;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class BatchValueOnSubscriber implements CompletableOnSubscribe {

    private final WriteBatch value;
    private final boolean useListener;

    public BatchValueOnSubscriber(WriteBatch value, boolean useListener) {
        this.value = value;
        this.useListener = useListener;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
       if(useListener) {
           value.commit().addOnCompleteListener(Executor.Companion.executeThreadPoolExecutor(), new RxCompletionListener(e));
       }else {
           value.commit();
           e.onComplete();
       }
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
