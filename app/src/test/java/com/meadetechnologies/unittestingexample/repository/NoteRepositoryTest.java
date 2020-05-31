package com.meadetechnologies.unittestingexample.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.meadetechnologies.unittestingexample.models.Note;
import com.meadetechnologies.unittestingexample.persistence.NoteDao;
import com.meadetechnologies.unittestingexample.ui.Resource;
import com.meadetechnologies.unittestingexample.util.InstantExecutorExtension;
import com.meadetechnologies.unittestingexample.util.LiveDataTestUtil;
import com.meadetechnologies.unittestingexample.util.TestUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static com.meadetechnologies.unittestingexample.repository.NoteRepository.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NoteRepositoryTest {

    private static final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);

    // system under test
    private NoteRepository noteRepository;

    @Mock
    private NoteDao noteDao;

    @BeforeEach
    public void initEach(){
        MockitoAnnotations.initMocks(this);
        noteRepository = new NoteRepository(noteDao);
    }

    /*
        insert note
        verify the correct method is called
        confirm observer is triggered
        confirm new rows inserted
     */
    @Test
    void insertNote_returnRow() throws Exception {
        //Arrange
        final Long insertedRow = 1L;
        final Single<Long> returnedDate = Single.just(insertedRow);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedDate);

        //Act
        final Resource<Integer> returnedValue = noteRepository.insertNote(NOTE1).blockingFirst();

        //Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        System.out.println("Returned value" + returnedValue.data);
        assertEquals(Resource.success(1, INSERT_SUCCESS), returnedValue);

        //Same test but with RxJava:
/*        noteRepository.insertNote(NOTE1)
                .test()
                .await()
                .assertValue(Resource.success(1, NoteRepository.INSERT_SUCCESS));*/
    }

    /*
        insert note
        failure (return -1)
     */
    @Test
    void insertNote_returnError() throws Exception {
        //Arrange
        final Long failedInsert = -1L;
        final Single<Long> returnedDate = Single.just(failedInsert);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedDate);

        //Act
        final Resource<Integer> returnedValue = noteRepository.insertNote(NOTE1).blockingFirst();

        //Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.error(null, INSERT_FAILURE), returnedValue);
    }

    /*
        insert note
        null title
        confirm throw exception
     */
    @Test
    void insertNote_nullTitle_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.insertNote(note);
            }
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    /*
        update note
        verify correct method is called
        confirm observer is triggered
        confirm number of rows updated
     */

    @Test
    void updateNote_returnNumRowsUpdated() throws Exception {
        //Arrange
        final int updatedRow = 1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(updatedRow));

        //Act
        final Resource<Integer> returnedValue = noteRepository.updateNote(NOTE1).blockingFirst();

        //Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }
/*
        update note
        failure (-1)
     */

    @Test
    void updateNote_returnFailure() throws Exception {
        //Arrange
        final int failedInsert = -1;
        final Single<Integer> returnedData = Single.just(failedInsert);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        //Act
        final Resource<Integer> returnedValue = noteRepository.updateNote(NOTE1).blockingFirst();

        //Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.error(failedInsert, UPDATE_FAILURE), returnedValue);
    }
/*
        update note
        null title
        throw exception
     */

    @Test
    void updateNote_nullTitle_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.updateNote(note);
            }
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    /*
        delete note
        null id
        throw exception
     */

    @Test
    void deleteNote_nullId_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setId(-1);
                noteRepository.deleteNote(note);
            }
        });

        assertEquals(exception.getMessage(), INVALID_NOTE_ID);
    }
    /*
        delete note
        delete success
        return Resource.success with deleted row
     */

    @Test
    void deleteNote_deleteSuccess_returnResourceSuccess() throws Exception {
        //Arrange
        final int deletedRow = 1;
        Resource<Integer> successResponse = Resource.success(deletedRow, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        //Act
        Resource<Integer> observedResource = liveDataTestUtil.getValue(noteRepository.deleteNote(NOTE1));

        //Assert
        assertEquals(observedResource, successResponse);
    }
    /*
        delete note
        delete failure
        return Resource.error
     */
    @Test
    void deleteNote_deleteFailure_returnResourceSuccess() throws Exception {
        //Arrange
        final int deletedRow = -1;
        Resource<Integer> failureResponse = Resource.error(deletedRow, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        //Act
        Resource<Integer> observedResource = liveDataTestUtil.getValue(noteRepository.deleteNote(NOTE1));

        //Assert
        assertEquals(observedResource, failureResponse);
    }

    /*
        retrieve notes
        return list of notes
     */

    @Test
    void getNotes_returnListWithNotes() throws Exception {
        //Arrange
        List<Note> notes = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        //Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());

        //Assert
        assertEquals(observedData, returnedData.getValue());
    }
    /*
        retrieve notes
        return empty list
     */

    @Test
    void getNotes_returnEmptyList() throws Exception {
        //Arrange
        List<Note> notes = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        //Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());

        //Assert
        assertEquals(observedData, returnedData.getValue());
    }
}
