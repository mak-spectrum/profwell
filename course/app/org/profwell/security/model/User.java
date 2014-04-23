package org.profwell.security.model;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;

@Entity
@Table(name="USER")
@Access(AccessType.FIELD)
public class User implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", unique = true, nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @Column(name="UUID", nullable = true, length = 36, unique = true)
    private String uuid;

    @Column(name="FIRST_NAME", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String firstName;

    @Column(name="LAST_NAME", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String lastName;

    @Column(name="USERNAME", unique = true, nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String username;

    @Column(name="EMAIL", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String email;

    @Column(name="PASSWORD", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String password;

    @Column(name="TIME_OUT_STAMP", nullable = true)
    private Timestamp timeoutStamp;

    @Enumerated(EnumType.STRING)
    @Column(name="ROLE", nullable = false, length = 20)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Workspace workspace;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getTimeoutStamp() {
        return timeoutStamp;
    }

    public void setTimeoutStamp(Timestamp timeoutStamp) {
        this.timeoutStamp = timeoutStamp;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
