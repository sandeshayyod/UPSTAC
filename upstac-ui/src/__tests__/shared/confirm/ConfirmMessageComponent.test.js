import {initMockAxios, resetMockAxios} from "../../../../__testshared/shared/frameworks/mock-http";
import {getStoreForAnonymousUser} from "../../../../__testshared/shared/store/mock-store-service";
import {mountComponentWithStoreAndHistoryAndUrl} from "../../../../__testshared/shared/component-helper";
import React from "react";
import ConfirmMessageComponent from "../../../shared/confirm/confirm-message-component";
import {confirmMessageService} from "../../../shared/confirm/confirm-message-service";

describe('ConfirmMessageComponent tests', () => {

    beforeEach(() => {
        initMockAxios();

    });

    afterEach(() => {
        resetMockAxios();
        jest.restoreAllMocks()
    });

    it('ConfirmMessageComponent should render confirm box', async (done) => {

        const store = getStoreForAnonymousUser()
        const mountedComponent = mountComponentWithStoreAndHistoryAndUrl(<ConfirmMessageComponent/>, {store})
        await mountedComponent.waitForDomLoad();

        confirmMessageService.show("halo").then(res => {
            done();
        })
        await mountedComponent.waitForDomLoad();
        const container = mountedComponent.getContainer()
        container.find('#btnConfirm').at(0).simulate('click');
    });

});

