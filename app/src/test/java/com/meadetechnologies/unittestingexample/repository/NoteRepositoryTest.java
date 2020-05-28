package com.meadetechnologies.unittestingexample.repository;

import com.meadetechnologies.unittestingexample.models.Note;
import com.meadetechnologies.unittestingexample.persistence.NoteDao;
import com.meadetechnologies.unittestingexample.ui.Resource;
import com.meadetechnologies.unittestingexample.util.TestUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
        assertEquals(Resource.success(1, NoteRepository.INSERT_SUCCESS), returnedValue);

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

        assertEquals(Resource.error(null, NoteRepository.INSERT_FAILURE), returnedValue);
    }

    /*
        insert note
        null title
        confirm throw exception
     */
    @Test
    void insertNote_nullTitle_throwException() throws Exception {
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.insertNote(note);
            }
        });
    }
}
