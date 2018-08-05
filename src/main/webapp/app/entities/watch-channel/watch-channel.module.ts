import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TwitteralertSharedModule } from 'app/shared';
import {
    WatchChannelComponent,
    WatchChannelDetailComponent,
    WatchChannelUpdateComponent,
    WatchChannelDeletePopupComponent,
    WatchChannelDeleteDialogComponent,
    watchChannelRoute,
    watchChannelPopupRoute
} from './';

const ENTITY_STATES = [...watchChannelRoute, ...watchChannelPopupRoute];

@NgModule({
    imports: [TwitteralertSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WatchChannelComponent,
        WatchChannelDetailComponent,
        WatchChannelUpdateComponent,
        WatchChannelDeleteDialogComponent,
        WatchChannelDeletePopupComponent
    ],
    entryComponents: [
        WatchChannelComponent,
        WatchChannelUpdateComponent,
        WatchChannelDeleteDialogComponent,
        WatchChannelDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TwitteralertWatchChannelModule {}
