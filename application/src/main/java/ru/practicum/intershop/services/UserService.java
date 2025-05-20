package ru.practicum.intershop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.entities.Authority;
import ru.practicum.intershop.entities.User;
import ru.practicum.intershop.repositories.AuthorityRepository;
import ru.practicum.intershop.repositories.UserAuthorityRepository;
import ru.practicum.intershop.repositories.UserRepository;

import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    public UserService(
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            UserAuthorityRepository userAuthorityRepository
            ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userAuthorityRepository = userAuthorityRepository;
    }


    @Transactional(readOnly = true)
    public Mono<User> getUserWithAuthorities() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> extractPrincipal(securityContext.getAuthentication()))
                .flatMap(userRepository::findOneByLogin).map(user -> {
                    userAuthorityRepository.findByUserId(user.getId()).collectList().flatMap(userAuthorities -> {
                        user.setAuthorities(userAuthorities.stream().map(userAuthority -> Authority.builder().name(userAuthority.getAuthorityName()).build()).collect(Collectors.toSet()));
                        return Mono.just(user);
                    });
                    return user;
                });
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Flux<String> getAuthorities() {
        return authorityRepository.findAll().map(Authority::getName);
    }
}

