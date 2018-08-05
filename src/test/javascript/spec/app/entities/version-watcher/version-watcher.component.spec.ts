/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TwitteralertTestModule } from '../../../test.module';
import { VersionWatcherComponent } from 'app/entities/version-watcher/version-watcher.component';
import { VersionWatcherService } from 'app/entities/version-watcher/version-watcher.service';
import { VersionWatcher } from 'app/shared/model/version-watcher.model';

describe('Component Tests', () => {
    describe('VersionWatcher Management Component', () => {
        let comp: VersionWatcherComponent;
        let fixture: ComponentFixture<VersionWatcherComponent>;
        let service: VersionWatcherService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [VersionWatcherComponent],
                providers: []
            })
                .overrideTemplate(VersionWatcherComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VersionWatcherComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VersionWatcherService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new VersionWatcher(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.versionWatchers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
