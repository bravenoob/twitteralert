import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IVersionWatcher } from 'app/shared/model/version-watcher.model';
import { Principal } from 'app/core';
import { VersionWatcherService } from './version-watcher.service';

@Component({
    selector: 'jhi-version-watcher',
    templateUrl: './version-watcher.component.html'
})
export class VersionWatcherComponent implements OnInit, OnDestroy {
    versionWatchers: IVersionWatcher[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private versionWatcherService: VersionWatcherService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.versionWatcherService.query().subscribe(
            (res: HttpResponse<IVersionWatcher[]>) => {
                this.versionWatchers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInVersionWatchers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IVersionWatcher) {
        return item.id;
    }

    registerChangeInVersionWatchers() {
        this.eventSubscriber = this.eventManager.subscribe('versionWatcherListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
