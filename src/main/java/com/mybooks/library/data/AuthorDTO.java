package com.mybooks.library.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthorDTO  extends RepresentationModel<AuthorDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "O primeiro nome não pode estar em branco!")
    @Size(min = 2, max = 50, message = "O primeiro nome deve ter entre 2 e 50 caracteres.")
    private String firstName;

    @NotBlank(message = "O sobrenome não pode estar em branco!")
    @Size(min = 2, max = 80, message = "O sobrenome deve ter entre 2 e 80 caracteres.")
    private String lastName;

    @NotBlank(message = "O país não pode estar em branco!")
    @Size(min = 2, max = 50, message = "O país deve ter entre 2 e 50 caracteres.")
    private String country;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR")
    @PastOrPresent(message = "A data de nascimento não pode ser futura!")
    private LocalDate birthDate;

    @Size(max = 512, message = "A URL da foto não pode exceder 512 caracteres.")
    private String photoUrl;
}
