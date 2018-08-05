import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { WatchChannelComponentsPage, WatchChannelUpdatePage } from './watch-channel.page-object';

describe('WatchChannel e2e test', () => {
    let navBarPage: NavBarPage;
    let watchChannelUpdatePage: WatchChannelUpdatePage;
    let watchChannelComponentsPage: WatchChannelComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load WatchChannels', () => {
        navBarPage.goToEntity('watch-channel');
        watchChannelComponentsPage = new WatchChannelComponentsPage();
        expect(watchChannelComponentsPage.getTitle()).toMatch(/Watch Channels/);
    });

    it('should load create WatchChannel page', () => {
        watchChannelComponentsPage.clickOnCreateButton();
        watchChannelUpdatePage = new WatchChannelUpdatePage();
        expect(watchChannelUpdatePage.getPageTitle()).toMatch(/Create or edit a Watch Channel/);
        watchChannelUpdatePage.cancel();
    });

    it('should create and save WatchChannels', () => {
        watchChannelComponentsPage.clickOnCreateButton();
        watchChannelUpdatePage.setChannelNameInput('channelName');
        expect(watchChannelUpdatePage.getChannelNameInput()).toMatch('channelName');
        watchChannelUpdatePage.watcherSelectLastOption();
        watchChannelUpdatePage.save();
        expect(watchChannelUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
