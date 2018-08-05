/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchReceiverComponent } from 'app/entities/watch-receiver/watch-receiver.component';
import { WatchReceiverService } from 'app/entities/watch-receiver/watch-receiver.service';
import { WatchReceiver } from 'app/shared/model/watch-receiver.model';

describe('Component Tests', () => {
    describe('WatchReceiver Management Component', () => {
        let comp: WatchReceiverComponent;
        let fixture: ComponentFixture<WatchReceiverComponent>;
        let service: WatchReceiverService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchReceiverComponent],
                providers: []
            })
                .overrideTemplate(WatchReceiverComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WatchReceiverComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchReceiverService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new WatchReceiver(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.watchReceivers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
