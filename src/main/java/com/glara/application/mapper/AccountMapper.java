package com.glara.application.mapper;


import com.glara.application.dto.AccountDTO;
import com.glara.domain.entity.Account;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    @Mapping(target = "accountTypeId", source = "accountType.id")
    @Mapping(target = "userId", source = "user.id")
    AccountDTO toDTO(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountType.id", source = "accountTypeId")
    @Mapping(target = "user.id", source = "userId")
    Account toEntity(AccountDTO dto);
}
