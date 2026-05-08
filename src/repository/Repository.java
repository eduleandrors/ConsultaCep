package repository;

import model.Cep;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Repository {

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
    	String sql = "SELECT * from cep WHERE codigo = ?";
    	Cep ultimo = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);            
        ) {
        	ps.setString(1, cep);
        	ResultSet rs = ps.executeQuery();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ultimo;
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

    public void listar(int op) {
    	String sql = "";
    	switch(op) {
    	case 1:
    		//recentes
    		sql = "SELECT * FROM cep order by horario limit 15";
    		break;
    	case 2:
    		//ordem original
    		sql = "SELECT * FROM cep limit 15";
    		break;
    	case 3:
    		//mais pesquisados
    		sql = "SELECT * FROM cep order by cont limit 15";
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
    
    public Cep ultimo() {
    	String sql = "SELECT * FROM cep order by horario desc limit 1";
    	Cep ultimo = null;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
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
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ultimo;
    }
    
}