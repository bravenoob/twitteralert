/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TwitteralertTestModule } from '../../../test.module';
import { WatchChannelComponent } from 'app/entities/watch-channel/watch-channel.component';
import { WatchChannelService } from 'app/entities/watch-channel/watch-channel.service';
import { WatchChannel } from 'app/shared/model/watch-channel.model';

describe('Component Tests', () => {
    describe('WatchChannel Management Component', () => {
        let comp: WatchChannelComponent;
        let fixture: ComponentFixture<WatchChannelComponent>;
        let service: WatchChannelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TwitteralertTestModule],
                declarations: [WatchChannelComponent],
                providers: []
            })
                .overrideTemplate(WatchChannelComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WatchChannelComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchChannelService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new WatchChannel(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.watchChannels[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
