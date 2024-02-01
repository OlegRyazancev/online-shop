package com.ryazancev.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ryazancev.validation.OnCreate;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LogoDTO {

    @JsonIgnore
    @NotNull(message = "Logo must be not null",
            groups = OnCreate.class)
    private MultipartFile file;
}
