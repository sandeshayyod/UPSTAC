package org.upgrad.upstac.testrequests.lab;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/labrequests")
public class LabRequestController {

    Logger log = LoggerFactory.getLogger(LabRequestController.class);

    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;

    @Autowired
    private TestRequestFlowService testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;

    @GetMapping("/to-be-tested")
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTests() {
        log.info("getting requests which are ready for testing");
        return testRequestQueryService.findBy(RequestStatus.INITIATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTester() {
        User tester = userLoggedInService.getLoggedInUser();
        log.info("getting requests to be tested by the tester : {}", tester.getFirstName());
        return testRequestQueryService.findByTester(tester);
    }

    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForLabTest(@PathVariable Long id) {
        User tester = userLoggedInService.getLoggedInUser();
        log.info("Assigning request with Id {} for lab testing by the tester : {}", id, tester.getFirstName());
        return testRequestUpdateService.assignForLabTest(id, tester);
    }

    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/update/{id}")
    public TestRequest updateLabTest(@PathVariable Long id, @RequestBody CreateLabResult createLabResult) {
        try {
            User tester = userLoggedInService.getLoggedInUser();
            log.info("Updating request with Id {} with lab test by the tester : {}", id, tester.getFirstName());
            return testRequestUpdateService.updateLabTest(id, createLabResult, tester);
        } catch (ConstraintViolationException e) {
            log.error("Exception occurred while updating lab test", e);
            throw asConstraintViolation(e);
        } catch (AppException e) {
            log.error("Exception occurred while updating lab test", e);
            throw asBadRequest(e.getMessage());
        }
    }
}
