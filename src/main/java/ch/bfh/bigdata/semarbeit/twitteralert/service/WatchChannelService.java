package ch.bfh.bigdata.semarbeit.twitteralert.service;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.WatchChannel;
import ch.bfh.bigdata.semarbeit.twitteralert.repository.WatchChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WatchChannelService {
    private final Logger log = LoggerFactory.getLogger(WatchChannelService.class);
    private final WatchChannelRepository watchChannelRepository;
    private final TwitterService twitterService;


    public WatchChannelService(WatchChannelRepository watchChannelRepository, TwitterService twitterService) {
        this.watchChannelRepository = watchChannelRepository;
        this.twitterService = twitterService;
    }

    public WatchChannel save(WatchChannel watchChannel) {
        WatchChannel save = watchChannelRepository.save(watchChannel);
        twitterService.setTwitterFollwings(findAll().stream().map(channel -> channel.getChannelName()).collect(Collectors.toList()));
        twitterService.executeAsynchronously();
        return save;
    }


    public List<WatchChannel> findAll() {
        return watchChannelRepository.findAll();
    }

    public Optional<WatchChannel> findById(Long id) {
        return watchChannelRepository.findById(id);
    }

    public void deleteById(Long id) {
        watchChannelRepository.deleteById(id);
    }
}
