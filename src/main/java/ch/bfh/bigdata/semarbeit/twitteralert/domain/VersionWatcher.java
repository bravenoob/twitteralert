package ch.bfh.bigdata.semarbeit.twitteralert.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A VersionWatcher.
 */
@Entity
@Table(name = "version_watcher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VersionWatcher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "watcher_name", nullable = false)
    private String watcherName;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "watcher")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WatchChannel> channels = new HashSet<>();

    @OneToMany(mappedBy = "watcher")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WatchReceiver> receivers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWatcherName() {
        return watcherName;
    }

    public VersionWatcher watcherName(String watcherName) {
        this.watcherName = watcherName;
        return this;
    }

    public void setWatcherName(String watcherName) {
        this.watcherName = watcherName;
    }

    public Boolean isActive() {
        return active;
    }

    public VersionWatcher active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<WatchChannel> getChannels() {
        return channels;
    }

    public VersionWatcher channels(Set<WatchChannel> watchChannels) {
        this.channels = watchChannels;
        return this;
    }

    public VersionWatcher addChannels(WatchChannel watchChannel) {
        this.channels.add(watchChannel);
        watchChannel.setWatcher(this);
        return this;
    }

    public VersionWatcher removeChannels(WatchChannel watchChannel) {
        this.channels.remove(watchChannel);
        watchChannel.setWatcher(null);
        return this;
    }

    public void setChannels(Set<WatchChannel> watchChannels) {
        this.channels = watchChannels;
    }

    public Set<WatchReceiver> getReceivers() {
        return receivers;
    }

    public VersionWatcher receivers(Set<WatchReceiver> watchReceivers) {
        this.receivers = watchReceivers;
        return this;
    }

    public VersionWatcher addReceiver(WatchReceiver watchReceiver) {
        this.receivers.add(watchReceiver);
        watchReceiver.setWatcher(this);
        return this;
    }

    public VersionWatcher removeReceiver(WatchReceiver watchReceiver) {
        this.receivers.remove(watchReceiver);
        watchReceiver.setWatcher(null);
        return this;
    }

    public void setReceivers(Set<WatchReceiver> watchReceivers) {
        this.receivers = watchReceivers;
    }

    public User getUser() {
        return user;
    }

    public VersionWatcher user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VersionWatcher versionWatcher = (VersionWatcher) o;
        if (versionWatcher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), versionWatcher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VersionWatcher{" +
            "id=" + getId() +
            ", watcherName='" + getWatcherName() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
