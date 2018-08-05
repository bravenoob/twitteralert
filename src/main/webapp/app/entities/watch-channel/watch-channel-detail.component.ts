import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWatchChannel } from 'app/shared/model/watch-channel.model';

@Component({
    selector: 'jhi-watch-channel-detail',
    templateUrl: './watch-channel-detail.component.html'
})
export class WatchChannelDetailComponent implements OnInit {
    watchChannel: IWatchChannel;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ watchChannel }) => {
            this.watchChannel = watchChannel;
        });
    }

    previousState() {
        window.history.back();
    }
}
