/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchChannelDeleteDialogComponent } from 'app/entities/watch-channel/watch-channel-delete-dialog.component';
import { WatchChannelService } from 'app/entities/watch-channel/watch-channel.service';

describe('Component Tests', () => {
    describe('WatchChannel Management Delete Component', () => {
        let comp: WatchChannelDeleteDialogComponent;
        let fixture: ComponentFixture<WatchChannelDeleteDialogComponent>;
        let service: WatchChannelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchChannelDeleteDialogComponent]
            })
                .overrideTemplate(WatchChannelDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WatchChannelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchChannelService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
