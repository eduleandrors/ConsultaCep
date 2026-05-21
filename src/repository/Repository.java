package repository;

import model.Cep;
import model.Categoria;
import model.Favorito;
import model.Usuario;
import util.ConnectionFactory;
import util.Util;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    //CRUD FAVORITOS
    
    public void salvarFavorito(String codigo, String nome, Usuario u) {
    	String sql = "INSERT INTO favoritos (fk_id_usuario, codigo, nome) VALUES (?, ?, ?)";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setInt(1, u.getId());
    		ps.setString(2, codigo);
    		ps.setString(3, nome);
    		ps.execute();
    		System.out.println("Favorito salvo com sucesso!");
    	} catch (Exception e) {
    		System.err.println("Erro ao salvar favorito: " + e.getMessage());
    	}
    }
    
    public void listarFavoritos(Usuario u) {
    	String sql = "SELECT f.id_favorito, f.codigo, f.nome AS nome_favorito, " +
    			"c.cidade, c.estado, c.logradouro, c.bairro, c.UF, c.regiao, c.DDD " +
    			"FROM favoritos f " +
    			"INNER JOIN cep c ON f.fk_id_usuario = c.fk_id_usuario AND f.codigo = c.codigo " +
    			"WHERE f.fk_id_usuario = ?";
    	
    	String sqlCat = "SELECT cat.nome FROM categoria_favoritos cf " +
    			"INNER JOIN categorias cat ON cf.fk_id_categoria = cat.id_categoria " +
    			"WHERE cf.fk_id_favorito = ?";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setInt(1, u.getId());
    		
    		try (ResultSet rs = ps.executeQuery()) {
    			boolean encontrou = false;
    			while (rs.next()) {
    				encontrou = true;
    				int idFav = rs.getInt("id_favorito");
    				System.out.println(
    					rs.getString("codigo") +
    					" - Nome: " + rs.getString("nome_favorito") +
    					", Cidade: " + rs.getString("cidade") +
    					", Estado: " + rs.getString("estado") +
    					", Logradouro: " + rs.getString("logradouro") +
    					", Bairro: " + rs.getString("bairro") +
    					", UF: " + rs.getString("UF") +
    					", Região: " + rs.getString("regiao") +
    					", DDD: " + rs.getString("DDD")
    				);
    				
    				// Buscar categorias deste favorito
    				try (PreparedStatement psCat = conn.prepareStatement(sqlCat)) {
    					psCat.setInt(1, idFav);
    					try (ResultSet rsCat = psCat.executeQuery()) {
    						List<String> cats = new ArrayList<>();
    						while (rsCat.next()) {
    							cats.add(rsCat.getString("nome"));
    						}
    						if (!cats.isEmpty()) {
    							System.out.println("  Categorias: " + String.join(", ", cats));
    						} else {
    							System.out.println("  Categorias: Nenhuma");
    						}
    					}
    				}
    			}
    			if (!encontrou) {
    				System.out.println("Nenhum favorito encontrado.");
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public Favorito buscarFavorito(String codigo, Usuario u) {
    	String sql = "SELECT * FROM favoritos WHERE codigo = ? AND fk_id_usuario = ?";
    	Favorito fav = null;
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setString(1, codigo);
    		ps.setInt(2, u.getId());
    		
    		try (ResultSet rs = ps.executeQuery()) {
    			if (rs.next()) {
    				fav = new Favorito(
    					rs.getInt("id_favorito"),
    					rs.getInt("fk_id_usuario"),
    					rs.getString("codigo"),
    					rs.getString("nome")
    				);
    			}
    		}
    	} catch (SQLException e) {
    		System.err.println("Erro ao buscar favorito: " + e.getMessage());
    	}
    	return fav;
    }
    
    public void excluirFavorito(String codigo, Usuario u) {
    	String sql = "DELETE FROM favoritos WHERE codigo = ? AND fk_id_usuario = ?";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setString(1, codigo);
    		ps.setInt(2, u.getId());
    		ps.execute();
    		System.out.println("Favorito removido com sucesso!");
    	} catch (Exception e) {
    		System.err.println("Erro ao excluir favorito: " + e.getMessage());
    	}
    }
    
    public void editarNomeFavorito(String codigo, String novoNome, Usuario u) {
    	String sql = "UPDATE favoritos SET nome = ? WHERE codigo = ? AND fk_id_usuario = ?";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setString(1, novoNome);
    		ps.setString(2, codigo);
    		ps.setInt(3, u.getId());
    		ps.execute();
    		System.out.println("Nome do favorito atualizado com sucesso!");
    	} catch (Exception e) {
    		System.err.println("Erro ao editar favorito: " + e.getMessage());
    	}
    }
    
    //CRUD CATEGORIAS
    
    public void salvarCategoria(String nome, Usuario u) {
    	String sql = "INSERT INTO categorias (fk_id_usuario, nome) VALUES (?, ?)";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setInt(1, u.getId());
    		ps.setString(2, nome);
    		ps.execute();
    		System.out.println("Categoria criada com sucesso!");
    	} catch (Exception e) {
    		System.err.println("Erro ao criar categoria: " + e.getMessage());
    	}
    }
    
    public List<Categoria> listarCategorias(Usuario u) {
    	String sql = "SELECT * FROM categorias WHERE fk_id_usuario = ?";
    	List<Categoria> lista = new ArrayList<>();
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setInt(1, u.getId());
    		
    		try (ResultSet rs = ps.executeQuery()) {
    			while (rs.next()) {
    				Categoria cat = new Categoria(
    					rs.getInt("id_categoria"),
    					rs.getInt("fk_id_usuario"),
    					rs.getString("nome")
    				);
    				lista.add(cat);
    				System.out.println(cat.getId() + " - " + cat.getNome());
    			}
    		}
    		if (lista.isEmpty()) {
    			System.out.println("Nenhuma categoria cadastrada.");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return lista;
    }
    
    public Categoria buscarCategoria(int id, Usuario u) {
    	String sql = "SELECT * FROM categorias WHERE id_categoria = ? AND fk_id_usuario = ?";
    	Categoria cat = null;
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setInt(1, id);
    		ps.setInt(2, u.getId());
    		
    		try (ResultSet rs = ps.executeQuery()) {
    			if (rs.next()) {
    				cat = new Categoria(
    					rs.getInt("id_categoria"),
    					rs.getInt("fk_id_usuario"),
    					rs.getString("nome")
    				);
    			}
    		}
    	} catch (SQLException e) {
    		System.err.println("Erro ao buscar categoria: " + e.getMessage());
    	}
    	return cat;
    }
    
    public void editarCategoria(int id, String novoNome, Usuario u) {
    	String sql = "UPDATE categorias SET nome = ? WHERE id_categoria = ? AND fk_id_usuario = ?";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setString(1, novoNome);
    		ps.setInt(2, id);
    		ps.setInt(3, u.getId());
    		ps.execute();
    		System.out.println("Categoria atualizada com sucesso!");
    	} catch (Exception e) {
    		System.err.println("Erro ao editar categoria: " + e.getMessage());
    	}
    }
    
    public void excluirCategoria(int id, Usuario u) {
    	String sql = "DELETE FROM categorias WHERE id_categoria = ? AND fk_id_usuario = ?";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setInt(1, id);
    		ps.setInt(2, u.getId());
    		ps.execute();
    		System.out.println("Categoria excluída com sucesso!");
    	} catch (Exception e) {
    		System.err.println("Erro ao excluir categoria: " + e.getMessage());
    	}
    }
    
    //VINCULO CATEGORIA-FAVORITO
    
    public void vincularCategoriaFavorito(int idFavorito, int idCategoria) {
    	String sql = "INSERT INTO categoria_favoritos (fk_id_favorito, fk_id_categoria) VALUES (?, ?)";
    	
    	try (
    		Connection conn = ConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql)
    	) {
    		ps.setInt(1, idFavorito);
    		ps.setInt(2, idCategoria);
    		ps.execute();
    	} catch (Exception e) {
    		System.err.println("Erro ao vincular categoria: " + e.getMessage());
    	}
    }
    
}