package br.com.autodoc.rxfirebox.storage;


import android.net.Uri;

import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
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

    /**
     *
     * @param file path of image
     * @param storageReference local in storage where file be saved
     * @return Completable object with information of upload
     */
    public Completable uploadComplete(Uri file, StorageReference storageReference) {
        return Completable.create(new UploadCompleteSubscriber(file, storageReference));
    }


    /*
     *
     * @param file path of image
     * @param storageReference local in storage where file be saved
     * @param storageMetadata information that will be saved together with the file in storage
     * @return Upload object with information of upload
     */
    public Flowable<Upload> uploadMetadata(Uri file, StorageReference storageReference, StorageMetadata storageMetadata) {
        return Flowable.create(new UploadMetadaSubscriber(file, storageReference, storageMetadata), BackpressureStrategy.BUFFER);
    }

    /**
     *
     * @param file path of image
     * @param storageReference local in storage where file be saved
     * @return Download object with information of upload
     */
    public Flowable<Dowload> download(Uri file, StorageReference storageReference) {
        return Flowable.create(new DowloadSubscriber(file, storageReference), BackpressureStrategy.BUFFER);
    }

    /**
     *
     * @param file path of image
     * @param storageReference local in storage where file be saved
     * @return Completable object with information of upload
     */
    public Completable downloadComplete(Uri file, StorageReference storageReference) {
        return Completable.create(new DownloadCompleteSubscriber(file, storageReference));
    }




}
