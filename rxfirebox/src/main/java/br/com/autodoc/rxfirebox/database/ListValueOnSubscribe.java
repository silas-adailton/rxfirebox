package br.com.autodoc.rxfirebox.database;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;

public class ListValueOnSubscribe<T> implements FlowableOnSubscribe<T> {

    private Query mQuery;
    private Function<DataSnapshot, T> mMarshaller;
    private final boolean mEnableRealTimeListener;

    public ListValueOnSubscribe(Query query, Function<DataSnapshot, T> marshaller) {
        mQuery = query;
        mMarshaller = marshaller;
        mEnableRealTimeListener = false;
    }

    public ListValueOnSubscribe(Query query, Function<DataSnapshot, T> marshaller,
                                boolean enableRealTimeListener) {
        mQuery = query;
        mMarshaller = marshaller;
        mEnableRealTimeListener = enableRealTimeListener;

    }

    @Override
    public void subscribe(FlowableEmitter<T> e) throws Exception {
        ValueEventListener listener = new RxSingleValueListener<>(e, mMarshaller, mEnableRealTimeListener);
        e.setCancellable(() -> {
            this.mQuery.removeEventListener(listener);
        });

        if (this.mEnableRealTimeListener) {
            this.mQuery.addValueEventListener(listener);
            return;
        }

        this.mQuery.addValueEventListener(listener);
    }


    private static class RxSingleValueListener<T> implements ValueEventListener {

        private final FlowableEmitter<T> subscriber;
        private final Function<DataSnapshot, T> marshaller;
        private final boolean mEnableRealTimeListener;

        RxSingleValueListener(FlowableEmitter<T> subscriber, Function<DataSnapshot, T> marshaller, boolean enableRealTimeListener) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
            this.mEnableRealTimeListener = enableRealTimeListener;
        }

        @Override
        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

            try {
                if (null != marshaller.apply(dataSnapshot))
                    this.subscriber.onNext(this.marshaller.apply(dataSnapshot));
            } catch (Exception e) {
                this.subscriber.onError(e);
            }

            if(!mEnableRealTimeListener){
                this.subscriber.onComplete();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            this.subscriber.onError(databaseError.toException());
        }
    }
}
