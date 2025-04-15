package com.shortthirdman.commvault.bdd;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Transactional
public class ScenarioSteps extends BaseScenario {
    private static final Logger log = LoggerFactory.getLogger(ScenarioSteps.class);

    private final RedissonClient redissonClient;
    private static final String BASE_URL = "http://localhost:8090/api/optimization/restrictions/compatibilities";

    public ScenarioSteps(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}