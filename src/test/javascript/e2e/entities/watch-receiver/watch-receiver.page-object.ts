import { element, by, promise, ElementFinder } from 'protractor';

export class WatchReceiverComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-watch-receiver div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getText();
    }
}

export class WatchReceiverUpdatePage {
    pageTitle = element(by.id('jhi-watch-receiver-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    emailInput = element(by.id('field_email'));
    channelNameInput = element(by.id('field_channelName'));
    watcherSelect = element(by.id('field_watcher'));

    getPageTitle() {
        return this.pageTitle.getText();
    }

    setEmailInput(email): promise.Promise<void> {
        return this.emailInput.sendKeys(email);
    }

    getEmailInput() {
        return this.emailInput.getAttribute('value');
    }

    setChannelNameInput(channelName): promise.Promise<void> {
        return this.channelNameInput.sendKeys(channelName);
    }

    getChannelNameInput() {
        return this.channelNameInput.getAttribute('value');
    }

    watcherSelectLastOption(): promise.Promise<void> {
        return this.watcherSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    watcherSelectOption(option): promise.Promise<void> {
        return this.watcherSelect.sendKeys(option);
    }

    getWatcherSelect(): ElementFinder {
        return this.watcherSelect;
    }

    getWatcherSelectedOption() {
        return this.watcherSelect.element(by.css('option:checked')).getText();
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
