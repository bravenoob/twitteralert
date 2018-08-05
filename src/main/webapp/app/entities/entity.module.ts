import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TwitteralertVersionWatcherModule } from './version-watcher/version-watcher.module';
import { TwitteralertWatchChannelModule } from './watch-channel/watch-channel.module';
import { TwitteralertWatchReceiverModule } from './watch-receiver/watch-receiver.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        TwitteralertVersionWatcherModule,
        TwitteralertWatchChannelModule,
        TwitteralertWatchReceiverModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TwitteralertEntityModule {}
