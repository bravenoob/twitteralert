import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IWatchChannel } from 'app/shared/model/watch-channel.model';
import { Principal } from 'app/core';
import { WatchChannelService } from './watch-channel.service';

@Component({
    selector: 'jhi-watch-channel',
    templateUrl: './watch-channel.component.html'
})
export class WatchChannelComponent implements OnInit, OnDestroy {
    watchChannels: IWatchChannel[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private watchChannelService: WatchChannelService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.watchChannelService.query().subscribe(
            (res: HttpResponse<IWatchChannel[]>) => {
                this.watchChannels = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWatchChannels();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWatchChannel) {
        return item.id;
    }

    registerChangeInWatchChannels() {
        this.eventSubscriber = this.eventManager.subscribe('watchChannelListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
