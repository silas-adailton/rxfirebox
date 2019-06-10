package br.com.autodoc.rxfirebox.firestore;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.DocumentSnapshot;

import br.com.autodoc.rxfirebox.Executor;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.functions.Function;

public class DocumentValueOnSubscribe<T> implements MaybeOnSubscribe<T> {

    private DocumentReference mDocument;
    private Function<DocumentSnapshot, T> mMarshaller;


    public DocumentValueOnSubscribe(DocumentReference documentReference, Function<DocumentSnapshot, T> marshaller) {
        mDocument = documentReference;
        mMarshaller = marshaller;
    }

    @Override
    public void subscribe(MaybeEmitter<T> e) throws Exception {
        EventListener eventListener = new RxSingleValueListener<>(e, mMarshaller);
        mDocument.addSnapshotListener(Executor.Companion.executeThreadPoolExecutor(), eventListener);
    }

    private static class RxSingleValueListener<T> implements EventListener<DocumentSnapshot> {

        private final MaybeEmitter<T> subscriber;
        private final Function<DocumentSnapshot, T> marshaller;

        RxSingleValueListener(MaybeEmitter<T> subscriber, Function<DocumentSnapshot, T> marshaller) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
        }

        @Override
        public void onEvent(DocumentSnapshot documentSnapshots, FirebaseFirestoreException e) {

            if(null != e)
                subscriber.onError(e);
            else
            {
                try {
                    if (null != marshaller.apply(documentSnapshots))
                        subscriber.onSuccess(marshaller.apply(documentSnapshots));
                } catch (Exception e1) {
                    subscriber.onError(e1);
                }

                subscriber.onComplete();
            }
        }
    }
}
