package com.auspost.demo.service;

import com.auspost.demo.domain.Suburb;
import com.auspost.demo.domain.SuburbRepository;
import com.auspost.demo.mapper.SuburbMapper;
import com.auspost.demo.model.SuburbDetails;
import com.auspost.demo.validator.PostCodeValidator;
import com.auspost.demo.validator.SuburbValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SuburbService {
    @Autowired
    private PostCodeValidator postCodeValidator;
    @Autowired
    private SuburbValidator suburbValidator;
    @Autowired
    private SuburbMapper mapper;
    @Autowired
    private SuburbRepository repository;

    private static Logger logger = LoggerFactory.getLogger(SuburbService.class);
    public SuburbDetails getSuburbByPostCode(Integer postCode) {
        postCodeValidator.validate(postCode);
        logger.info("Getting suburb information for postcode {}", postCode);
        List<Suburb> suburbs = repository.findByPostCode(postCode);
        logger.info("Retrieved {} suburbs for postCode {}", suburbs.size(), postCode);
        return mapper.destinationToSource(suburbs.stream().findFirst().orElse(null));
    }

    public SuburbDetails getSuburbByName(String name) {
        logger.info("Getting postcode information for suburb {}", name);
        List<Suburb> suburbs = repository.findByName(name);
        logger.info("Retrieved {} suburbs for name {}", suburbs.size(), name);
        return mapper.destinationToSource(suburbs.stream().findFirst().orElse(null));
    }

    public SuburbDetails addSuburb(SuburbDetails suburb) {
        suburbValidator.validate(suburb);
        logger.info("Adding suburb information for {}", suburb);
        Suburb suburbToSave = mapper.sourceToDestination(suburb);
        Suburb savedSuburb = repository.save(suburbToSave);
        logger.info("Suburb {} with postCode {} saved ", savedSuburb.getName(), savedSuburb.getPostCode());
        return mapper.destinationToSource(savedSuburb);
    }
}
