package uz.alifservice.service;

import org.springframework.data.domain.Page;
import uz.alifservice.criteria.GenericCriteria;

public interface GenericService<T, E extends GenericCriteria> {

    T get(Long id);

    Page<T> list(E criteria);
}
