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
	
    public void salvar(Cep cep, Usuario u) {
    	String sql =
                "INSERT INTO cep (fk_id_usuario, codigo, cidade, estado, logradouro, complemento, bairro, UF, regiao, DDD, cont, horario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, CURRENT_TIMESTAMP)";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {
            	ps.setInt (1, u.getId());
                ps.setString(2, cep.getCodigo()); 
                ps.setString(3, cep.getCidade());
                ps.setString(4, cep.getEstado());
                ps.setString(5, cep.getLogradouro());
                ps.setString(6, cep.getComplemento());
                ps.setString(7, cep.getBairro());
                ps.setString(8, cep.getUF());
                ps.setString(9, cep.getRegiao());
                ps.setString(10, cep.getDDD());

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            
    }
    
    public Cep buscar(String cep, Usuario u) {
        String sql = "SELECT * FROM cep WHERE codigo = ? and fk_id_usuario = ?";
        Cep ultimo = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, cep);
            ps.setInt(2, u.getId());
            
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
    
    public void listar(int op, Usuario u) {
    	String sql = "";
    	switch(op) {
    	case 1:
    		sql = "SELECT * FROM cep WHERE fk_id_usuario = ? order by horario desc limit 15";
    		break;
    	case 2:
    		sql = "SELECT * FROM cep WHERE fk_id_usuario = ? order by horario asc limit 15";
    		break;
    	case 3:
    		sql = "SELECT * FROM cep WHERE fk_id_usuario = ? order by cont desc limit 15";
    		break;
    	}
    	try (
    	        Connection conn = ConnectionFactory.getConnection();
    	        PreparedStatement ps = conn.prepareStatement(sql);
    	    ) {
    	        ps.setInt(1, u.getId()); 
    	        
    	        try (ResultSet rs = ps.executeQuery()) {
    	            while (rs.next()) {
    	                System.out.println(
    	                    rs.getString("codigo") +
    	                    " - Cidade: " + rs.getString("cidade") +
    	                    ", Estado: " + rs.getString("estado") +
    	                    ", Logradouro: " + rs.getString("logradouro") +
    	                    ", Complemento: " + rs.getString("complemento") +
    	                    ", Bairro: " + rs.getString("bairro") +
    	                    ", UF: " + rs.getString("UF") +
    	                    ", Região: " + rs.getString("regiao") +
    	                    ", DDD: " + rs.getString("DDD")
    	                );
    	            }
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    }
    
    public void atualizar(String cep, Usuario u) {
    	String sql =
                "UPDATE cep set cont = cont + 1, horario = CURRENT_TIMESTAMP WHERE codigo = ? and fk_id_usuario = ?";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {

                ps.setString(1, cep);
                ps.setInt(2, u.getId());

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public void apagar(String cep, Usuario u) {
    	String sql =
                "DELETE FROM cep WHERE codigo = ? and fk_id_usuario = ?";

            try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {

                ps.setString(1, cep);
                ps.setInt(2, u.getId());

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public void limparHistórico( Usuario u) {
        String sql = "DELETE FROM cep WHERE fk_id_usuario = ?";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, u.getId());
            ps.executeUpdate(); 
            
            System.out.println("Histórico limpo com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Pesquisas especificas CEP
    
    public int cont(Usuario u) {
        String sql = "SELECT sum(cont) FROM cep WHERE fk_id_usuario = ?";
        int cont = 0;
        
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, u.getId());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cont = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar a soma de acessos: " + e.getMessage());
        }
        return cont;
    }
    
    public Cep maisConsultado(){
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
    
    public Cep ultimo(Usuario u) {
        String sql = "SELECT * FROM cep WHERE fk_id_usuario = ? ORDER BY horario DESC LIMIT 1";
        Cep ultimo = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, u.getId());
            
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
    
    public Usuario login(String email, String senha) throws NoSuchAlgorithmException {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        Usuario logado = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
        	senha = Util.gerarSHA256(salt + senha + salt);
            ps.setString(1, email);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logado = new Usuario(
                        rs.getInt("id_usuario"), 
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
    
    
    public void Cadastro(Usuario u) {
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
    
    public boolean ConferirEmail (String email)
    {
    	String sql =
                "Select * from usuario where email = ?";
    	try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                	if (rs.next()) {
                	    return true;
                }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao buscar CEP: " + e.getMessage());
            }
                return false;
    }
    
    
}