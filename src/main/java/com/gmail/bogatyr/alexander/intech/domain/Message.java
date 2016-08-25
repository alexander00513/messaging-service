package com.gmail.bogatyr.alexander.intech.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Alexander Bogatyrenko on 12.08.16.
 *
 * This class represents...
 */
@Entity
public class Message extends AbstractAuditingEntity {

    @Id
    @SequenceGenerator(name = "message-id-generator", sequenceName = "message_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message-id-generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String subject;

    @NotNull
    @Size(min = 1, max = 1024)
    @Column(length = 1024, nullable = false)
    private String message;

    @NotNull
    @ManyToOne
    private User from;

    @NotNull
    @ManyToOne
    private User to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (id != null ? !id.equals(message1.id) : message1.id != null) return false;
        if (subject != null ? !subject.equals(message1.subject) : message1.subject != null) return false;
        if (message != null ? !message.equals(message1.message) : message1.message != null) return false;
        if (from != null ? !from.equals(message1.from) : message1.from != null) return false;
        return to != null ? to.equals(message1.to) : message1.to == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}
