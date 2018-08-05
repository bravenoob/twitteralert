import { NgModule } from '@angular/core';

import { TwitteralertSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [TwitteralertSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [TwitteralertSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class TwitteralertSharedCommonModule {}
