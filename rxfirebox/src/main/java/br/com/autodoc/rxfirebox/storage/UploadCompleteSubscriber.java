package br.com.autodoc.rxfirebox.storage;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;


class UploadCompleteSubscriber implements CompletableOnSubscribe {

    private final Uri file;
    private final StorageReference storageReference;

    public UploadCompleteSubscriber(Uri file, StorageReference storageReference) {
        this.file = file;
        this.storageReference = storageReference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        storageReference.getFile(file).addOnCompleteListener(task -> {
                if (task.getException() != null)
                    e.onError(task.getException());
                else if(task.isComplete() && task.isSuccessful())
                    e.onComplete();
        });
    }
}
