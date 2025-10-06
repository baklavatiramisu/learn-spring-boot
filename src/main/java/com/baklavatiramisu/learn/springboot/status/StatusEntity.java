package com.baklavatiramisu.learn.springboot.status;

import com.baklavatiramisu.learn.springboot.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

@Entity(name = "Status")
@Table(name = "status_updates")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity user;
    @NotBlank
    @Size(max = 500)
    private String status;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    private OffsetDateTime deletedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public OffsetDateTime getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(OffsetDateTime deletedOn) {
        this.deletedOn = deletedOn;
    }

    @Override
    public String toString() {
        return "StatusEntity{" +
                "id=" + id +
                ", user=" + user +
                ", status='" + status + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", deletedOn=" + deletedOn +
                '}';
    }
}
