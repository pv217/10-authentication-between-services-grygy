package cz.muni.fi.airportmanager.baggageservice.entity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "`user`")
@UserDefinition // Tells security-jpa-reactive to use this entity as a user for authentication
public class User extends PanacheEntity {
    @Username

    public String username;
    @Password
    public String password;
    @Roles
    public String role;

    /**
     * Adds a new user to the database
     *
     * @param username the username
     * @param password the unencrypted password (it is encrypted with bcrypt)
     * @param role     the comma-separated roles
     */
    public static Uni<User> add(String username, String password, String role) {
        User user = new User();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        return user.persistAndFlush();
    }
}