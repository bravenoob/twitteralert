import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { VersionWatcherComponentsPage, VersionWatcherUpdatePage } from './version-watcher.page-object';

describe('VersionWatcher e2e test', () => {
    let navBarPage: NavBarPage;
    let versionWatcherUpdatePage: VersionWatcherUpdatePage;
    let versionWatcherComponentsPage: VersionWatcherComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load VersionWatchers', () => {
        navBarPage.goToEntity('version-watcher');
        versionWatcherComponentsPage = new VersionWatcherComponentsPage();
        expect(versionWatcherComponentsPage.getTitle()).toMatch(/Version Watchers/);
    });

    it('should load create VersionWatcher page', () => {
        versionWatcherComponentsPage.clickOnCreateButton();
        versionWatcherUpdatePage = new VersionWatcherUpdatePage();
        expect(versionWatcherUpdatePage.getPageTitle()).toMatch(/Create or edit a Version Watcher/);
        versionWatcherUpdatePage.cancel();
    });

    it('should create and save VersionWatchers', () => {
        versionWatcherComponentsPage.clickOnCreateButton();
        versionWatcherUpdatePage.setWatcherNameInput('watcherName');
        expect(versionWatcherUpdatePage.getWatcherNameInput()).toMatch('watcherName');
        versionWatcherUpdatePage
            .getActiveInput()
            .isSelected()
            .then(selected => {
                if (selected) {
                    versionWatcherUpdatePage.getActiveInput().click();
                    expect(versionWatcherUpdatePage.getActiveInput().isSelected()).toBeFalsy();
                } else {
                    versionWatcherUpdatePage.getActiveInput().click();
                    expect(versionWatcherUpdatePage.getActiveInput().isSelected()).toBeTruthy();
                }
            });
        versionWatcherUpdatePage.userSelectLastOption();
        versionWatcherUpdatePage.save();
        expect(versionWatcherUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
