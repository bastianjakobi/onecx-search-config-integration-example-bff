package org.tkit.onecx.search.config.integration.bff.rs.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import gen.org.tkit.onecx.search.config.integration.example.bff.rs.internal.model.ProblemDetailResponseDTO;
import gen.org.tkit.onecx.searchconfig.v1.client.model.ProblemDetailResponse;

@Mapper(uses = { OffsetDateTimeMapper.class })
public interface ProblemDetailMapper {
    @Mapping(target = "removeParamsItem", ignore = true)
    @Mapping(target = "removeInvalidParamsItem", ignore = true)
    ProblemDetailResponseDTO map(ProblemDetailResponse problemDetailResponse);
}
