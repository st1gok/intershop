package ru.practicum.intershop.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Objects;

@Data
@Table(name = "user_authority")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String authorityName;
    private Long userId;

}
