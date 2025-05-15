package uz.alifservice.service.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.alifservice.criteria.auth.RoleCriteria;
import uz.alifservice.domain.auth.Role;
import uz.alifservice.dto.auth.role.RoleCrudDto;
import uz.alifservice.enums.AppLanguage;
import uz.alifservice.mapper.auth.RoleMapper;
import uz.alifservice.repository.auth.RoleRepository;
import uz.alifservice.service.GenericCrudService;

@Service
@RequiredArgsConstructor
public class RoleService implements GenericCrudService<Role, RoleCrudDto, RoleCriteria> {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Role get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> list(RoleCriteria criteria) {
        return repository.findAll(criteria.toSpecification(),
                PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(criteria.getDirection(), criteria.getSort())));
    }

    @Override
    @Transactional
    public Role create(RoleCrudDto dto) {
        return repository.save(mapper.fromCreateDto(dto));
    }

    @Override
    @Transactional
    public Role update(Long id, RoleCrudDto dto) {
        Role entity = get(id);
        return repository.save(mapper.fromUpdate(dto, entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Role entity = get(id);
        entity.setDeleted(true);
    }
}
