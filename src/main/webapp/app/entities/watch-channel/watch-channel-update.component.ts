import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWatchChannel } from 'app/shared/model/watch-channel.model';
import { WatchChannelService } from './watch-channel.service';
import { IVersionWatcher } from 'app/shared/model/version-watcher.model';
import { VersionWatcherService } from 'app/entities/version-watcher';

@Component({
    selector: 'jhi-watch-channel-update',
    templateUrl: './watch-channel-update.component.html'
})
export class WatchChannelUpdateComponent implements OnInit {
    private _watchChannel: IWatchChannel;
    isSaving: boolean;

    versionwatchers: IVersionWatcher[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private watchChannelService: WatchChannelService,
        private versionWatcherService: VersionWatcherService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ watchChannel }) => {
            this.watchChannel = watchChannel;
        });
        this.versionWatcherService.query().subscribe(
            (res: HttpResponse<IVersionWatcher[]>) => {
                this.versionwatchers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.watchChannel.id !== undefined) {
            this.subscribeToSaveResponse(this.watchChannelService.update(this.watchChannel));
        } else {
            this.subscribeToSaveResponse(this.watchChannelService.create(this.watchChannel));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWatchChannel>>) {
        result.subscribe((res: HttpResponse<IWatchChannel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackVersionWatcherById(index: number, item: IVersionWatcher) {
        return item.id;
    }
    get watchChannel() {
        return this._watchChannel;
    }

    set watchChannel(watchChannel: IWatchChannel) {
        this._watchChannel = watchChannel;
    }
}
