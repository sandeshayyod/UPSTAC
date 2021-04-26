package org.upgrad.upstac.testrequests;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.flow.TestRequestFlow;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
public class TestRequestController {

    Logger log = LoggerFactory.getLogger(TestRequestController.class);


    @Autowired
    private TestRequestService testRequestService;

    @Autowired
    private UserLoggedInService userLoggedInService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;

    @Autowired
    private TestRequestFlowService testRequestFlowService;


    @PostMapping("/api/testrequests")
    public TestRequest createRequest(@RequestBody CreateTestRequest testRequest) {
        try {
            User user = userLoggedInService.getLoggedInUser();
            log.info("Creating request by patient {}", user.getFirstName());
            TestRequest result = testRequestService.createTestRequestFrom(user, testRequest);
            return result;
        } catch (AppException e) {
            log.error("Exception occurred while creating request ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @GetMapping("/api/testrequests")
    public List<TestRequest> requestHistory() {
        User user = userLoggedInService.getLoggedInUser();
        log.info("getting request history by patient {}", user.getFirstName());
        return testRequestService.getHistoryFor(user);
    }

    @GetMapping("/api/testrequests/{id}")
    public Optional<TestRequest> getById(@PathVariable Long id) {
        log.info("getting request details for test with ID : {}", id);
        return testRequestQueryService.getTestRequestById(id);
    }

    @GetMapping("/api/testrequests/flow/{id}")
    public List<TestRequestFlow> getFlowById(@PathVariable Long id) {
        Optional<TestRequest> testRequest = testRequestQueryService.getTestRequestById(id);
        if (testRequest.isPresent()) {
            log.info("getting request flow details for test flow with test request ID : {}", id);
            return testRequestFlowService.findByRequest(testRequest.get());
        } else {
            return Collections.emptyList();
        }
    }

}
