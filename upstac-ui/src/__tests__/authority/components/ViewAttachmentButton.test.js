import React from 'react';
import IconButton from "@material-ui/core/IconButton";
import {initMockAxios, resetMockAxios} from "../../../../__testshared/shared/frameworks/mock-http";
import {mountComponentWithStoreAndHistory} from "../../../../__testshared/shared/component-helper";
import {getStoreForGovernmentAuthorityWithPendingUsersAndThresholds} from "../../../../__testshared/shared/store/mock-store-service";
import {setupMocksForDownloadDocument} from "../../../../__testshared/shared/api/mock-authority";
import {mockNotificationAndConfirmationModules} from "../../../../__testshared/shared/frameworks/mock-notification";
import http from "../../../shared/services/http-service";
import {ViewAttachmentButton} from "../../../authority/components/ViewAttachmentButton";

function getSelectedUserOn(store, dataIndex) {
    return (store.getState().authority.pendingUsers)[dataIndex];
}

describe('View Attachment Button tests', () => {

    beforeEach(() => {
        initMockAxios();
        mockNotificationAndConfirmationModules();

    });

    afterEach(() => {
        resetMockAxios();
        jest.restoreAllMocks()
    });


    it('should download file , if view attachment button gets clicked', async () => {


        const downloadFileFromServerSpy = jest.spyOn(http, 'downloadFileFromServer')


        const dataIndex = 1;
        const mountedComponent = mountComponentWithStoreAndHistory(<ViewAttachmentButton dataIndex={dataIndex}/>,
            getStoreForGovernmentAuthorityWithPendingUsersAndThresholds())


        await mountedComponent.waitForDomLoad();
        const selectedUser = getSelectedUserOn(mountedComponent.getStore(), dataIndex);
        setupMocksForDownloadDocument(selectedUser.id)

        const container = mountedComponent.getContainer()
        container.find(IconButton).at(0).simulate('click');


        mountedComponent.verifyOnComplete(() => {

            expect(downloadFileFromServerSpy).toBeCalled()


        })


    });


});

