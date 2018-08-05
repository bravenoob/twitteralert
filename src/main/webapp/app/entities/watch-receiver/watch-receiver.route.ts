import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WatchReceiver } from 'app/shared/model/watch-receiver.model';
import { WatchReceiverService } from './watch-receiver.service';
import { WatchReceiverComponent } from './watch-receiver.component';
import { WatchReceiverDetailComponent } from './watch-receiver-detail.component';
import { WatchReceiverUpdateComponent } from './watch-receiver-update.component';
import { WatchReceiverDeletePopupComponent } from './watch-receiver-delete-dialog.component';
import { IWatchReceiver } from 'app/shared/model/watch-receiver.model';

@Injectable({ providedIn: 'root' })
export class WatchReceiverResolve implements Resolve<IWatchReceiver> {
    constructor(private service: WatchReceiverService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((watchReceiver: HttpResponse<WatchReceiver>) => watchReceiver.body));
        }
        return of(new WatchReceiver());
    }
}

export const watchReceiverRoute: Routes = [
    {
        path: 'watch-receiver',
        component: WatchReceiverComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchReceivers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'watch-receiver/:id/view',
        component: WatchReceiverDetailComponent,
        resolve: {
            watchReceiver: WatchReceiverResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchReceivers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'watch-receiver/new',
        component: WatchReceiverUpdateComponent,
        resolve: {
            watchReceiver: WatchReceiverResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchReceivers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'watch-receiver/:id/edit',
        component: WatchReceiverUpdateComponent,
        resolve: {
            watchReceiver: WatchReceiverResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchReceivers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const watchReceiverPopupRoute: Routes = [
    {
        path: 'watch-receiver/:id/delete',
        component: WatchReceiverDeletePopupComponent,
        resolve: {
            watchReceiver: WatchReceiverResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WatchReceivers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
