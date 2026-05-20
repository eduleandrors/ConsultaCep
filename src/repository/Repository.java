package repository;

import model.Cep;
import model.Usuario;
import util.ConnectionFactory;
import util.Util;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Repository {
	
	private String salt = "svl12j";

	//CRUD CEP
	
    public void salvar(Cep cep) {
    	String sql =
                "INSERT INTO cep (codigo, cidade, estado, logradouro, complemento, bairro, UF, regiao, DDD, cont, horario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, CURRENT_TIMESTAMP)";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {

                ps.setString(1, cep.getCodigo()); 
                ps.setString(2, cep.getCidade());
                ps.setString(3, cep.getEstado());
                ps.setString(4, cep.getLogradouro());
                ps.setString(5, cep.getComplemento());
                ps.setString(6, cep.getBairro());
                ps.setString(7, cep.getUF());
                ps.setString(8, cep.getRegiao());
                ps.setString(9, cep.getDDD());

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            
    }
    
    public Cep buscar(String cep) {
        String sql = "SELECT * FROM cep WHERE codigo = ?";
        Cep ultimo = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, cep);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ultimo = new Cep(
                        rs.getString("codigo"), 
                        rs.getString("cidade"), 
                        rs.getString("estado"),
                        rs.getString("logradouro"),
                        rs.getString("complemento"),
                        rs.getString("bairro"),
                        rs.getString("UF"),
                        rs.getString("regiao"),
                        rs.getString("DDD"),
                        rs.getInt("cont"),
                        rs.getTimestamp("horario")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar CEP: " + e.getMessage());
        }
        
        return ultimo;
    }
    
    public void listar(int op) {
    	String sql = "";
    	switch(op) {
    	case 1:
    		//recentes
    		sql = "SELECT * FROM cep order by horario desc limit 15";
    		break;
    	case 2:
    		sql = "SELECT * FROM cep order by cont desc limit 15";
    		break;
    	}
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {

            while (rs.next()) {
                System.out.println(
                		rs.getString("codigo") +
        			    " - Cidade: " + rs.getString("cidade") +
        			    ", Estado: " + rs.getString("estado") +
        			    ", Logradouro: " + rs.getString("logradouro") +
        			    ", Complemento: " + rs.getString("complemento") +
        			    ", Bairro: " +rs.getString("bairro") +
        			    ", UF: " + rs.getString("UF") +
        			    ", Região: " + rs.getString("regiao") +
        			    ", DDD: " + rs.getString("DDD")
        			);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizar(String cep) {
    	String sql =
                "UPDATE cep set cont = cont + 1, horario = CURRENT_TIMESTAMP WHERE codigo = ?";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {

                ps.setString(1, cep);

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public void apagar(String cep) {
    	String sql =
                "DELETE FROM cep WHERE codigo = ?";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {

                ps.setString(1, cep);

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public void limparHitórico(String cep) {
    	String sql =
                "DELETE * FROM cep";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    //Pesquisas especificas CEP
    
    public int cont ()
    {
    	String sql = "select sum(cont) from cep;";
    	int cont = 0;
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cont = rs.getInt("sum(cont)");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar CEP: " + e.getMessage());
        }
        return cont;
    }
    
    public Cep maisConsultdo(){
    	Cep mais = null;
    	String sql = "select * from cep order by cont desc limit 1;";
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	 mais = new Cep(
                             rs.getString("codigo"), 
                             rs.getString("cidade"), 
                             rs.getString("estado"),
                             rs.getString("logradouro"),
                             rs.getString("complemento"),
                             rs.getString("bairro"),
                             rs.getString("UF"),
                             rs.getString("regiao"),
                             rs.getString("DDD"),
                             rs.getInt("cont"),
                             rs.getTimestamp("horario")
                         );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar CEP: " + e.getMessage());
        }
        return mais;
    }
    
    public Cep ultimo() {
    	String sql = "SELECT * FROM cep order by horario desc limit 1";
    	Cep ultimo = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
        	if (rs.next()) {
        		ultimo = new Cep(rs.getString("codigo"), 
            			rs.getString("cidade"), 
            			rs.getString("estado"),
        			    rs.getString("logradouro"),
        			    rs.getString("complemento"),
        			    rs.getString("bairro"),
        			    rs.getString("UF"),
        			    rs.getString("regiao"),
        			    rs.getString("DDD"),
        			    rs.getInt("cont"),
        			    rs.getTimestamp("horario"));
        	}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ultimo;
    }
    
    //CRUD USUARIO
    public void criarUsuario(Usuario u) {
    	String sql =
                "INSERT INTO usuario (nome, email, senha, dataCriacao) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {
            	u.hashar();
                ps.setString(1, u.getNome()); 
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getSenha());

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            
    }
    
    public Usuario login(String senha) throws NoSuchAlgorithmException {
        String sql = "SELECT * FROM usuario WHERE senha = ?";
        Usuario logado = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
        	senha = Util.gerarSHA256(salt + senha + salt);
            ps.setString(1, senha);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logado = new Usuario(
                        rs.getInt("id"), 
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getTimestamp("dataCriacao")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar CEP: " + e.getMessage());
        }
        
        return logado;
    } 
}