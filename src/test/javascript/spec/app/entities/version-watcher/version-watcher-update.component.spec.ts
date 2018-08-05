/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TwitteralertTestModule } from '../../../test.module';
import { VersionWatcherUpdateComponent } from 'app/entities/version-watcher/version-watcher-update.component';
import { VersionWatcherService } from 'app/entities/version-watcher/version-watcher.service';
import { VersionWatcher } from 'app/shared/model/version-watcher.model';

describe('Component Tests', () => {
    describe('VersionWatcher Management Update Component', () => {
        let comp: VersionWatcherUpdateComponent;
        let fixture: ComponentFixture<VersionWatcherUpdateComponent>;
        let service: VersionWatcherService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [VersionWatcherUpdateComponent]
            })
                .overrideTemplate(VersionWatcherUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VersionWatcherUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VersionWatcherService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new VersionWatcher(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.versionWatcher = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new VersionWatcher();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.versionWatcher = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
