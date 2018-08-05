import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVersionWatcher } from 'app/shared/model/version-watcher.model';

@Component({
    selector: 'jhi-version-watcher-detail',
    templateUrl: './version-watcher-detail.component.html'
})
export class VersionWatcherDetailComponent implements OnInit {
    versionWatcher: IVersionWatcher;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ versionWatcher }) => {
            this.versionWatcher = versionWatcher;
        });
    }

    previousState() {
        window.history.back();
    }
}
