import { IVersionWatcher } from 'app/shared/model//version-watcher.model';

export interface IWatchReceiver {
    id?: number;
    email?: string;
    channelName?: string;
    watcher?: IVersionWatcher;
}

export class WatchReceiver implements IWatchReceiver {
    constructor(public id?: number, public email?: string, public channelName?: string, public watcher?: IVersionWatcher) {}
}
