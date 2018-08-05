import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IVersionWatcher } from 'app/shared/model/version-watcher.model';
import { VersionWatcherService } from './version-watcher.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-version-watcher-update',
    templateUrl: './version-watcher-update.component.html'
})
export class VersionWatcherUpdateComponent implements OnInit {
    private _versionWatcher: IVersionWatcher;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private versionWatcherService: VersionWatcherService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ versionWatcher }) => {
            this.versionWatcher = versionWatcher;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.versionWatcher.id !== undefined) {
            this.subscribeToSaveResponse(this.versionWatcherService.update(this.versionWatcher));
        } else {
            this.subscribeToSaveResponse(this.versionWatcherService.create(this.versionWatcher));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IVersionWatcher>>) {
        result.subscribe((res: HttpResponse<IVersionWatcher>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get versionWatcher() {
        return this._versionWatcher;
    }

    set versionWatcher(versionWatcher: IVersionWatcher) {
        this._versionWatcher = versionWatcher;
    }
}
