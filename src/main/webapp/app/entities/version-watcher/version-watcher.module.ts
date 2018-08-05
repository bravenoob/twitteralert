import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TwitteralertSharedModule } from 'app/shared';
import { TwitteralertAdminModule } from 'app/admin/admin.module';
import {
    VersionWatcherComponent,
    VersionWatcherDetailComponent,
    VersionWatcherUpdateComponent,
    VersionWatcherDeletePopupComponent,
    VersionWatcherDeleteDialogComponent,
    versionWatcherRoute,
    versionWatcherPopupRoute
} from './';

const ENTITY_STATES = [...versionWatcherRoute, ...versionWatcherPopupRoute];

@NgModule({
    imports: [TwitteralertSharedModule, TwitteralertAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        VersionWatcherComponent,
        VersionWatcherDetailComponent,
        VersionWatcherUpdateComponent,
        VersionWatcherDeleteDialogComponent,
        VersionWatcherDeletePopupComponent
    ],
    entryComponents: [
        VersionWatcherComponent,
        VersionWatcherUpdateComponent,
        VersionWatcherDeleteDialogComponent,
        VersionWatcherDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TwitteralertVersionWatcherModule {}
