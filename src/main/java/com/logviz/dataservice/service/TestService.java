package com.logViz.dataservice.service;

import com.logViz.dataservice.repository.TestRepository;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final TestRepository testRepository;


    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public int test(){
        return testRepository.test();
    }
}
