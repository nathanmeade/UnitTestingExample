package com.meadetechnologies.unittestingexample;

import android.app.Application;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.meadetechnologies.unittestingexample.models.Note;
import com.meadetechnologies.unittestingexample.persistence.NoteDao;
import com.meadetechnologies.unittestingexample.persistence.NoteDatabase;
import com.meadetechnologies.unittestingexample.util.TestUtil;

import org.junit.After;
import org.junit.Before;

public abstract class NoteDatabaseTest {

    //system under test
    private NoteDatabase noteDatabase;

    public NoteDao getNoteDao(){
        return noteDatabase.getNoteDao();
    }

    @Before
    public void init(){
        noteDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(), NoteDatabase.class
        ).build();
    }

    @After
    public void finish(){
        noteDatabase.close();
    }
}
