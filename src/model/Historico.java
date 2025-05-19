package model;

public class Historico {
    private int id;
    private int usuarioId;
    private String termoBusca;

    public Historico(int id, int usuarioId, String termoBusca) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.termoBusca = termoBusca;
    }

    public Historico(int usuarioId, String termoBusca) {
        this(0, usuarioId, termoBusca);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTermoBusca() {
        return termoBusca;
    }
    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }
}
