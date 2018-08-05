import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWatchReceiver } from 'app/shared/model/watch-receiver.model';
import { WatchReceiverService } from './watch-receiver.service';
import { IVersionWatcher } from 'app/shared/model/version-watcher.model';
import { VersionWatcherService } from 'app/entities/version-watcher';

@Component({
    selector: 'jhi-watch-receiver-update',
    templateUrl: './watch-receiver-update.component.html'
})
export class WatchReceiverUpdateComponent implements OnInit {
    private _watchReceiver: IWatchReceiver;
    isSaving: boolean;

    versionwatchers: IVersionWatcher[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private watchReceiverService: WatchReceiverService,
        private versionWatcherService: VersionWatcherService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ watchReceiver }) => {
            this.watchReceiver = watchReceiver;
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
        if (this.watchReceiver.id !== undefined) {
            this.subscribeToSaveResponse(this.watchReceiverService.update(this.watchReceiver));
        } else {
            this.subscribeToSaveResponse(this.watchReceiverService.create(this.watchReceiver));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWatchReceiver>>) {
        result.subscribe((res: HttpResponse<IWatchReceiver>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get watchReceiver() {
        return this._watchReceiver;
    }

    set watchReceiver(watchReceiver: IWatchReceiver) {
        this._watchReceiver = watchReceiver;
    }
}
