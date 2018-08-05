import { IVersionWatcher } from 'app/shared/model//version-watcher.model';

export interface IWatchChannel {
    id?: number;
    channelName?: string;
    watcher?: IVersionWatcher;
}

export class WatchChannel implements IWatchChannel {
    constructor(public id?: number, public channelName?: string, public watcher?: IVersionWatcher) {}
}
