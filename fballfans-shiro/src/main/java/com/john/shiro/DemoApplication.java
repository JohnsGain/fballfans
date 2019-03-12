package com.john.shiro;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.process.runtime.connector.Connector;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjuwa
 * @date 2019/2/25
 * @since jdk1.8
 */
@SpringBootApplication
@RestController
public class DemoApplication {


    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

    private ProcessRuntime processRuntime;

    public DemoApplication(ProcessRuntime processRuntime) {
        this.processRuntime = processRuntime;
    }

    public static void main(String[] args) {
        //ProcessEngineConfigurationImpl
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/process-definitions")
    public List<ProcessDefinition> getProcessDefinition() {
        return processRuntime.processDefinitions(Pageable.of(0, 100))
                .getContent();
    }

    @PostMapping("/documents")
    public String processFile(@RequestBody String content) {
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("categorizeProcess")
                .withVariable("fileContent", content).build());
        String message = ">>> Created Process Instance: " + processInstance;
        LOGGER.info(message);
        return message;
    }

    @Bean
    public Connector processTextConnector() {
        return item -> {
            Map<String, Object> inBoundVariables = item.getInBoundVariables();
            String fileContent = (String) inBoundVariables.get("fileContent");
            if (fileContent.contains("activiti")) {
                item.addOutBoundVariable("approved", true);
            } else {
                item.addOutBoundVariable("approved", false);
            }
            return item;
        };
    }

    @Bean
    public Connector tagTextConnector() {
        return item -> {
            String contentToTag = (String) item.getInBoundVariables().get("fileContent");
            contentToTag += " :) ";
            item.addOutBoundVariable("fileContent", contentToTag);
            LOGGER.info("Final Content: {}", contentToTag);
            return item;
        };
    }

    @Bean
    public Connector discardTextConnector() {
        return item -> {
            String contentToDiscard = (String) item.getInBoundVariables().get("fileContent");
            contentToDiscard += " :( ";
            item.addOutBoundVariable("fileContent", contentToDiscard);
            LOGGER.info("Final Content: {}", contentToDiscard);
            return item;
        };
    }
}
