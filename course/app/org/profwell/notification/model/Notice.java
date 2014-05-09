package org.profwell.notification.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;
import org.profwell.security.model.User;

@Entity
@Table(name="NOTICE")
@Access(AccessType.FIELD)
public class Notice implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", unique = true, nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;
    
    @Column(name="TEXT", nullable = true, length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT, columnDefinition="TEXT")
    private String text;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID",
            referencedColumnName = "ID",
            nullable = true)
    private User sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID",
            referencedColumnName = "ID",
            nullable = true)
    private User receiver;
    
   	@Override
	public long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
        return id == DEFAULT_UNINITIALIZED_ID_VALUE;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public void setId(long id) {
		this.id = id;
	}

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
