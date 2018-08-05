import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVersionWatcher } from 'app/shared/model/version-watcher.model';
import { VersionWatcherService } from './version-watcher.service';

@Component({
    selector: 'jhi-version-watcher-delete-dialog',
    templateUrl: './version-watcher-delete-dialog.component.html'
})
export class VersionWatcherDeleteDialogComponent {
    versionWatcher: IVersionWatcher;

    constructor(
        private versionWatcherService: VersionWatcherService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.versionWatcherService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'versionWatcherListModification',
                content: 'Deleted an versionWatcher'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-version-watcher-delete-popup',
    template: ''
})
export class VersionWatcherDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ versionWatcher }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(VersionWatcherDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.versionWatcher = versionWatcher;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
