package br.com.autodoc.rxfirebox;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.functions.Function;

public class FirestoreBox<T> implements Box {

    @Override
    public Class<T> getMyType() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

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

    public Function<QuerySnapshot, List<T>> toListChanges() {

        return querySnapshot -> {
            List<T> list = new ArrayList<>();
            if (querySnapshot.size() > 0) {
                List<DocumentChange> documents = querySnapshot.getDocumentChanges();
                for (DocumentChange document : documents) {
                    list.add(document.getDocument().toObject(getMyType()));
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
            Map<String, Object> stringMap = new HashMap<>();

            if (querySnapshot.size() > 0) {
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                for (DocumentSnapshot document : documents) {
                    stringMap.put(document.getId(),document);
                }
            }
            return stringMap;
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
    public Function<DataSnapshot, Set<String>> toSet() {

        return dataSnapshot -> {

            Set<String> keys = new HashSet<>();

            if (dataSnapshot.hasChildren()) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    keys.add(child.getKey());
                }
            }
            return keys;

        };
    }
}