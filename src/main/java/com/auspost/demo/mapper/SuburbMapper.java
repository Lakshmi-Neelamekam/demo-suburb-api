package com.auspost.demo.mapper;

import com.auspost.demo.domain.Suburb;
import com.auspost.demo.model.SuburbDetails;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SuburbMapper {
    Suburb sourceToDestination(SuburbDetails source);
    SuburbDetails destinationToSource(Suburb suburb);
}
