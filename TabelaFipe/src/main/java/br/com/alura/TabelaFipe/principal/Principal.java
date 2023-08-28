package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculos;
import br.com.alura.TabelaFipe.services.ConsumoApi;
import br.com.alura.TabelaFipe.services.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){
        var menu = """
                ***** OPÇÕES *****
                -> Carro
                -> Moto
                -> Caminhão
                
                Digite o tipo de veículo: 
                """;

        System.out.println(menu);
        var opcao = leitura.nextLine();
        String endereco = "";

        if(opcao.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else  {
            endereco = URL_BASE + "caminhoes/marcas";
        }


        var json = consumo.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o código da marca: ");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nLista de modelos:");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite o nome do carro: ");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m->m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\n***** Modelos Filtrados *****".toUpperCase());
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigite o código do modelo: ");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);

        List<Veiculos> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculos veiculoEscolhido = conversor.obterDados(json, Veiculos.class);
            veiculos.add(veiculoEscolhido);

        }
        System.out.println("\nTodos os veículos do ano: ");
        veiculos.forEach(System.out::println);
    }
}
