package com.kronen.buddy.backend.persistence.domain.backend;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;

@Entity
public class PasswordResetToken implements Serializable {

    private static final long serialVersionUID = 1L;    
    
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetToken.class);

    private static final int DEFAULT_TOKEN_LENGHT_IN_MINUTES = 120;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String token;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "expiry_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expiryDate;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, User user, LocalDateTime creationDateTime, int expirationInMinutes) {
	if(token == null || user == null || creationDateTime == null) {
	    throw new IllegalArgumentException("You must provide a non null token, user and creationDateTime");
	}
	
	if(expirationInMinutes == 0) {
	    LOG.warn("The token expiration length in minutes is zero. Assigning the default value {}", 
		    DEFAULT_TOKEN_LENGHT_IN_MINUTES);
	    expirationInMinutes = DEFAULT_TOKEN_LENGHT_IN_MINUTES;
	}
	
	this.token = token;
	this.user = user;
	this.expiryDate = creationDateTime.plusMinutes(expirationInMinutes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	PasswordResetToken other = (PasswordResetToken) obj;
	return id == other.id;
    }
    
}
