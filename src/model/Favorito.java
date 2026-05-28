package model;

public class Favorito {
	private int id;
	private int idUsuario;
	private String codigo;
	private String nome;

	public Favorito(int id, int idUsuario, String codigo, String nome) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.codigo = codigo;
		this.nome = nome;
	}

	public int getId() { return id; }
	public int getIdUsuario() { return idUsuario; }
	public String getCodigo() { return codigo; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
}
