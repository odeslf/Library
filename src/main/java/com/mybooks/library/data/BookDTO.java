package com.mybooks.library.data;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BookDTO extends RepresentationModel<BookDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "O título não pode estar em branco!")
    @Size(min = 2, max = 100, message = "O título deve ter entre 2 e 100 caracteres.")
    private String title;

    @NotBlank(message = "O gênero não pode estar em branco!")
    @Size(min = 2, max = 20, message = "O gênero deve ter entre 2 e 20 caracteres.")
    private String genre;

    @NotBlank(message = "O ano de publicação não pode estar em branco!")
    @Pattern(regexp = "\\d{4}", message = "O ano de publicação deve ser um número de 4 dígitos.")
    private String publicationYear;

    @NotNull(message = "O número de páginas é obrigatório!")
    @Min(value = 1, message = "O número de páginas deve ser no mínimo 1.")
    private Integer pages;

    @NotNull(message = "O autor é obrigatório!")
    private Long authorId;

    @Size(max = 512, message = "A URL da capa não pode exceder 512 caracteres.")
    private String coverPhotoUrl;
}
