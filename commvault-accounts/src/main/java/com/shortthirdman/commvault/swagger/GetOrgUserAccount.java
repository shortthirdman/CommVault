// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.shortthirdman.commvault.common.CommVaultSwaggerConstants.*;

@Operation(summary = "Retrieves user account profiles by organization", tags = {"Accounts"})
@ApiResponses(value = {
        @ApiResponse(responseCode = RESPONSE_CODE_200, description = "Successful retrieval of user accounts by organization",
                useReturnTypeSchema = true, content = {}),
        @ApiResponse(responseCode = RESPONSE_CODE_500, description = INTERNAL_SERVER_ERROR, useReturnTypeSchema = true, content = {}),
        @ApiResponse(responseCode = RESPONSE_CODE_400, description = RESOURCE_NOT_FOUND_ERROR, useReturnTypeSchema = true, content = {}),
        @ApiResponse(responseCode = RESPONSE_CODE_401, description = UNAUTHORIZED, useReturnTypeSchema = true, content = {}),
        @ApiResponse(responseCode = RESPONSE_CODE_403, description = FORBIDDEN, useReturnTypeSchema = true, content = {})
})
@Parameters({
        @Parameter(name = "orgId", in = ParameterIn.PATH, description = "Organization ID")
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetOrgUserAccount {
}
