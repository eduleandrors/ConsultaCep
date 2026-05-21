import java.util.List;
import java.util.Scanner;
import repository.Repository;
import service.CepService;
import model.Cep;
import model.Categoria;
import model.Favorito;
import model.Usuario;
import util.Util;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class MainCep {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		Scanner sc = new Scanner(System.in);

		CepService service = new CepService();
		Repository repo = new Repository();
		char op, op2 = '4';
		String x, y, ultimoEndereco = "";
		boolean existe = false;
		Cep ultimo = null;
		int max = 20, cont = 0, maxh = 15, opL;
		LocalDateTime horario;
		Usuario logado = null;
		List<Cep> lista = null;
		do {
			System.out.println("\n1 - Fazer Login");
			System.out.println("2 - Fazer Logout");
			System.out.println("3 - Fazer Cadastro");
			System.out.println("0 - Sair");
			y = sc.nextLine();
			if (!y.equals("1") && !y.equals("2") && !y.equals("3")  && !y.equals("0")) {
				System.out.println("Valor inválido, tente novamente");
				op2 = 1;
			} else
			{
				op2 = y.charAt(0);
				switch (op2)
				{
				case '1':
					if (logado!=null)
					{
						System.out.println("Você já está logado!");
					}else
					{
						System.out.println("Digite seu email:");
						String email = sc.nextLine();
						System.out.println("Digite sua senha:");
						String senha = sc.nextLine();
						logado = repo.login(email, senha);
					}
					if (logado == null)
					{
						System.out.println("Usuário inválido ou não existe!");
					}
					else
					{
						System.out.println("Bem vindo(a) de volta " + logado.getNome() + "!");
						do {

							System.out.println("\n1 - Consultar CEP");
							System.out.println("2 - Consultar endereço");
							System.out.println("3 - Listar histórico de pesquisa");
							System.out.println("4 - Pesquisar dentro do histórico");
							System.out.println("5 - Deletar consulta");
							System.out.println("6 - Limpar histórico");
							System.out.println("7 - Favoritos");
							System.out.println("0 - Sair");

							x = sc.nextLine();
							if (!x.equals("1") && !x.equals("2") && !x.equals("0") && !x.equals("3") && !x.equals("4") && !x.equals("5") && !x.equals("6") && !x.equals("7")) {
								System.out.println("Valor inválido, tente novamente");
								op = 1;
							} else {
								op = x.charAt(0);
								switch (op) {
								case '1':
								    existe = false;
								    System.out.print("Digite o CEP: ");
								    String cep = sc.nextLine();
								    
								    if (cont < max) {
								        if (cep.length() != 8 || !cep.chars().allMatch(Character::isDigit)) {
								            System.out.println("Digite um Cep válido de 8 numeros");
								            break;
								        }
								        ultimo = repo.ultimo(logado); 
								        Cep noBanco = repo.buscar(cep, logado);
								        
								        if (noBanco != null) {
								            existe = true;
								            repo.atualizar(cep, logado); 
								        }
								        
								        if (!existe) {
								            Cep c = service.buscar(cep);
								            System.out.println("Retorno do service: " + c);
								            if (c != null) {
								                repo.salvar(c, logado);
								                System.out.println("Cidade: " + c.getCidade() + ", Estado: " + c.getEstado()
								                        + ", Logradouro: " + c.getLogradouro() + ", Complemento: " + c.getComplemento()
								                        + ", Bairro: " + c.getBairro() + ", UF: " + c.getUF() + ", Região: "
								                        + c.getRegiao() + ", DDD: " + c.getDDD());
								                cont=repo.cont(logado);
								            } else {
								                System.out.println("Cep inexistente");
								            }
								        } else {
								            if (ultimo != null && ultimo.getCodigo().equals(cep)) {
								                System.out.println("Cep já consultado");
								            } else {
								                System.out.println("Cidade: " + noBanco.getCidade() + ", Estado: " + noBanco.getEstado()
								                        + ", Logradouro: " + noBanco.getLogradouro() + ", Complemento: " + noBanco.getComplemento()
								                        + ", Bairro: " + noBanco.getBairro() + ", UF: " + noBanco.getUF() + ", Região: "
								                        + noBanco.getRegiao() + ", DDD: " + noBanco.getDDD());
								            }
								        }
								    } else {
								        System.out.println("Máximo de consultas atingido.");
								    }
								    break;
								case '2':

								    System.out.print("UF: ");
								    String uf = sc.nextLine();

								    System.out.print("Cidade: ");
								    String cidade = sc.nextLine();

								    System.out.print("Logradouro: ");
								    String logradouro = sc.nextLine();

								    if (cont >= max) {
								        System.out.println("Máximo de consultas atingido.");
								        break;
								    }

								    if (logradouro.length() < 3) {
								        System.out.println(
								            "Digite um logradouro válido de no mínimo 3 caracteres");
								        break;
								    }
								    
								    String pesquisaAtual = uf.toUpperCase().trim() + "|" + cidade.toUpperCase().trim() + "|" + logradouro.toUpperCase().trim();
								    if (pesquisaAtual.equals(ultimoEndereco)) {
								        System.out.println(
								            "Este endereço já foi consultado na última pesquisa.");
								        break;
								    }

								    List<Cep> dadosBanco = repo.buscarPorEndereco(uf, cidade, logradouro, logado);

								    if (!dadosBanco.isEmpty()) {

								        System.out.println(
								            "Resultados encontrados no histórico:");

								        for (Cep c : dadosBanco) {

								            repo.atualizar(c.getCodigo(), logado);

								            System.out.println(
								                "Cidade: " + c.getCidade() +
								                ", Estado: " + c.getEstado() +
								                ", Logradouro: " + c.getLogradouro() +
								                ", Complemento: " + c.getComplemento() +
								                ", Bairro: " + c.getBairro() +
								                ", UF: " + c.getUF() +
								                ", Região: " + c.getRegiao() +
								                ", DDD: " + c.getDDD()
								            );
								        }

								    } else {

								        List<Cep> resultados =
								            service.buscarPorEndereco(
								                uf, cidade, logradouro);

								        if (resultados == null || resultados.isEmpty()) {

								            System.out.println("Endereço inexistente");

								        } else {

								            System.out.println(
								                "Resultados encontrados:");

								            for (Cep c : resultados) {

								                Cep noBanco = repo.buscar(c.getCodigo(), logado);

								                if (noBanco == null) {
								                    repo.salvar(c, logado);
								                } else {
								                    repo.atualizar(
								                        c.getCodigo(), logado);
								                }

								                System.out.println(
								                    "Cidade: " + c.getCidade() +
								                    ", Estado: " + c.getEstado() +
								                    ", Logradouro: " + c.getLogradouro() +
								                    ", Complemento: " + c.getComplemento() +
								                    ", Bairro: " + c.getBairro() +
								                    ", UF: " + c.getUF() +
								                    ", Região: " + c.getRegiao() +
								                    ", DDD: " + c.getDDD()
								                );
								            }

								            cont = repo.cont(logado);
								        }
								    }
								    ultimoEndereco = pesquisaAtual;

								    break;
								case '3':
								    System.out.println("Escolha a ordenação desejada para a lista.");
								    System.out.println("\n1 - Consultas recentes");
								    System.out.println("\n2 - Ordem original");
								    System.out.println("\n3 - Mais consultados");
								    
								    String entradaL = sc.nextLine(); 
								    
								    if (!entradaL.equals("1") && !entradaL.equals("2") && !entradaL.equals("3")) {
								        System.out.println("Valor inválido, tente novamente");
								        break;
								    } else {
								        opL = Integer.parseInt(entradaL);
								        repo.listar(opL, logado);
								    }
								    
								    System.out.println("Foram feitas " + repo.cont(logado)+ " consultas bem sucedidas.");
								    ultimo = repo.ultimo(logado);
								    if (ultimo != null) {
								        System.out.println("A ultima consulta feita foi pelo CEP " + ultimo.getCodigo() + " de "
								                + ultimo.getCidade());
								    }
								    break;
								case '4':
									System.out.print("Digite o CEP para buscar: ");
									String busca = sc.nextLine();

									Cep c1 = repo.buscar(busca, logado);
									
									if (c1 != null)
									{
										System.out.println(c1.getCodigo() + " - " + "Cidade: " + c1.getCidade() + ", Estado: "
												+ c1.getEstado() + ", Logradouro: " + c1.getLogradouro() + ", Complemento: "
												+ c1.getComplemento() + ", Bairro: " + c1.getBairro() + ", UF: " + c1.getUF()
												+ ", Região: " + c1.getRegiao() + ", DDD: " + c1.getDDD());
									}else
									{
										System.out.println("CEP não encontrado no histórico.");
									}

									break;

								case '5':
									System.out.print("Digite o CEP para apagar: ");
									String apagar = sc.nextLine();

									Cep c2 = repo.buscar(apagar, logado);
									if (c2!=null)
									{
										repo.apagar(apagar, logado);
										System.out.println("CEP removido do histórico.");
									}else {
										System.out.println("CEP não encontrado no histórico.");
									}

									break;
								
								case '6':
									repo.limparHistórico(logado);
								break;
							case '7':
								char opFav;
								do {
									System.out.println("\n=== Favoritos ===");
									System.out.println("1 - Listar favoritos");
									System.out.println("2 - Listar favoritos por categoria");
									System.out.println("3 - Adicionar favorito");
									System.out.println("4 - Excluir favorito");
									System.out.println("5 - Editar nome de favorito");
									System.out.println("6 - Criar categoria");
									System.out.println("7 - Listar categorias");
									System.out.println("8 - Editar categoria");
									System.out.println("9 - Excluir categoria");
									System.out.println("0 - Voltar");

									String entradaFav = sc.nextLine();
									if (entradaFav.length() != 1 || "0123456789".indexOf(entradaFav.charAt(0)) == -1) {
										System.out.println("Valor inválido, tente novamente");
										opFav = 1;
									} else {
										opFav = entradaFav.charAt(0);
										switch (opFav) {
										case '1': // Listar favoritos
											repo.listarFavoritos(logado);
											break;
											
										case '2': // Filtrar favoritos por categoria
											System.out.print("Digite o ID da categoria: ");
										    String idCatFiltro = sc.nextLine();

										    try {
										        int idFiltro = Integer.parseInt(idCatFiltro);

										        Categoria catFiltro = repo.buscarCategoria(idFiltro, logado);

										        if (catFiltro != null) {
										            repo.listarFavoritosPorCategoria(idFiltro, logado);
										        } else {
										            System.out.println("Categoria não encontrada.");
										        }

										    } catch (NumberFormatException ex) {
										        System.out.println("ID inválido.");
										    }
											break;

										case '3': // Adicionar favorito
											System.out.print("Digite o código do CEP para favoritar: ");
											String cepFav = sc.nextLine();

											Cep cepExiste = repo.buscar(cepFav, logado);
											if (cepExiste == null) {
												System.out.println("CEP não encontrado no seu histórico. Consulte primeiro.");
												break;
											}

											Favorito favExiste = repo.buscarFavorito(cepFav, logado);
											if (favExiste != null) {
												System.out.println("Este CEP já está nos seus favoritos.");
												break;
											}

											System.out.print("Digite um nome para este favorito: ");
											String nomeFav = sc.nextLine();

											repo.salvarFavorito(cepFav, nomeFav, logado);

											// Vincular a categorias
											List<Categoria> cats = repo.listarCategorias(logado);
											if (!cats.isEmpty()) {
												System.out.println("Deseja vincular a alguma categoria? (Digite os IDs separados por vírgula, ou 0 para pular)");
												String idsCat = sc.nextLine();
												if (!idsCat.equals("0")) {
													Favorito novoFav = repo.buscarFavorito(cepFav, logado);
													if (novoFav != null) {
														String[] ids = idsCat.split(",");
														for (String idStr : ids) {
															try {
																int idCat = Integer.parseInt(idStr.trim());
																Categoria catExiste = repo.buscarCategoria(idCat, logado);
																if (catExiste != null) {
																	repo.vincularCategoriaFavorito(novoFav.getId(), idCat);
																	System.out.println("Vinculado à categoria: " + catExiste.getNome());
																} else {
																	System.out.println("Categoria ID " + idCat + " não encontrada.");
																}
															} catch (NumberFormatException ex) {
																System.out.println("ID inválido: " + idStr.trim());
															}
														}
													}
												}
											}
											break;

										case '4': // Excluir favorito
											repo.listarFavoritos(logado);
											System.out.print("Digite o código do CEP para remover dos favoritos: ");
											String cepExcluir = sc.nextLine();
											Favorito favExcluir = repo.buscarFavorito(cepExcluir, logado);
											if (favExcluir != null) {
												repo.excluirFavorito(cepExcluir, logado);
											} else {
												System.out.println("Favorito não encontrado.");
											}
											break;

										case '5': // Editar nome de favorito
											repo.listarFavoritos(logado);
											System.out.print("Digite o código do CEP do favorito para editar: ");
											String cepEditar = sc.nextLine();
											Favorito favEditar = repo.buscarFavorito(cepEditar, logado);
											if (favEditar != null) {
												System.out.print("Digite o novo nome: ");
												String novoNomeFav = sc.nextLine();
												repo.editarNomeFavorito(cepEditar, novoNomeFav, logado);
											} else {
												System.out.println("Favorito não encontrado.");
											}
											break;

										case '6': // Criar categoria
											System.out.print("Digite o nome da nova categoria: ");
											String nomeCat = sc.nextLine();
											repo.salvarCategoria(nomeCat, logado);
											break;

										case '7': // Listar categorias
											repo.listarCategorias(logado);
											break;

										case '8': // Editar categoria
											repo.listarCategorias(logado);
											System.out.print("Digite o ID da categoria para editar: ");
											String idEditStr = sc.nextLine();
											try {
												int idEdit = Integer.parseInt(idEditStr);
												Categoria catEdit = repo.buscarCategoria(idEdit, logado);
												if (catEdit != null) {
													System.out.print("Digite o novo nome: ");
													String novoNomeCat = sc.nextLine();
													repo.editarCategoria(idEdit, novoNomeCat, logado);
												} else {
													System.out.println("Categoria não encontrada.");
												}
											} catch (NumberFormatException ex) {
												System.out.println("ID inválido.");
											}
											break;

										case '9': // Excluir categoria
											repo.listarCategorias(logado);
											System.out.print("Digite o ID da categoria para excluir: ");
											String idExcStr = sc.nextLine();
											try {
												int idExc = Integer.parseInt(idExcStr);
												Categoria catExc = repo.buscarCategoria(idExc, logado);
												if (catExc != null) {
													repo.excluirCategoria(idExc, logado);
												} else {
													System.out.println("Categoria não encontrada.");
												}
											} catch (NumberFormatException ex) {
												System.out.println("ID inválido.");
											}
											break;

										case '0':
											System.out.println("Voltando ao menu principal");
											break;
										}
									}
								} while (opFav != '0');
								break;
							case '0':
								System.out.println("Retornando a tela de login");
								break;
								}
							}
						} while (op != '0');
					}
				break;
				case '2':
					if (logado ==null)
					{
						System.out.println("Nenhuma conta logada!");
					}
					else
					{
						logado = null;
						System.out.println("Deslogado com sucesso!");
					}
				break;
				case '3':
					System.out.println("Digite seu nome:");
					String nomeu = sc.nextLine();
					System.out.println("Digite seu email:");
					String emailu = sc.nextLine();
					System.out.println("Digite sua senha:");
					String senhau = sc.nextLine();
					if (repo.ConferirEmail(emailu))
					{
						System.out.println("Este email já está cadastrado");
					}
					else
					{
						Usuario u = new Usuario (-1, nomeu, emailu, senhau,Timestamp.from(Instant.now()));
						repo.Cadastro(u);
						System.out.println("Cadastro concluido! Seja bem-vindo(a)");
					}
				break;
				case '0':
					System.out.println("Obrigado por usar o ViaCep 2.1.2!");
					break;
				}
					
			}
			
		}while (op2 != '0');
	}
}