package br.com.autodoc.rxfirebox.database;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.functions.Function;

public class SingleValueOnSubscribe<T> implements MaybeOnSubscribe<T> {

    private Query mQuery;
    private Function<DataSnapshot, T> mMarshaller;


    public SingleValueOnSubscribe(Query query, Function<DataSnapshot, T> marshaller) {
        mQuery = query;
        mMarshaller = marshaller;
    }

    @Override
    public void subscribe(MaybeEmitter<T> e) throws Exception {
        mQuery.addListenerForSingleValueEvent(new RxSingleValueListener<>(e, mMarshaller, mQuery));
    }


    private static class RxSingleValueListener<T> implements ValueEventListener {

        private final MaybeEmitter<T> subscriber;
        private final Function<DataSnapshot, T> marshaller;
        private final Query query;

        RxSingleValueListener(MaybeEmitter<T> subscriber, Function<DataSnapshot, T> marshaller, Query query) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
            this.query = query;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            query.removeEventListener(this);
            
            try {
                if(null != marshaller.apply(dataSnapshot)) {
                    subscriber.onSuccess(marshaller.apply(dataSnapshot));
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }

            subscriber.onComplete();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            query.removeEventListener(this);
            subscriber.onError(databaseError.toException());
        }
    }
}
