import { element, by, promise, ElementFinder } from 'protractor';

export class VersionWatcherComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-version-watcher div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getText();
    }
}

export class VersionWatcherUpdatePage {
    pageTitle = element(by.id('jhi-version-watcher-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    watcherNameInput = element(by.id('field_watcherName'));
    activeInput = element(by.id('field_active'));
    userSelect = element(by.id('field_user'));

    getPageTitle() {
        return this.pageTitle.getText();
    }

    setWatcherNameInput(watcherName): promise.Promise<void> {
        return this.watcherNameInput.sendKeys(watcherName);
    }

    getWatcherNameInput() {
        return this.watcherNameInput.getAttribute('value');
    }

    getActiveInput() {
        return this.activeInput;
    }
    userSelectLastOption(): promise.Promise<void> {
        return this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    userSelectOption(option): promise.Promise<void> {
        return this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    save(): promise.Promise<void> {
        return this.saveButton.click();
    }

    cancel(): promise.Promise<void> {
        return this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}
