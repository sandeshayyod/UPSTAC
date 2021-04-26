package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.consultation.Consultation;
import org.upgrad.upstac.testrequests.consultation.ConsultationService;
import org.upgrad.upstac.testrequests.consultation.CreateConsultationRequest;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.testrequests.lab.LabResultService;
import org.upgrad.upstac.users.User;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@Slf4j
@Validated
public class TestRequestUpdateService {

    @Autowired
    private TestRequestRepository testRequestRepository;


    @Autowired
    private TestRequestFlowService testRequestFlowService;


    @Autowired
    private LabResultService labResultService;


    @Autowired
    private ConsultationService consultationService;


    @Transactional
    public TestRequest saveTestRequest(@Valid TestRequest result) {
        return testRequestRepository.save(result);
    }

    TestRequest updateStatusAndSave(TestRequest testRequest, RequestStatus status) {
        testRequest.setStatus(status);
        return saveTestRequest(testRequest);
    }

    /**
     * Assign the lab test for the given id and for given tester
     * @param id
     * @param tester
     * @return
     */
    public TestRequest assignForLabTest(Long id, User tester) {
        TestRequest testRequest = testRequestRepository.findByRequestIdAndStatus(id, RequestStatus.INITIATED).orElseThrow(() -> new AppException("Invalid ID"));
        LabResult labResult = labResultService.assignForLabTest(testRequest, tester);
        testRequestFlowService.log(testRequest, RequestStatus.INITIATED, RequestStatus.LAB_TEST_IN_PROGRESS, tester);
        testRequest.setLabResult(labResult);
        return updateStatusAndSave(testRequest, RequestStatus.LAB_TEST_IN_PROGRESS);
    }

    /**
     * method to update lab test based for the given id and test details along with tester info
     * @param id
     * @param createLabResult
     * @param tester
     * @return
     */
    public TestRequest updateLabTest(Long id, @Valid CreateLabResult createLabResult, User tester) {
        TestRequest testRequest = testRequestRepository.findByRequestIdAndStatus(id, RequestStatus.LAB_TEST_IN_PROGRESS).orElseThrow(() -> new AppException("Invalid ID or State"));
        labResultService.updateLabTest(testRequest, createLabResult);
        testRequestFlowService.log(testRequest, RequestStatus.LAB_TEST_IN_PROGRESS, RequestStatus.LAB_TEST_COMPLETED, tester);
        return updateStatusAndSave(testRequest, RequestStatus.LAB_TEST_COMPLETED);
    }

    /**
     * Assign the test request with given id for the given user
     * @param id
     * @param doctor
     * @return
     */
    public TestRequest assignForConsultation(Long id, User doctor) {
        TestRequest testRequest = testRequestRepository.findByRequestIdAndStatus(id, RequestStatus.LAB_TEST_COMPLETED).orElseThrow(() -> new AppException("Invalid ID or State"));
        Consultation consultation = consultationService.assignForConsultation(testRequest, doctor);
        testRequestFlowService.log(testRequest, RequestStatus.LAB_TEST_COMPLETED, RequestStatus.DIAGNOSIS_IN_PROCESS, doctor);
        testRequest.setConsultation(consultation);
        return updateStatusAndSave(testRequest, RequestStatus.DIAGNOSIS_IN_PROCESS);
    }

    /**
     * update the consultation details for the given test id with given consultation details and for given doctor
     * @param id
     * @param createConsultationRequest
     * @param doctor
     * @return
     */
    public TestRequest updateConsultation(Long id, @Valid CreateConsultationRequest createConsultationRequest, User doctor) {
        TestRequest testRequest = testRequestRepository.findByRequestIdAndStatus(id, RequestStatus.DIAGNOSIS_IN_PROCESS).orElseThrow(() -> new AppException("Invalid ID or State"));
        consultationService.updateConsultation(testRequest, createConsultationRequest);
        testRequestFlowService.log(testRequest, RequestStatus.DIAGNOSIS_IN_PROCESS, RequestStatus.COMPLETED, doctor);
        return updateStatusAndSave(testRequest, RequestStatus.COMPLETED);
    }


}
