package com.ryazancev.common.dto.logo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ryazancev.common.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Logo DTO")
public class LogoDto {

    @Schema(description = "File to be upload")
    @JsonIgnore
    @NotNull(
            message = "Logo must be not null",
            groups = OnCreate.class
    )
    private MultipartFile file;
}
