import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWatchChannel } from 'app/shared/model/watch-channel.model';

type EntityResponseType = HttpResponse<IWatchChannel>;
type EntityArrayResponseType = HttpResponse<IWatchChannel[]>;

@Injectable({ providedIn: 'root' })
export class WatchChannelService {
    private resourceUrl = SERVER_API_URL + 'api/watch-channels';

    constructor(private http: HttpClient) {}

    create(watchChannel: IWatchChannel): Observable<EntityResponseType> {
        return this.http.post<IWatchChannel>(this.resourceUrl, watchChannel, { observe: 'response' });
    }

    update(watchChannel: IWatchChannel): Observable<EntityResponseType> {
        return this.http.put<IWatchChannel>(this.resourceUrl, watchChannel, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IWatchChannel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IWatchChannel[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
