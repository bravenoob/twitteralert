package ch.bfh.bigdata.semarbeit.twitteralert.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A WatchChannel.
 */
@Entity
@Table(name = "watch_channel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WatchChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "channel_name", nullable = false)
    private String channelName;

    @ManyToOne
    @JsonIgnoreProperties("channels")
    private VersionWatcher watcher;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public WatchChannel channelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public VersionWatcher getWatcher() {
        return watcher;
    }

    public WatchChannel watcher(VersionWatcher versionWatcher) {
        this.watcher = versionWatcher;
        return this;
    }

    public void setWatcher(VersionWatcher versionWatcher) {
        this.watcher = versionWatcher;
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
        WatchChannel watchChannel = (WatchChannel) o;
        if (watchChannel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), watchChannel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WatchChannel{" +
            "id=" + getId() +
            ", channelName='" + getChannelName() + "'" +
            "}";
    }
}
