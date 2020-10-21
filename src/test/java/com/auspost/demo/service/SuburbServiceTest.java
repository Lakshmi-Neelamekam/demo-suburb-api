package com.auspost.demo.service;

import com.auspost.demo.domain.Suburb;
import com.auspost.demo.domain.SuburbRepository;
import com.auspost.demo.mapper.SuburbMapper;
import com.auspost.demo.model.SuburbDetails;
import com.auspost.demo.validator.PostCodeValidator;
import com.auspost.demo.validator.SuburbValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuburbServiceTest {

    @Mock
    private PostCodeValidator postCodeValidator;
    @Mock
    private SuburbValidator suburbValidator;
    @Mock
    private SuburbMapper mapper;
    @Mock
    private SuburbRepository repository;
    @InjectMocks
    private SuburbService service;

    private List<Suburb> suburbs;
    private Suburb suburb;
    private Suburb savedSuburb;
    private SuburbDetails suburbDetails;
    private static final Integer POST_CODE = 3000;
    private static final String NAME = "Melbourne";
    @BeforeEach
    public void init() {
        suburb = new Suburb();
        suburb.setName(NAME);
        savedSuburb = new Suburb();
        savedSuburb.setName(NAME + "-saved");
        suburbs = Collections.singletonList(suburb);
        suburbDetails = new SuburbDetails("test", POST_CODE, "VIC", "new");
    }

    @Test
    public void shouldInvokeGetByPostCodeSuccessfully() {
        when(repository.findByPostCode(POST_CODE)).thenReturn(suburbs);
        when(mapper.destinationToSource(suburb)).thenReturn(suburbDetails);
        SuburbDetails suburbByPostCode = service.getSuburbByPostCode(POST_CODE);
        assertAll(() -> assertEquals(suburbDetails, suburbByPostCode));
        verify(postCodeValidator, times(1)).validate(POST_CODE);
        verify(suburbValidator, times(0)).validate(any());
        verify(repository, times(1)).findByPostCode(POST_CODE);
        verify(mapper, times(1)).destinationToSource(suburb);
    }

    @Test
    public void shouldInvokeGetByNameSuccessfully() {
        when(repository.findByName(NAME)).thenReturn(suburbs);
        when(mapper.destinationToSource(suburb)).thenReturn(suburbDetails);
        SuburbDetails suburbByName = service.getSuburbByName(NAME);
        assertAll(() -> assertEquals(suburbDetails, suburbByName));
        verify(postCodeValidator, times(0)).validate(any());
        verify(suburbValidator, times(0)).validate(any());
        verify(repository, times(1)).findByName(NAME);
        verify(mapper, times(1)).destinationToSource(suburb);
    }

    @Test
    public void shouldInvokeAddSuburbSuccessfully() {
        SuburbDetails newSuburbDetails = new SuburbDetails();
        when(repository.save(suburb)).thenReturn(savedSuburb);
        when(mapper.destinationToSource(savedSuburb)).thenReturn(newSuburbDetails);
        when(mapper.sourceToDestination(suburbDetails)).thenReturn(suburb);
        SuburbDetails newSuburb = service.addSuburb(suburbDetails);
        assertAll(() -> assertEquals(newSuburbDetails, newSuburb));
        //As we are using a mocked suburbValidator here, postCodeValidator will not be invoked
        verify(postCodeValidator, times(0)).validate(POST_CODE);
        verify(suburbValidator, times(1)).validate(suburbDetails);
        verify(repository, times(1)).save(suburb);
        verify(mapper, times(1)).destinationToSource(savedSuburb);
        verify(mapper, times(1)).sourceToDestination(suburbDetails);
    }
}
