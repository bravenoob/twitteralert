import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IWatchReceiver } from 'app/shared/model/watch-receiver.model';
import { Principal } from 'app/core';
import { WatchReceiverService } from './watch-receiver.service';

@Component({
    selector: 'jhi-watch-receiver',
    templateUrl: './watch-receiver.component.html'
})
export class WatchReceiverComponent implements OnInit, OnDestroy {
    watchReceivers: IWatchReceiver[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private watchReceiverService: WatchReceiverService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.watchReceiverService.query().subscribe(
            (res: HttpResponse<IWatchReceiver[]>) => {
                this.watchReceivers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWatchReceivers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWatchReceiver) {
        return item.id;
    }

    registerChangeInWatchReceivers() {
        this.eventSubscriber = this.eventManager.subscribe('watchReceiverListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
