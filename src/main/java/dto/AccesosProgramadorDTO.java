package dto;

import dao.Login;
import lombok.Data;

import java.util.Set;

@Data
public class AccesosProgramadorDTO {
    private int num;
    private Set<Login> logins;
}
