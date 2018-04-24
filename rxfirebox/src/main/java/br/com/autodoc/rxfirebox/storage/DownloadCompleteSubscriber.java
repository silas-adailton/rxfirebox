package br.com.autodoc.rxfirebox.storage;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;


class DownloadCompleteSubscriber implements CompletableOnSubscribe {

    private final Uri file;
    private final StorageReference storageReference;

    public DownloadCompleteSubscriber(Uri file, StorageReference storageReference) {
        this.file = file;
        this.storageReference = storageReference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        storageReference.putFile(file).addOnCompleteListener(task -> {
                if (task.getException() != null)
                    e.onError(task.getException());
                else if(task.isComplete() && task.isSuccessful())
                    e.onComplete();
        });
    }
}
