package com.shortthirdman.commvault.bdd;

import com.redis.testcontainers.RedisContainer;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("integration")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CommVaultSpringBootTestLoader {
    private static final Logger log = LoggerFactory.getLogger(SpringBootTestLoader.class);

    private static final String DOCKER_REGISTRY = "minestar-docker.artifacts.cat.com";
    private static final String REDIS_IMAGE = DOCKER_REGISTRY + "/minestar/redis-stack:6.2.6-v6";
    private static final int REDIS_PORT = 6379;
//    private static final String ARTEMISMQ_IMAGE = DOCKER_REGISTRY + "/minestar/activemq-artemis:2.28.0";
//    private static final int ARTEMISMQ_PORT = 61616;

    @BeforeAll
    public static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        RedisContainer redisContainer = new RedisContainer(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT);
        redisContainer.start();

        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(REDIS_PORT).toString());

        log.info("Started Redis");

//        GenericContainer<?> artemismqContainer = new GenericContainer<>(ARTEMISMQ_IMAGE)
//                .withExposedPorts(ARTEMISMQ_PORT)
//                .withEnv("ARTEMIS_PASSWORD", "artemis")
//                .withCommand ("run");
//
//        artemismqContainer.start();
//
//        String mqAddress = "tcp://localhost:" + artemismqContainer.getMappedPort(61616);
//
//        System.setProperty("spring.artemis.broker-url", mqAddress);
//        System.setProperty("spring.jms.listener.auto-startup", String.valueOf(true));
//
//        LOGGER.info("Started ArtemisMQ, address {}", mqAddress);
    }
}
