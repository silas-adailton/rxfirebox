package br.com.autodoc.rxfirebox.firestore;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.autodoc.rxfirebox.Executor;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;

public class ListValueOnSubscribe<T> implements FlowableOnSubscribe<T> {

    private Query mQuery;
    private Function<QuerySnapshot, T> mMarshaller;


    public ListValueOnSubscribe(Query query, Function<QuerySnapshot, T> marshaller) {
        mQuery = query;
        mMarshaller = marshaller;
    }

    @Override
    public void subscribe(FlowableEmitter<T> e) throws Exception {
        OnSuccessListener onSuccessListener = new RxSingleValueListener<>(e, mMarshaller);
        mQuery.get().addOnSuccessListener(Executor.Companion.executeThreadPoolExecutor(), onSuccessListener);
    }


    private static class RxSingleValueListener<T> implements OnSuccessListener<QuerySnapshot> {

        private final FlowableEmitter<T> subscriber;
        private final Function<QuerySnapshot, T> marshaller;

        RxSingleValueListener(FlowableEmitter<T> subscriber, Function<QuerySnapshot, T> marshaller) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
        }


        @Override
        public void onSuccess(QuerySnapshot querySnapshot) {
            try {
                if (null != marshaller.apply(querySnapshot))
                    subscriber.onNext(marshaller.apply(querySnapshot));
            } catch (Exception e) {
                subscriber.onError(e);
            }

            subscriber.onComplete();
        }
    }
}
