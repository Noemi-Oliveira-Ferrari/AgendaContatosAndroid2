package br.senai.sp.modelo;

import android.widget.ScrollView;

import java.io.Serializable;

/* a interface serializable serializa a classe */
public class Contato implements Serializable{

    private int id;
    private String nome;
    private String endereco;
    private String email;
    private String telefone;
    private String endereco_linkedin;
    private byte foto[];


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String  getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String  getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String  getEndereco_linkedin() {
        return endereco_linkedin;
    }

    public void setEndereco_linkedin(String endereco_linkedin) { this.endereco_linkedin = endereco_linkedin; }

    public String  getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String  getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) { this.telefone = telefone; }


    @Override
    public String toString() {
        return this.nome;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
