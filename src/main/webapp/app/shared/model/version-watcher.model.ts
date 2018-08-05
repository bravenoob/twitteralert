import { IWatchChannel } from 'app/shared/model//watch-channel.model';
import { IWatchReceiver } from 'app/shared/model//watch-receiver.model';
import { IUser } from 'app/core/user/user.model';

export interface IVersionWatcher {
    id?: number;
    watcherName?: string;
    active?: boolean;
    channels?: IWatchChannel[];
    receivers?: IWatchReceiver[];
    user?: IUser;
}

export class VersionWatcher implements IVersionWatcher {
    constructor(
        public id?: number,
        public watcherName?: string,
        public active?: boolean,
        public channels?: IWatchChannel[],
        public receivers?: IWatchReceiver[],
        public user?: IUser
    ) {
        this.active = false;
    }
}
