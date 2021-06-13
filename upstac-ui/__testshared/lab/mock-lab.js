import {mockGet, mockPut, mockServerError} from "../shared/frameworks/mock-http";
import {
    doAssignLabResultBaseUrl,
    doGetMyLabHistoryUrl,
    doGetPendingTestRequestsUrl,
    doUpdateLabResultBaseUrl
} from "../../src/lab/labDispatcher";
import {
    doGetMyLabHistoryResponse,
    doGetPendingTestResponse,
    getAssignLabResultResponseFor,
    getUpdateLabResultResponseFor
} from "./lab-responses";


export function setupMocksForGetMyLabHistory() {

    mockGet(doGetMyLabHistoryUrl, doGetMyLabHistoryResponse)
}

export function setupMockErrorForGetMyLabHistory() {

    mockServerError(doGetMyLabHistoryUrl)
}


export function setupMocksForDoGetPendingTestRequests() {

    mockGet(doGetPendingTestRequestsUrl, doGetPendingTestResponse)
}

export function setupMocksForDoAssignLabResult(id) {

    const inputUrl = doAssignLabResultBaseUrl + id;
    console.log("setupMocksForDoAssignLabResult", setupMocksForDoAssignLabResult)
    mockPut(inputUrl, getAssignLabResultResponseFor(id))
}

export function setupMocksForDoUpdateLabResult(id) {

    mockPut(doUpdateLabResultBaseUrl + id, getUpdateLabResultResponseFor(id))
}
