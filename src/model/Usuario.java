package model;

public class Usuario extends Pessoa {
    protected String email;
    protected int senha;

    
    public Usuario(int id, String email, int senha, String nome) {
        super(id, nome); 
        this.email = email;
        this.senha = senha;
    }

   
    public Usuario(String nome, int senha, String email) {
        
        super(0, nome); 
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id; 
    }

    public void setId(int id) {
        this.id = id; 
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }
}