package br.com.autodoc.rxfirebox.storage;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.autodoc.rxfirebox.Executor;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;


class UploadSubscriber implements FlowableOnSubscribe<Upload> {

    private final Uri file;
    private final StorageReference storageReference;

    public UploadSubscriber(Uri file, StorageReference storageReference) {
        this.file = file;
        this.storageReference = storageReference;
    }

    @Override
    public void subscribe(FlowableEmitter<Upload> emitter) throws Exception {

        if (isLocalImage(file)) {
            storageReference.putFile(file)
                    .addOnSuccessListener(Executor.Companion.executeThreadPoolExecutor(), taskSnapshot -> emitProgress(emitter, taskSnapshot))
                    .addOnProgressListener(Executor.Companion.executeThreadPoolExecutor(), taskSnapshot -> emitProgress(emitter, taskSnapshot)).
                    addOnFailureListener(error -> emitter.onError(error.getCause())).
                    addOnCompleteListener(Executor.Companion.executeThreadPoolExecutor(), task -> {
                        if (task.isComplete()) {
                            emitter.onComplete();
                        }
                    });
        } else {
            emitCurrentFile(emitter);
        }


    }

    private boolean isLocalImage(Uri file) {
        String path = file.toString();
        return !path.contains("http") && !path.contains("https");
    }

    private void emitCurrentFile(FlowableEmitter<Upload> emitter) {
        emitter.onNext(new Upload(100.0, Uri.parse(""), file));
        emitter.onComplete();
    }

    private void emitProgress(FlowableEmitter<Upload> e, UploadTask.TaskSnapshot taskSnapshot) {
        double progress = getProgress(taskSnapshot);
        Uri session = taskSnapshot.getUploadSessionUri();
        Uri uri = taskSnapshot.getDownloadUrl();

        if (session == null) {
            session = Uri.parse("");
        }

        if (uri == null) {
            uri = Uri.parse("");
        }

        e.onNext(new Upload(progress, session, uri));
    }

    private double getProgress(UploadTask.TaskSnapshot taskSnapshot) {
        return (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
    }
}
