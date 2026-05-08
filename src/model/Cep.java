package model;
import java.sql.Timestamp;

public class Cep {
    private String codigo, cidade, estado, logradouro, complemento, bairro, UF, regiao, DDD;
    private int consultas;
    private Timestamp horario;

    public Cep(String codigo, String cidade, String estado, String logradouro, String complemento, String bairro, String UF, String regiao, String DDD, int consultas, Timestamp horario) {
    	this.codigo = codigo;
        this.cidade = cidade;
        this.estado = estado;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.UF = UF;
        this.regiao = regiao;
        this.DDD = DDD;
        this.consultas = consultas;
        this.horario = horario;
        
    }

    public String getCodigo() { return codigo; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getLogradouro() { return logradouro; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getUF() { return UF; }
    public String getRegiao() { return regiao; }
    public String getDDD() { return DDD; }
    public int getConsultas() { return consultas; }
    public Timestamp getHorario() { return horario; }
}