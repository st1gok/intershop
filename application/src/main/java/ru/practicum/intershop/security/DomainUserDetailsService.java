package ru.practicum.intershop.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.entities.Authority;
import ru.practicum.intershop.entities.User;
import ru.practicum.intershop.repositories.UserAuthorityRepository;
import ru.practicum.intershop.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class DomainUserDetailsService implements ReactiveUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private final UserAuthorityRepository authorityRepository;

    public DomainUserDetailsService(UserRepository userRepository, UserAuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserDetails> findByUsername(String username) {
        LOG.debug("Authenticating {}", username);

        String lowercaseLogin = username.toLowerCase();
                return userRepository.findOneByLogin(lowercaseLogin)
                        .switchIfEmpty(Mono.defer(() -> {
                            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
                        }))
                .zipWhen(user -> authorityRepository.findByUserId(user.getId()).collectList()).map(t -> {
                    t.getT1().setAuthorities(t.getT2().stream().map(userAuthority -> Authority.builder().name(userAuthority.getAuthorityName()).build()).collect(Collectors.toSet()));
                    return createSpringSecurityUser(lowercaseLogin, t.getT1());
                });
    }

    private UserDetails createSpringSecurityUser(String lowercaseLogin, User user) {
        List<SimpleGrantedAuthority> grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(Authority::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
    }

}
