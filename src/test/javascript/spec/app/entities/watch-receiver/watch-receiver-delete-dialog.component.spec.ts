/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchReceiverDeleteDialogComponent } from 'app/entities/watch-receiver/watch-receiver-delete-dialog.component';
import { WatchReceiverService } from 'app/entities/watch-receiver/watch-receiver.service';

describe('Component Tests', () => {
    describe('WatchReceiver Management Delete Component', () => {
        let comp: WatchReceiverDeleteDialogComponent;
        let fixture: ComponentFixture<WatchReceiverDeleteDialogComponent>;
        let service: WatchReceiverService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchReceiverDeleteDialogComponent]
            })
                .overrideTemplate(WatchReceiverDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WatchReceiverDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchReceiverService);
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
