package br.com.autodoc.rxfirebox.firestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class FirestoreObserver {

    /**
     * Run the query once and cancel the listener
     *
     * @param query firebase query
     * @param marshaller function to convert data
     * @param <T> type of the return
     * @return return type converted in marshaller
     */
    public <T> Maybe<T> single(Query query, Function<QuerySnapshot, T> marshaller) {
        return Maybe.create(new SingleValueOnSubscribe<>(query, marshaller));
    }

    /**
     * Run the query and not cancel the listener
     *
     * @param query firebase query
     * @param marshaller function to convert data
     * @param <T> Type of the return
     * @return return type converted in marshaller
     */
    public <T> Flowable<T> list(Query query, Function<QuerySnapshot, T> marshaller) {
        return Flowable.create(new ListValueOnSubscribe<>(query, marshaller), BackpressureStrategy.BUFFER);
    }

    /**
     * Create value in database
     *
     * @param value data create in database
     * @param reference where the data will be saved
     * @param <T> Type of the value
     * @return success or error
     */
    public <T> Completable set(Map<String, T> value, DocumentReference reference) {
        return Completable.create(new SetValueOnSubscriber<T>(value, reference));
    }

    /**
     * update value in database
     *
     * @param value data create in database
     * @param reference where the data will be saved
     * @param <T> Type of the value
     * @return success or error
     */
    public <T> Completable add(Map<String, T> value, CollectionReference reference) {
        return Completable.create(new AddValueOnSubscriber<T>(value, reference));
    }

    /**
     *
     * remove end point in database
     *
     * @param reference reference that be removed
     * @return success or error
     */
    public Completable remove(DocumentReference reference) {
        return Completable.create(new RemoveValueOnSubscriber(reference));
    }
}
