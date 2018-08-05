/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchChannelDetailComponent } from 'app/entities/watch-channel/watch-channel-detail.component';
import { WatchChannel } from 'app/shared/model/watch-channel.model';

describe('Component Tests', () => {
    describe('WatchChannel Management Detail Component', () => {
        let comp: WatchChannelDetailComponent;
        let fixture: ComponentFixture<WatchChannelDetailComponent>;
        const route = ({ data: of({ watchChannel: new WatchChannel(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchChannelDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WatchChannelDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WatchChannelDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.watchChannel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
