package todoapp;

import todoapp.ui.ConsoleUI;

public class TodoApp {
    public static void main(String[] args) {
        System.out.println("Sistema de Lista de Tarefas POO Avançada");
        System.out.println("Autor: Seu Nome");
        System.out.println("Versão: 1.0");

        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}