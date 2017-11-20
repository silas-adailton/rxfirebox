package br.com.autodoc.rxfirebox;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.functions.Function;

public class Box<T> {

    private Class<T> getMyType() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Function<DataSnapshot, List<T>> toList() {
        return dataSnapshot -> {
            List<T> list = new ArrayList<>();
            if (dataSnapshot.hasChildren()) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    list.add(child.getValue(getMyType()));
                }
            }
            return list;
        };
    }

    public Function<DataSnapshot, T> toClass() {
        return dataSnapshot -> dataSnapshot.getValue(getMyType());
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
                    return child.getValue(getMyType());
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