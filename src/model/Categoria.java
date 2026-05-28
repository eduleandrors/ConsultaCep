package model;

public class Categoria {
	private int id;
	private int idUsuario;
	private String nome;

	public Categoria(int id, int idUsuario, String nome) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.nome = nome;
	}

	public int getId() { return id; }
	public int getIdUsuario() { return idUsuario; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
}
