package com.meadetechnologies.unittestingexample.di;

import android.app.Application;

import androidx.room.Room;

import com.meadetechnologies.unittestingexample.persistence.NoteDao;
import com.meadetechnologies.unittestingexample.persistence.NoteDatabase;
import com.meadetechnologies.unittestingexample.repository.NoteRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.meadetechnologies.unittestingexample.persistence.NoteDatabase.DATABASE_NAME;

@Module
class AppModule {

    @Singleton
    @Provides
    static NoteDatabase provideNoteDatabase(Application application){
        return Room.databaseBuilder(application, NoteDatabase.class, DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    static NoteDao provideNoteDao(NoteDatabase noteDatabase){
        return noteDatabase.getNoteDao();
    }

    @Singleton
    @Provides
    static NoteRepository provideNoteRepository(NoteDao noteDao) {return new NoteRepository(noteDao);
    }
}
