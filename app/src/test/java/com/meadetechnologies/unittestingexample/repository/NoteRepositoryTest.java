package com.meadetechnologies.unittestingexample.repository;

import com.meadetechnologies.unittestingexample.persistence.NoteDao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class NoteRepositoryTest {

    // system under test
    private NoteRepository noteRepository;

    @Mock
    private NoteDao noteDao;

    @BeforeEach
    public void initEach(){
        MockitoAnnotations.initMocks(this);
        noteRepository = new NoteRepository(noteDao);
    }

    @Test
    void dummyTest() throws Exception {
        assertNotNull(noteDao);
        assertNotNull(noteRepository);
    }
}
