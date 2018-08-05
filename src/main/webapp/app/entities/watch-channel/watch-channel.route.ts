import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WatchChannel } from 'app/shared/model/watch-channel.model';
import { WatchChannelService } from './watch-channel.service';
import { WatchChannelComponent } from './watch-channel.component';
import { WatchChannelDetailComponent } from './watch-channel-detail.component';
import { WatchChannelUpdateComponent } from './watch-channel-update.component';
import { WatchChannelDeletePopupComponent } from './watch-channel-delete-dialog.component';
import { IWatchChannel } from 'app/shared/model/watch-channel.model';

@Injectable({ providedIn: 'root' })
export class WatchChannelResolve implements Resolve<IWatchChannel> {
    constructor(private service: WatchChannelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((watchChannel: HttpResponse<WatchChannel>) => watchChannel.body));
        }
        return of(new WatchChannel());
    }
}

export const watchChannelRoute: Routes = [
    {
        path: 'watch-channel',
        component: WatchChannelComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchChannels'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'watch-channel/:id/view',
        component: WatchChannelDetailComponent,
        resolve: {
            watchChannel: WatchChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchChannels'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'watch-channel/new',
        component: WatchChannelUpdateComponent,
        resolve: {
            watchChannel: WatchChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchChannels'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'watch-channel/:id/edit',
        component: WatchChannelUpdateComponent,
        resolve: {
            watchChannel: WatchChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchChannels'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const watchChannelPopupRoute: Routes = [
    {
        path: 'watch-channel/:id/delete',
        component: WatchChannelDeletePopupComponent,
        resolve: {
            watchChannel: WatchChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchChannels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
