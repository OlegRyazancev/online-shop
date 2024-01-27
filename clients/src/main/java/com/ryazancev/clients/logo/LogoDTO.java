package com.ryazancev.clients.logo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LogoDTO  {

    @JsonIgnore
    @NotNull(message = "Image must be not null.")
    private MultipartFile file;
}
