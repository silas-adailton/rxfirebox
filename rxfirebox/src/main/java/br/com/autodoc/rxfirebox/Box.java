package br.com.autodoc.rxfirebox;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.functions.Function;

public class Box<T> {

    private final Class<T> clazz;

    public Box(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Function<DataSnapshot, List<T>> toList() {
        return dataSnapshot -> {
            List<T> list = new ArrayList<>();
            if (dataSnapshot.hasChildren()) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    list.add(child.getValue(clazz));
                }
            }
            return list;
        };
    }

    public Function<DataSnapshot, T> toClass() {
        return dataSnapshot -> dataSnapshot.getValue(clazz);
    }


    public Function<DataSnapshot, Map<String, Object>> toMap() {

        return dataSnapshot -> {

            Map<String, Object> stringMap = null;

            if (dataSnapshot.hasChildren())
                stringMap = (Map<String, Object>) dataSnapshot.getValue();

            return stringMap;
        };
    }


    public Function<DataSnapshot, T> toFirst() {

        return dataSnapshot -> {

            if (dataSnapshot.hasChildren()) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    return child.getValue(clazz);
                }
            }
            return null;
        };
    }


    public Function<DataSnapshot, Set<String>> toSet() {

        return dataSnapshot -> {
            GenericTypeIndicator<Map<String, Boolean>> map = new GenericTypeIndicator<Map<String, Boolean>>() {
            };

            Map<String, Boolean> stringMap = null;

            if (dataSnapshot.hasChildren())
                stringMap = dataSnapshot.getValue(map);

            return stringMap.keySet();
        };
    }

}