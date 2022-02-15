package dao;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@AllArgsConstructor
@Table(name = "login")
public class Login extends BaseDAO{
    private ObjectId id;
    private Programador programador;
    private LocalDateTime instante;
    private String token;
    private AccessHistory accessHistory;
    private boolean conectado;

    public Login(Programador programador, LocalDateTime instante, String token, AccessHistory accessHistory, boolean conectado) {
        id = new ObjectId();
        this.programador = programador;
        this.instante = instante;
        this.token = token;
        this.accessHistory = accessHistory;
        this.conectado = conectado;
    }

    public Login() {
        this.id = new ObjectId();
        this.instante = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @OneToOne
    //@JoinColumn(name = "programador", referencedColumnName = "id", nullable = false)
    public Programador getProgramador() {
        return programador;
    }

    public void setProgramador(Programador programador) {
        this.programador = programador;
    }

    public LocalDateTime getInstante() {
        return instante;
    }

    public void setInstante(LocalDateTime instancia) {
        this.instante = instancia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @ManyToOne
    //@JoinColumn(name = "accessHistory", referencedColumnName = "id", nullable = false)
    public AccessHistory getAccessHistory() {
        return accessHistory;
    }

    public void setAccessHistory(AccessHistory accessHistory) {
        this.accessHistory = accessHistory;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", programador=" + programador +
                ", instancia=" + instante +
                ", conectado=" + conectado +
                ", accessHistory=" + accessHistory +
                ", token=" + token +
                '}';
    }
}
