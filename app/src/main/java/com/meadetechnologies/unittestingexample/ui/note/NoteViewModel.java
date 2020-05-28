package com.meadetechnologies.unittestingexample.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meadetechnologies.unittestingexample.models.Note;
import com.meadetechnologies.unittestingexample.repository.NoteRepository;
import com.meadetechnologies.unittestingexample.ui.Resource;

import javax.inject.Inject;

public class NoteViewModel extends ViewModel {

    private static final String TAG = "NoteViewModel";

    // inject
    private final NoteRepository noteRepository;

    // vars
    private MutableLiveData<Note> note = new MutableLiveData<>();

    @Inject
    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<Resource<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.insertNote(note.getValue())
        );
    }

    public LiveData<Note> observeNote(){
        return note;
    }

    public void setNote(Note note) throws Exception{
        if (note.getTitle() == null || note.getTitle().equals("")){
            throw new Exception(NoteRepository.NOTE_TITLE_NULL);
        }
        this.note.setValue(note);
    }
}
