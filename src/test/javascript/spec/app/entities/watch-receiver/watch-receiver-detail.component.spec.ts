/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchReceiverDetailComponent } from 'app/entities/watch-receiver/watch-receiver-detail.component';
import { WatchReceiver } from 'app/shared/model/watch-receiver.model';

describe('Component Tests', () => {
    describe('WatchReceiver Management Detail Component', () => {
        let comp: WatchReceiverDetailComponent;
        let fixture: ComponentFixture<WatchReceiverDetailComponent>;
        const route = ({ data: of({ watchReceiver: new WatchReceiver(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchReceiverDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WatchReceiverDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WatchReceiverDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.watchReceiver).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
