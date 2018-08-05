import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { VersionWatcher } from 'app/shared/model/version-watcher.model';
import { VersionWatcherService } from './version-watcher.service';
import { VersionWatcherComponent } from './version-watcher.component';
import { VersionWatcherDetailComponent } from './version-watcher-detail.component';
import { VersionWatcherUpdateComponent } from './version-watcher-update.component';
import { VersionWatcherDeletePopupComponent } from './version-watcher-delete-dialog.component';
import { IVersionWatcher } from 'app/shared/model/version-watcher.model';

@Injectable({ providedIn: 'root' })
export class VersionWatcherResolve implements Resolve<IVersionWatcher> {
    constructor(private service: VersionWatcherService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((versionWatcher: HttpResponse<VersionWatcher>) => versionWatcher.body));
        }
        return of(new VersionWatcher());
    }
}

export const versionWatcherRoute: Routes = [
    {
        path: 'version-watcher',
        component: VersionWatcherComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VersionWatchers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version-watcher/:id/view',
        component: VersionWatcherDetailComponent,
        resolve: {
            versionWatcher: VersionWatcherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VersionWatchers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version-watcher/new',
        component: VersionWatcherUpdateComponent,
        resolve: {
            versionWatcher: VersionWatcherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VersionWatchers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version-watcher/:id/edit',
        component: VersionWatcherUpdateComponent,
        resolve: {
            versionWatcher: VersionWatcherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VersionWatchers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const versionWatcherPopupRoute: Routes = [
    {
        path: 'version-watcher/:id/delete',
        component: VersionWatcherDeletePopupComponent,
        resolve: {
            versionWatcher: VersionWatcherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VersionWatchers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
