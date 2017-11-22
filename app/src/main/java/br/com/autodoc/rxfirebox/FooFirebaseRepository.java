package br.com.autodoc.rxfirebox;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.List;

import br.com.autodoc.rxfirebox.database.DatabaseObserver;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class FooFirebaseRepository extends DatabaseBox<Foo> {
    private final DatabaseReference databaseReference;
    private final DatabaseObserver databaseBox;

    public FooFirebaseRepository(DatabaseReference databaseReference, DatabaseObserver databaseBox) {
        this.databaseReference = databaseReference;
        this.databaseBox = databaseBox;
    }

    public Completable save(Foo foo) {
        return databaseBox.set(foo, databaseReference);
    }

    public Maybe<List<Foo>> single() {
        Query query = databaseReference.orderByValue().equalTo(false);
        return databaseBox.single(query, toList());
    }

    public Flowable<List<Foo>> list() {
        Query query = databaseReference.orderByValue();
        return databaseBox.list(query, toList());
    }
}
