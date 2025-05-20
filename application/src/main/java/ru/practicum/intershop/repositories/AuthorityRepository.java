package ru.practicum.intershop.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.intershop.entities.Authority;

@Repository
public interface AuthorityRepository extends R2dbcRepository<Authority, Long> {

}
