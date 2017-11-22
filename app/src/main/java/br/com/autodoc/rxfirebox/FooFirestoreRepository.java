package br.com.autodoc.rxfirebox;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.autodoc.rxfirebox.database.DatabaseBox;
import br.com.autodoc.rxfirebox.firestore.FirestoreBox;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class FooFirestoreRepository extends Box<Foo> {
    private final CollectionReference reference;
    private final FirestoreBox box;

    public FooFirestoreRepository(CollectionReference reference, FirestoreBox box) {
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
        return box.single(query, toDocumentList());
    }

    public Flowable<List<Foo>> where(String name) {
        Query query = reference.whereEqualTo("name",name);
        return box.list(query, toDocumentList());
    }
}
