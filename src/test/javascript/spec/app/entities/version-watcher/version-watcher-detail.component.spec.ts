/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TwitteralertTestModule } from '../../../test.module';
import { VersionWatcherDetailComponent } from 'app/entities/version-watcher/version-watcher-detail.component';
import { VersionWatcher } from 'app/shared/model/version-watcher.model';

describe('Component Tests', () => {
    describe('VersionWatcher Management Detail Component', () => {
        let comp: VersionWatcherDetailComponent;
        let fixture: ComponentFixture<VersionWatcherDetailComponent>;
        const route = ({ data: of({ versionWatcher: new VersionWatcher(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [VersionWatcherDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(VersionWatcherDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VersionWatcherDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.versionWatcher).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
