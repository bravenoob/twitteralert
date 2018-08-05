package ch.bfh.bigdata.semarbeit.twitteralert.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A WatchReceiver.
 */
@Entity
@Table(name = "watch_receiver")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WatchReceiver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "channel_name")
    private String channelName;

    @ManyToOne
    @JsonIgnoreProperties("receivers")
    private VersionWatcher watcher;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public WatchReceiver email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannelName() {
        return channelName;
    }

    public WatchReceiver channelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public VersionWatcher getWatcher() {
        return watcher;
    }

    public WatchReceiver watcher(VersionWatcher versionWatcher) {
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
        WatchReceiver watchReceiver = (WatchReceiver) o;
        if (watchReceiver.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), watchReceiver.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WatchReceiver{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", channelName='" + getChannelName() + "'" +
            "}";
    }
}
