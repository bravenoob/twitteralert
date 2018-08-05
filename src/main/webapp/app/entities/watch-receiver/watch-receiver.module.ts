import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TwitteralertSharedModule } from 'app/shared';
import {
    WatchReceiverComponent,
    WatchReceiverDetailComponent,
    WatchReceiverUpdateComponent,
    WatchReceiverDeletePopupComponent,
    WatchReceiverDeleteDialogComponent,
    watchReceiverRoute,
    watchReceiverPopupRoute
} from './';

const ENTITY_STATES = [...watchReceiverRoute, ...watchReceiverPopupRoute];

@NgModule({
    imports: [TwitteralertSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WatchReceiverComponent,
        WatchReceiverDetailComponent,
        WatchReceiverUpdateComponent,
        WatchReceiverDeleteDialogComponent,
        WatchReceiverDeletePopupComponent
    ],
    entryComponents: [
        WatchReceiverComponent,
        WatchReceiverUpdateComponent,
        WatchReceiverDeleteDialogComponent,
        WatchReceiverDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TwitteralertWatchReceiverModule {}
