import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWatchReceiver } from 'app/shared/model/watch-receiver.model';

@Component({
    selector: 'jhi-watch-receiver-detail',
    templateUrl: './watch-receiver-detail.component.html'
})
export class WatchReceiverDetailComponent implements OnInit {
    watchReceiver: IWatchReceiver;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ watchReceiver }) => {
            this.watchReceiver = watchReceiver;
        });
    }

    previousState() {
        window.history.back();
    }
}
