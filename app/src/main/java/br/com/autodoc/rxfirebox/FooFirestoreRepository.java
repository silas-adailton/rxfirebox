package br.com.autodoc.rxfirebox;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.autodoc.rxfirebox.firestore.FirestoreObserver;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class FooFirestoreRepository extends FirestoreBox<Foo> {
    private final CollectionReference reference;
    private final FirestoreObserver box;

    public FooFirestoreRepository(CollectionReference reference, FirestoreObserver box) {
        this.reference = reference;
        this.box = box;
    }

    public Completable save(Foo foo) {
        Map<String,Object> map = new HashMap<>();
        map.put("name", foo.getName());
        return box.set(map, reference.document());
    }

    public Maybe<List<Foo>> single() {
        Query query = reference;
        return box.single(query, toList());
    }

    public Flowable<List<Foo>> where(String name) {
        Query query = reference.whereEqualTo("name",name);
        return box.list(query, toList());
    }
}
