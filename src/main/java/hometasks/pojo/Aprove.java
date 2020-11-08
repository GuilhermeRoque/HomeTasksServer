package hometasks.pojo;

/* Esta classe representa a tabela Aprova do banco de dados que guarda o historico de aprovação de uma Regra
*  por um Usuario*/

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Aprove {
    private String idUser;
    private int idRule;
    private boolean state;

    public Aprove(String idUser, int idRule, boolean state) {
        this.idUser = idUser;
        this.idRule = idRule;
        this.state = state;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getIdRule() {
        return idRule;
    }

    public void setIdRule(int idRule) {
        this.idRule = idRule;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Aprova{" +
                "idUsuario='" + idUser + '\'' +
                ", idRegra=" + idRule +
                ", estado=" + state +
                '}';
    }
}
