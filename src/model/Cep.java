package model;
import java.time.LocalDateTime;

public class Cep {
    private String codigo, cidade, estado, logradouro, complemento, bairro, UF, regiao, DDD;
    private int consultas;
    private LocalDateTime horario;

    public Cep(String codigo, String cidade, String estado, String logradouro, String complemento, String bairro, String UF, String regiao, String DDD) {
    	this.codigo = codigo;
        this.cidade = cidade;
        this.estado = estado;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.UF = UF;
        this.regiao = regiao;
        this.DDD = DDD;
        this.consultas = 1;
        this.horario = LocalDateTime.now();
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
    public LocalDateTime getHorario() { return horario; }
    
    public void setConsultas(int num) {
    	this.consultas = num;
    	this.horario = LocalDateTime.now();
    }
}