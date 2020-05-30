package com.meadetechnologies.unittestingexample.di;

import com.meadetechnologies.unittestingexample.ui.note.NoteActivity;
import com.meadetechnologies.unittestingexample.ui.noteslist.NotesListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract NotesListActivity contributeNotesListActivity();

    @ContributesAndroidInjector
    abstract NoteActivity contributeNoteActivity();
}
