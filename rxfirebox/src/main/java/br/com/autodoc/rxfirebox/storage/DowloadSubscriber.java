package br.com.autodoc.rxfirebox.storage;

import android.net.Uri;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;


class DowloadSubscriber implements FlowableOnSubscribe<Dowload> {

    private final Uri file;
    private final StorageReference storageReference;

    public DowloadSubscriber(Uri file, StorageReference storageReference) {
        this.file = file;
        this.storageReference = storageReference;
    }

    @Override
    public void subscribe(FlowableEmitter<Dowload> emitter) throws Exception {

        storageReference.getFile(file)
                .addOnSuccessListener(taskSnapshot -> emitProgress(emitter, taskSnapshot))
                .addOnProgressListener(taskSnapshot -> emitProgress(emitter, taskSnapshot)).
                addOnFailureListener(error -> emitter.onError(error.getCause())).
                addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        emitter.onComplete();
                    }
                });

    }


    private void emitCurrentFile(FlowableEmitter<Upload> emitter) {
        emitter.onNext(new Upload(100.0, Uri.parse(""), file));
        emitter.onComplete();
    }

    private void emitProgress(FlowableEmitter<Dowload> e, FileDownloadTask.TaskSnapshot taskSnapshot) {
        double progress = getProgress(taskSnapshot);
        e.onNext(new Dowload(progress));
    }

    private double getProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
        return (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
    }
}
