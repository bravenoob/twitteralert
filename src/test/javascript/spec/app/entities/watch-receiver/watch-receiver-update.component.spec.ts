/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchReceiverUpdateComponent } from 'app/entities/watch-receiver/watch-receiver-update.component';
import { WatchReceiverService } from 'app/entities/watch-receiver/watch-receiver.service';
import { WatchReceiver } from 'app/shared/model/watch-receiver.model';

describe('Component Tests', () => {
    describe('WatchReceiver Management Update Component', () => {
        let comp: WatchReceiverUpdateComponent;
        let fixture: ComponentFixture<WatchReceiverUpdateComponent>;
        let service: WatchReceiverService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchReceiverUpdateComponent]
            })
                .overrideTemplate(WatchReceiverUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WatchReceiverUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchReceiverService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WatchReceiver(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.watchReceiver = entity;
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
                    const entity = new WatchReceiver();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.watchReceiver = entity;
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
