package uz.alifservice.service;

import org.springframework.data.domain.Page;
import uz.alifservice.criteria.GenericCriteria;
import uz.alifservice.domain.Auditable;
import uz.alifservice.dto.GenericCrudDto;

public interface GenericCrudService<T extends Auditable<Long>, C extends GenericCrudDto, E extends GenericCriteria> {

    T get(Long id);

    Page<T> list(E criteria);

    T create(C dto);

    T update(Long id, C dto);

    void delete(Long id);
}
