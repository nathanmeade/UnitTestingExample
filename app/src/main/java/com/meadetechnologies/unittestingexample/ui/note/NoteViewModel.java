package com.meadetechnologies.unittestingexample.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meadetechnologies.unittestingexample.models.Note;
import com.meadetechnologies.unittestingexample.repository.NoteRepository;
import com.meadetechnologies.unittestingexample.ui.Resource;
import com.meadetechnologies.unittestingexample.util.DateUtil;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class NoteViewModel extends ViewModel {

    public enum  ViewState {VIEW, EDIT}

    private static final String TAG = "NoteViewModel";
    public static final String NO_CONTENT_ERROR = "Can't save note without content";

    // inject
    private final NoteRepository noteRepository;

    // vars
    private MutableLiveData<Note> note = new MutableLiveData<>();
    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    private boolean isNewNote;
    private Subscription updateSubscription, insertSubscription;

    @Inject
    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<Resource<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.insertNote(note.getValue())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        insertSubscription = subscription;
                    }
                })
        );
    }

    public LiveData<Resource<Integer>> updateNote() throws Exception{
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.updateNote(note.getValue())
                    .doOnSubscribe(new Consumer<Subscription>() {
                        @Override
                        public void accept(Subscription subscription) throws Exception {
                            updateSubscription = subscription;
                        }
                    })
        );
    }

    public LiveData<Note> observeNote(){
        return note;
    }

    public LiveData<ViewState> observeViewState(){
        return viewState;
    }

    public void setViewState(ViewState viewState){
        this.viewState.setValue(viewState);
    }

    public void setIsNewNote(boolean isNewNote){
        this.isNewNote = isNewNote;
    }

    public LiveData<Resource<Integer>> saveNote() throws Exception {
        if (!shouldAllowSave()){
            throw new Exception(NO_CONTENT_ERROR);
        }
        cancelPendingTransactions();

        return new NoteInsertUpdateHelper<Integer>(){

            @Override
            public void setNoteId(int noteId) {
                isNewNote = false;
                Note currentNote = note.getValue();
                currentNote.setId(noteId);
                note.setValue(currentNote);
            }

            @Override
            public LiveData<Resource<Integer>> getAction() throws Exception {
                if (isNewNote){
                    return insertNote();
                }
                return updateNote();
            }

            @Override
            public String defineAction() {
                if (isNewNote){
                    return ACTION_INSERT;
                }
                return ACTION_UPDATE;
            }

            @Override
            public void onTransactionComplete() {
                updateSubscription = null;
                insertSubscription = null;
            }
        }.getAsLiveData();
    }

    public void cancelUpdateTransaction(){
        updateSubscription.cancel();
        updateSubscription = null;
    }

    public void cancelInsertTransaction(){
        insertSubscription.cancel();
        insertSubscription = null;
    }

    private void cancelPendingTransactions(){
        if (insertSubscription != null){
            cancelInsertTransaction();
        }
        if (updateSubscription != null){
            cancelUpdateTransaction();
        }
    }

    private boolean shouldAllowSave(){
        return removeWhiteSpace(note.getValue().getContent()).length() > 0;
    }

    public void updateNote(String title, String content) throws Exception{
        if(title == null || title.equals("")){
            throw new NullPointerException("Title can't be null");
        }
        String temp = removeWhiteSpace(content);
        if(temp.length() > 0){
            Note updatedNote = new Note(note.getValue());
            updatedNote.setTitle(title);
            updatedNote.setContent(content);
            updatedNote.setTimestamp(DateUtil.getCurrentTimeStamp());

            note.setValue(updatedNote);
        }
    }

    private String removeWhiteSpace(String string) {
        string = string.replace("\n", "");
        string = string.replace(" ", "");
        return string;
    }

    public void setNote(Note note) throws Exception{
        if (note.getTitle() == null || note.getTitle().equals("")){
            throw new Exception(NoteRepository.NOTE_TITLE_NULL);
        }
        this.note.setValue(note);
    }

    public boolean shouldNavigateBack() {
        if (viewState.getValue() == ViewState.VIEW){
            return true;
        }
        return false;
    }
}
