import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWatchReceiver } from 'app/shared/model/watch-receiver.model';
import { WatchReceiverService } from './watch-receiver.service';

@Component({
    selector: 'jhi-watch-receiver-delete-dialog',
    templateUrl: './watch-receiver-delete-dialog.component.html'
})
export class WatchReceiverDeleteDialogComponent {
    watchReceiver: IWatchReceiver;

    constructor(
        private watchReceiverService: WatchReceiverService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.watchReceiverService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'watchReceiverListModification',
                content: 'Deleted an watchReceiver'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-watch-receiver-delete-popup',
    template: ''
})
export class WatchReceiverDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ watchReceiver }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WatchReceiverDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.watchReceiver = watchReceiver;
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
