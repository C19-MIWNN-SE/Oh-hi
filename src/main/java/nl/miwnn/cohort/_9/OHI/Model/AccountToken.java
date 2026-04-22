package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * @author INT Developers
 * Handles the setup of account tokens for users to login
 */
@Entity

public class AccountToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String token;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private OHIUser ohiUser;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    protected AccountToken() {}

    public AccountToken(String token, OHIUser ohiUser, LocalDateTime expiresAt) {
        this.token = token;
        this.ohiUser = ohiUser;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OHIUser getOhiUser() {
        return ohiUser;
    }

    public void setOhiUser(OHIUser ohiUser) {
        this.ohiUser = ohiUser;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
