package br.com.autodoc.rxfirebox.firestore;


import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;

public class ListValueChangesOnSubscribe<T> implements FlowableOnSubscribe<T> {

    private Query mQuery;
    private Function<QuerySnapshot, T> mMarshaller;


    public ListValueChangesOnSubscribe(Query query, Function<QuerySnapshot, T> marshaller) {
        mQuery = query;
        mMarshaller = marshaller;
    }

    @Override
    public void subscribe(FlowableEmitter<T> e) throws Exception {
        EventListener eventListener = new RxSingleValueListener<>(e, mMarshaller);
        mQuery.addSnapshotListener(eventListener);
    }


    private static class RxSingleValueListener<T> implements EventListener<QuerySnapshot> {

        private final FlowableEmitter<T> subscriber;
        private final Function<QuerySnapshot, T> marshaller;

        RxSingleValueListener(FlowableEmitter<T> subscriber, Function<QuerySnapshot, T> marshaller) {
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
                        subscriber.onNext(marshaller.apply(documentSnapshots));
                } catch (Exception e1) {
                    subscriber.onError(e1);
                }
            }
        }
    }
}
