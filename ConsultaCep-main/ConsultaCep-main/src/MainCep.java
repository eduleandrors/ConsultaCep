import java.util.List;
import java.util.Scanner;
import repository.Repository;
import service.CepService;
import model.Cep;
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
		String x, y;
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
							System.out.println("2 - Listar histórico de pesquisa");
							System.out.println("3 - Pesquisar dentro do histórico");
							System.out.println("4 - Deletar consulta");
							System.out.println("0 - Sair");

							x = sc.nextLine();
							if (!x.equals("1") && !x.equals("2") && !x.equals("0") && !x.equals("3") && !x.equals("4")) {
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
								case '3':
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

								case '4':
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