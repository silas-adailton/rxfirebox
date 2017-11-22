package br.com.autodoc.rxfirebox.firestore;


import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.functions.Function;

public class SingleValueOnSubscribe<T> implements MaybeOnSubscribe<T> {

    private Query mQuery;
    private Function<QuerySnapshot, T> mMarshaller;


    public SingleValueOnSubscribe(Query query, Function<QuerySnapshot, T> marshaller) {
        mQuery = query;
        mMarshaller = marshaller;
    }

    @Override
    public void subscribe(MaybeEmitter<T> e) throws Exception {
        EventListener eventListener = new RxSingleValueListener<>(e, mMarshaller);
        mQuery.addSnapshotListener(eventListener);
    }

    private static class RxSingleValueListener<T> implements EventListener<QuerySnapshot> {

        private final MaybeEmitter<T> subscriber;
        private final Function<QuerySnapshot, T> marshaller;

        RxSingleValueListener(MaybeEmitter<T> subscriber, Function<QuerySnapshot, T> marshaller) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
        }

        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

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
