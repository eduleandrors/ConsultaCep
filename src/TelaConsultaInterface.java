import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.Cep;
import service.CepService;

public class TelaConsultaInterface{

    public static void main(String[] args) {

        CepService service = new CepService();

        JFrame janela = new JFrame("Consulta de API");

        JLabel label = new JLabel("Digite o valor da consulta:");

        JTextField campoConsulta = new JTextField();

        JButton botaoConsultar = new JButton("Consultar");

        label.setBounds(30, 30, 200, 30);
        campoConsulta.setBounds(30, 70, 250, 30);
        botaoConsultar.setBounds(30, 120, 120, 35);

        botaoConsultar.addActionListener(e -> {

            String valor = campoConsulta.getText();
            Cep c = service.buscar(valor);

            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Digite um valor para consultar.");
            }
            else if(c != null) {
                JOptionPane.showMessageDialog(null, "CEP CONSULTADO:" +  
                                                "\nCidade: " + c.getCidade() +
								                "\nEstado: " + c.getEstado() +
								                "\nLogradouro: " + c.getLogradouro() +
								                "\nComplemento: " + c.getComplemento() +
								                "\nBairro: " + c.getBairro() +
								                "\nUF: " + c.getUF() +
								                "\nRegião: " + c.getRegiao() +
								                "\nDDD: " + c.getDDD());
            }
            else{
                JOptionPane.showMessageDialog(null, "CEP INVÁLIDO");
            }
        });

        janela.add(label);
        janela.add(campoConsulta);
        janela.add(botaoConsultar);

        janela.setSize(350, 230);
        janela.setLayout(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
    }
}