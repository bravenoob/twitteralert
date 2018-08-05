/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchChannelUpdateComponent } from 'app/entities/watch-channel/watch-channel-update.component';
import { WatchChannelService } from 'app/entities/watch-channel/watch-channel.service';
import { WatchChannel } from 'app/shared/model/watch-channel.model';

describe('Component Tests', () => {
    describe('WatchChannel Management Update Component', () => {
        let comp: WatchChannelUpdateComponent;
        let fixture: ComponentFixture<WatchChannelUpdateComponent>;
        let service: WatchChannelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchChannelUpdateComponent]
            })
                .overrideTemplate(WatchChannelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WatchChannelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchChannelService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WatchChannel(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.watchChannel = entity;
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
                    const entity = new WatchChannel();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.watchChannel = entity;
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
