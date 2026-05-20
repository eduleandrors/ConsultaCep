package model;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import util.Util;

public class Usuario {
	private int id;
	private String nome, email, senha;
	private Timestamp dataCriacao;
	private String salt = "svl12j";
	
	public Usuario(int id, String nome, String email, String senha, Timestamp dataCriacao){
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.dataCriacao = dataCriacao;
	}
	
	public void hashar() throws NoSuchAlgorithmException {
		this.senha = Util.gerarSHA256(this.salt + senha + this.salt);
	}
	
	public int getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public String getEmail() {
		return email;
	}
	public String getSenha() {
		return senha;
	}
	public Timestamp getDataCriacao() {
		return dataCriacao;
	}
}
