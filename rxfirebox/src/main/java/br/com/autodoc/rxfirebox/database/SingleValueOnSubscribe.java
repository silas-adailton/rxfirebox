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
    private ValueEventListener listener;


    public SingleValueOnSubscribe(Query query, Function<DataSnapshot, T> marshaller) {
        mQuery = query;
        mMarshaller = marshaller;
    }

    @Override
    public void subscribe(MaybeEmitter<T> e) throws Exception {
        listener = new RxSingleValueListener<>(e, mMarshaller);
        mQuery.addListenerForSingleValueEvent(listener);
    }


    private class RxSingleValueListener<T> implements ValueEventListener {

        private final MaybeEmitter<T> subscriber;
        private final Function<DataSnapshot, T> marshaller;

        RxSingleValueListener(MaybeEmitter<T> subscriber, Function<DataSnapshot, T> marshaller) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            try {
                if(null != marshaller.apply(dataSnapshot)) {
                    subscriber.onSuccess(marshaller.apply(dataSnapshot));
                    mQuery.removeEventListener(listener);
                }
            } catch (Exception e) {
                subscriber.onError(e);
                mQuery.removeEventListener(listener);
            }

            subscriber.onComplete();
            mQuery.removeEventListener(listener);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            subscriber.onError(databaseError.toException());
            mQuery.removeEventListener(listener);
        }
    }
}
