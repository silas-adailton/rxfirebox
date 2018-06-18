package br.com.autodoc.rxfirebox.firestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class FirestoreObserver {

    private final boolean useListener;

    public FirestoreObserver() {
        useListener = false;
    }

    public FirestoreObserver(boolean useListener) {
        this.useListener = useListener;
    }

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
     * Run the query once and cancel the listener
     *
     * @param documentReference firebase query
     * @param marshaller function to convert data
     * @param <T> type of the return
     * @return return type converted in marshaller
     */
    public <T> Maybe<T> single(DocumentReference documentReference, Function<DocumentSnapshot, T> marshaller) {
        return Maybe.create(new DocumentValueOnSubscribe<>(documentReference, marshaller));
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
     * Run the query and not cancel the listener
     *
     * @param query firebase query
     * @param marshaller function to convert data
     * @param <T> Type of the return
     * @return return type converted in marshaller
     */
    public <T> Flowable<T> listChanges(Query query, Function<QuerySnapshot, T> marshaller) {
        return Flowable.create(new ListValueChangesOnSubscribe<>(query, marshaller), BackpressureStrategy.BUFFER);
    }

    /**
     * Create value in database
     *
     * @param value data create in database
     * @param reference where the data will be saved
     * @param <T> Type of the value
     * @return success or error
     */
    public <T> Completable set(Object value, DocumentReference reference) {
        return Completable.create(new SetValueOnSubscriber(value, reference, useListener));
    }

    /**
     * Create value in database
     *
     * @param value data create in database
     * @param <T> Type of the value
     * @return success or error
     */
    public <T> Completable batch(WriteBatch value) {
        return Completable.create(new BatchValueOnSubscriber(value, useListener));
    }

    /**
     * update value in database
     *
     * @param value data create in database
     * @param reference where the data will be saved
     * @param <T> Type of the value
     * @return success or error
     */
    public <T> Completable add(Object value, CollectionReference reference) {
        return Completable.create(new AddValueOnSubscriber(value, reference,useListener));
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


    /**
     * update value in database
     *
     * @param value data create in database
     * @param reference where the data will be saved
     * @param <T> Type of the value
     * @return success or error
     */
    public <T> Completable update(Map<String,Object> value, DocumentReference reference) {
        return Completable.create(new UpdateValueOnSubscriber(value, reference, useListener));
    }
}
