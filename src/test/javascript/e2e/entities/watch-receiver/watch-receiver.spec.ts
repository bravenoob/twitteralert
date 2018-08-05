import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { WatchReceiverComponentsPage, WatchReceiverUpdatePage } from './watch-receiver.page-object';

describe('WatchReceiver e2e test', () => {
    let navBarPage: NavBarPage;
    let watchReceiverUpdatePage: WatchReceiverUpdatePage;
    let watchReceiverComponentsPage: WatchReceiverComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load WatchReceivers', () => {
        navBarPage.goToEntity('watch-receiver');
        watchReceiverComponentsPage = new WatchReceiverComponentsPage();
        expect(watchReceiverComponentsPage.getTitle()).toMatch(/Watch Receivers/);
    });

    it('should load create WatchReceiver page', () => {
        watchReceiverComponentsPage.clickOnCreateButton();
        watchReceiverUpdatePage = new WatchReceiverUpdatePage();
        expect(watchReceiverUpdatePage.getPageTitle()).toMatch(/Create or edit a Watch Receiver/);
        watchReceiverUpdatePage.cancel();
    });

    it('should create and save WatchReceivers', () => {
        watchReceiverComponentsPage.clickOnCreateButton();
        watchReceiverUpdatePage.setEmailInput('email');
        expect(watchReceiverUpdatePage.getEmailInput()).toMatch('email');
        watchReceiverUpdatePage.setChannelNameInput('channelName');
        expect(watchReceiverUpdatePage.getChannelNameInput()).toMatch('channelName');
        watchReceiverUpdatePage.watcherSelectLastOption();
        watchReceiverUpdatePage.save();
        expect(watchReceiverUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
