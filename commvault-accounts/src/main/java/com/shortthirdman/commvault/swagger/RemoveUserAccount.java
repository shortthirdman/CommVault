// Copyright (c) ShortThirdMan 2025. All rights reserved.

package com.shortthirdman.commvault.swagger;

import com.shortthirdman.commvault.dto.CommVaultApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.shortthirdman.commvault.common.CommVaultSwaggerConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ApiResponses(value = {
        @ApiResponse(responseCode = RESPONSE_CODE_200, description = "Successful removal of existing user account", content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommVaultApiResponse.class))
        }),
        @ApiResponse(responseCode = RESPONSE_CODE_500, description = INTERNAL_SERVER_ERROR, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommVaultApiResponse.class))
        }),
        @ApiResponse(responseCode = RESPONSE_CODE_400, description = RESOURCE_NOT_FOUND_ERROR, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommVaultApiResponse.class))
        }),
        @ApiResponse(responseCode = RESPONSE_CODE_401, description = UNAUTHORIZED, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommVaultApiResponse.class))
        }),
        @ApiResponse(responseCode = RESPONSE_CODE_403, description = FORBIDDEN, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommVaultApiResponse.class))
        })
})
@Operation(summary = "Delete user account details", tags = {"Accounts"})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoveUserAccount {
}
