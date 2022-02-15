package dao;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Table(name = "access_history")
public class AccessHistory extends BaseDAO{
    private ObjectId id;
    private ObjectId programador;
    private LocalDateTime instante;
    private Set<Login> logins;

    public AccessHistory(ObjectId programador, LocalDateTime instante) {
        id = new ObjectId();
        this.programador = programador;
        this.instante = instante;
        logins = new HashSet<>();
    }

    public AccessHistory() {
        this.id = new ObjectId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProgramador() {
        return programador;
    }

    public void setProgramador(ObjectId programador) {
        this.programador = programador;
    }

    public LocalDateTime getInstante() {
        return instante;
    }

    public void setInstante(LocalDateTime instancia) {
        this.instante = instancia;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accessHistory", cascade = CascadeType.REMOVE)
    public Set<Login> getLogins() {
        return logins;
    }

    public void setLogins(Set<Login> logins) {
        this.logins = logins;
    }

    @Override
    public String toString() {
        return "AccessHistory{" +
                "id=" + id +
                ", programador=" + programador +
                ", instancia=" + instante +
                ", logins=" + logins +
                '}';
    }
}
