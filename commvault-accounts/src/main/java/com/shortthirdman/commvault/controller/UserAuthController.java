package com.shortthirdman.commvault.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API endpoints to manage user authentication")
@RequestMapping(value = "/api/auth", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROTOBUF_VALUE})
public class UserAuthController {
}
