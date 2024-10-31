package org.xmdf.homelabinventory.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty
    @Size(min = 3, max = 60)
    private String firstName;

    @Size(min = 3, max = 60)
    private String lastName;

    @Email
    @NotEmpty
    @Size(min = 3, max = 90)
    private String email;

    @NotEmpty
    @Size(min = 8, max = 60)
    private String password;
}
