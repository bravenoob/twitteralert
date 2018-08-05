import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWatchChannel } from 'app/shared/model/watch-channel.model';
import { WatchChannelService } from './watch-channel.service';

@Component({
    selector: 'jhi-watch-channel-delete-dialog',
    templateUrl: './watch-channel-delete-dialog.component.html'
})
export class WatchChannelDeleteDialogComponent {
    watchChannel: IWatchChannel;

    constructor(
        private watchChannelService: WatchChannelService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.watchChannelService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'watchChannelListModification',
                content: 'Deleted an watchChannel'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-watch-channel-delete-popup',
    template: ''
})
export class WatchChannelDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ watchChannel }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WatchChannelDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.watchChannel = watchChannel;
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
