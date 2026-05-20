import java.util.List;
import java.util.Scanner;
import repository.Repository;
import service.CepService;
import model.Cep;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class MainCep {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		CepService service = new CepService();
		Repository repo = new Repository();
		char op;
		String x;
		boolean existe = false;
		Cep ultimo = null;
		int max = 20, cont = 0, maxh = 15, opL;
		LocalDateTime horario;
		List<Cep> lista = null;

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
				        ultimo = repo.ultimo(); 
				        Cep noBanco = repo.buscar(cep);
				        
				        if (noBanco != null) {
				            existe = true;
				            repo.atualizar(cep); 
				        }
				        
				        if (!existe) {
				            Cep c = service.buscar(cep);
				            if (c != null) {
				                repo.salvar(c);
				                System.out.println("Cidade: " + c.getCidade() + ", Estado: " + c.getEstado()
				                        + ", Logradouro: " + c.getLogradouro() + ", Complemento: " + c.getComplemento()
				                        + ", Bairro: " + c.getBairro() + ", UF: " + c.getUF() + ", Região: "
				                        + c.getRegiao() + ", DDD: " + c.getDDD());
				                cont++;
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
				                cont++;
				            }
				        }
				    } else {
				        System.out.println("Máximo de consultas atingido.");
				    }
				    break;
				case '2':
					System.out.println("Escolha a ordenação desejada para a lista.");
					System.out.println("\n1 - Consultas recentes");
					System.out.println("\n2 - Mais consultados");
					opL = sc.nextInt();
					if (opL != 1 && opL != 2) {
						System.out.println("Valor inválido, tente novamente");
						break;
					} else {
						repo.listar(opL);
					}
					System.out.println("Foram feitas " + repo.cont() + " consultas bem sucedidas.");
					System.out.println("O CEP mais consultado é " + repo.maisConsultdo().getCodigo());
					ultimo = repo.ultimo();
					if (ultimo != null) {
						System.out.println("A ultima consulta feita foi pelo CEP " + ultimo.getCodigo() + " de "
								+ ultimo.getCidade());
					}
					sc.nextLine();
					break;

				case '3':
					System.out.print("Digite o CEP para buscar: ");
					String busca = sc.nextLine();

					Cep c1 = repo.buscar(busca);
					
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

					Cep c2 = repo.buscar(apagar);
					if (c2!=null)
					{
						repo.apagar(apagar);
						System.out.println("CEP removido do histórico.");
					}else {
						System.out.println("CEP não encontrado no histórico.");
					}

					break;

				case '0':
					System.out.println("Obrigado por usar o ViaCep 2.1.2!");
					break;
				}
			}
		} while (op != '0');
	}
}