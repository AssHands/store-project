package com.ak.store.user.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WriteUserCommand {
    private UUID userId;

    private String name;

    private String email;

    private String password;
}
