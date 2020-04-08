#include <StubPreamble.h>
#include "AmigoOculto.h"

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

// Incluir leitura dinamica no programa (metodo getch()):
#ifdef _WIN32
    #include <conio.h>
    #define setup() fflush(stdout) // limpa buffer de saida
#else
    #include <curses.h>
    #define setup() \
        initscr(); /* inicializa nova tela vazia */ \
        noecho(); /* configura para nao imprimir a entrada do usuario no terminal de saida */ \
        cbreak(); /* define buffer de entrada para um caractere por vez */ \
        keypad(stdscr, TRUE); /* permite o uso de teclas especiais (como as setas, por exemplo) */ \
        curs_set(0); /* "apaga" o cursor */ \
        fflush(stdout); // limpa buffer de saida
#endif

#define UP 259
#define DOWN 258
#define LEFT 260
#define RIGHT 261
#define Esc 27
#define entrada_valida(c) (c==Esc || c==' ' || c=='\n' || c==UP || c=='W' || c==DOWN || c=='S' || c==LEFT || c=='A' || c==RIGHT || c=='D')

/**
 * Funcao principal.
 * @param menu string com o texto do menu COMPLETO (incluindo cabecalho, opcoes e formatacoes).
 * @return index da linha da opcao selecionada pelo usuario.
*/
int main(struct AmigoOculto *this){
    setup();
    printf("%s", menu); // imprime o menu
    
    int entrada = getch();
    // Validar entrada:
    while( !entrada_valida( toupper(entrada) ) ){
        printf("\a"); // dispara barulho do terminal
        entrada = getch();
    }

    return entrada;
}