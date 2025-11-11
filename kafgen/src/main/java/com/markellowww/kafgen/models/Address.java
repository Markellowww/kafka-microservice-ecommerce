package com.markellowww.kafgen.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Markelloww
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery address")
public class Address {

    @Schema(
            description = "City",
            example = "Moscow",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String city;

    @Schema(
            description = "Street and building",
            example = "Tverskaya St., Building 15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String street;

    @Schema(
            description = "Zip-code",
            example = "125009",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String zipCode;
}