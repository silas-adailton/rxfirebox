package br.com.autodoc.rxfirebox;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.functions.Function;

public class FirestoreBox<T> implements Box {

    @Override
    public Class<T> getMyType() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Function<QuerySnapshot, List<T>> toList() {

        return querySnapshot -> {
            List<T> list = new ArrayList<>();
            if (querySnapshot.size() > 0) {
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                for (DocumentSnapshot document : documents) {
                    list.add(document.toObject(getMyType()));
                }
            }
            return list;
        };
    }


    @Override
    public Function<DocumentSnapshot, T> toClass() {
        return documentSnapshot -> documentSnapshot.toObject(getMyType());
    }

    @Override
    public Function<QuerySnapshot, Map<String, Object>> toMap() {

        return querySnapshot -> {
            if (querySnapshot.size() > 0) {
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                for (DocumentSnapshot document : documents) {
                    return document.getData();
                }
            }
            return null;
        };
    }

    @Override
    public Function<QuerySnapshot, T> toFirst() {

        return querySnapshot -> {
            if (querySnapshot.size() > 0) {
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                for (DocumentSnapshot document : documents) {
                    return document.toObject(getMyType());
                }
            }
            return null;
        };
    }


    @Override
    public Function<QuerySnapshot, Set<String>> toSet() {

        return querySnapshot -> {
            if (querySnapshot.size() > 0) {
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                for (DocumentSnapshot document : documents) {
                    return document.getData().keySet();
                }
            }
            return null;
        };
    }
}