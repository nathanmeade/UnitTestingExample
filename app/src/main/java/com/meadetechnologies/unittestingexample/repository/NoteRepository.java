package com.meadetechnologies.unittestingexample.repository;

import androidx.annotation.NonNull;

import com.meadetechnologies.unittestingexample.persistence.NoteDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NoteRepository {

    // inject
    @NonNull
    private final NoteDao noteDao;

    @Inject
    public NoteRepository(@NonNull NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}
