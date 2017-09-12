package br.com.autodoc.rxfirebox.storage;


import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class StorageBox {

    /**
     *
     * @param file path of image
     * @param storageReference local in storage where file be saved
     * @return Upload object with information of upload
     */
    public Flowable<Upload> upload(Uri file, StorageReference storageReference) {
        return Flowable.create(new UploadSubscriber(file, storageReference), BackpressureStrategy.BUFFER);
    }

}
