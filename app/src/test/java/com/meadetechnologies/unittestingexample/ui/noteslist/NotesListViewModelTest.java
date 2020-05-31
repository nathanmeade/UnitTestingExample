package com.meadetechnologies.unittestingexample.ui.noteslist;

import androidx.lifecycle.MutableLiveData;

import com.meadetechnologies.unittestingexample.models.Note;
import com.meadetechnologies.unittestingexample.repository.NoteRepository;
import com.meadetechnologies.unittestingexample.ui.Resource;
import com.meadetechnologies.unittestingexample.util.InstantExecutorExtension;
import com.meadetechnologies.unittestingexample.util.LiveDataTestUtil;
import com.meadetechnologies.unittestingexample.util.TestUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.meadetechnologies.unittestingexample.repository.NoteRepository.DELETE_FAILURE;
import static com.meadetechnologies.unittestingexample.repository.NoteRepository.DELETE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(InstantExecutorExtension.class)
public class NotesListViewModelTest {

    //system under test:
    private NotesListViewModel notesListViewModel;

    @Mock
    private NoteRepository noteRepository;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        notesListViewModel = new NotesListViewModel(noteRepository);
    }

    /*
        retrieve a list of notes
        observe list
        return list
     */

    @Test
    void retrieveNotes_returnNotesList() throws Exception {
        //Arrange
        List<Note> returnedData = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        //Act
        notesListViewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(notesListViewModel.observeNotes());

        //Assert
        assertEquals(returnedData, observedData);
    }
    /*
        retrieve a list of notes
        observe the list
        return empty list
     */

    @Test
    void retrieveNotes_returnEmptyNotesList() throws Exception {
        //Arrange
        List<Note> returnedData = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        //Act
        notesListViewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(notesListViewModel.observeNotes());

        //Assert
        assertEquals(returnedData, observedData);
    }
    /*
        delete note
        observe Resource.success
        return Resource.success
     */

    @Test
    void deleteNote_observeResourceSuccess() throws Exception {
        //Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.success(1, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);

        //Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(notesListViewModel.deleteNote(deletedNote));

        //Assert
        assertEquals(observedValue, returnedData);
    }
    /*
        delete note
        observe Resource.error
        return Resource.error
     */
    @Test
    void deleteNote_observeResourceError() throws Exception {
        //Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.error(null, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);

        //Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(notesListViewModel.deleteNote(deletedNote));

        //Assert
        assertEquals(observedValue, returnedData);
    }
}
