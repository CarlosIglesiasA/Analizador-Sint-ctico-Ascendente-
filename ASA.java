package com.mycompany.asa;
import java.util.List;
import java.util.Stack;

public class ASA implements Parser {
    private int i = 0;
    private Token preanalisis;
    private final List<Token> tokens;
    private final Stack<Integer> pila;
    private boolean hayErrores = false;
    private boolean salir=false;

    public ASA(List<Token> tokens) {
        this.pila = new Stack();
        this.tokens = tokens;
        preanalisis=this.tokens.get(i);
        this.pila.push(0);
    }

    @Override
    public boolean parse() {
        do {
            switch (pila.peek()) {
                case 0:
                    if (preanalisis.tipo == TipoToken.SELECT) {
                        pila.push(2);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else
                        hayErrores=true;
                    break;

                case 1:
                    if (preanalisis.tipo == TipoToken.EOF)
                        salir= true;
                    break;

                case 2:
                    if (preanalisis.tipo == TipoToken.IDENTIFICADOR) {
                        pila.push(9);
                        i++;
                        preanalisis = tokens.get(i);
                    }

                    else if (preanalisis.tipo == TipoToken.ASTERISCO) {
                        pila.push(6);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else if (preanalisis.tipo == TipoToken.DISTINCT) {
                        pila.push(4);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else
                        hayErrores=true;
                    break;

                case 3:
                    if(preanalisis.tipo==TipoToken.FROM)
                        pila.push(10);
                    else
                        hayErrores=true;

                    i++;
                    preanalisis = tokens.get(i);
                    break;

                case 4:
                    if (preanalisis.tipo == TipoToken.IDENTIFICADOR)
                        pila.push(9);
                    else if (preanalisis.tipo == TipoToken.ASTERISCO)
                        pila.push(6);
                    else
                        hayErrores=true;

                    i++;
                    preanalisis = tokens.get(i);
                    break;

                case 5:
                    if(preanalisis.tipo==TipoToken.FROM) {//reduccion de 3 (D->P)
                        pila.pop();
                        if (pila.peek() == 2)
                            pila.push(3);
                        else
                            hayErrores = true;
                    }
                    else
                        hayErrores=true;

                    break;

                case 6:
                    if(preanalisis.tipo==TipoToken.FROM) {//reduccion de 4 (P->*)
                        pila.pop();
                        if (pila.peek() == 2)
                            pila.push(5);
                        else if (pila.peek() == 4)
                            pila.push(18);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 7:
                    if(preanalisis.tipo==TipoToken.COMA) {
                        pila.push(22);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else if(preanalisis.tipo==TipoToken.FROM){//reduccion de 5 (P->A)
                        pila.pop();
                        if (pila.peek() == 2)
                            pila.push(5);
                        else if (pila.peek() == 4)
                            pila.push(18);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 8:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.FROM) {//reduccion de 7 (A->A1)
                        pila.pop();
                        if (pila.peek() == 2 || pila.peek()==4)
                            pila.push(7);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 9:
                    if(preanalisis.tipo==TipoToken.PUNTO) {
                        pila.push(20);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.FROM){//reduccion de 10 (A2->Epsilon)
                        pila.push(19);
                    }
                    else
                        hayErrores=true;
                    break;

                case 10, 14:
                    if(preanalisis.tipo==TipoToken.IDENTIFICADOR) {
                        pila.push(13);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else
                        hayErrores=true;
                    break;

                case 11:
                    if(preanalisis.tipo==TipoToken.COMA) {
                        pila.push(14);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else if(preanalisis.tipo==TipoToken.EOF){//reduccion de 1 (Q->select D from T)
                        pila.pop();
                        pila.pop();
                        pila.pop();
                        pila.pop();
                        if (pila.peek() == 0)
                            pila.push(1);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 12:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.EOF) {//reduccion de 12 (T->T1)
                        pila.pop();
                        if (pila.peek() == 10)
                            pila.push(11);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 13:
                    if(preanalisis.tipo==TipoToken.IDENTIFICADOR) {
                        pila.push(17);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.EOF){//reduccion de 15 (T2->Epsilon)
                        pila.push(16);
                    }
                    else
                        hayErrores=true;
                    break;

                    //el 14 esta cpn el 10 porque son practicamente el mismo caso

                case 15:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.EOF) {//reduccion de 11 (T->T,T1)
                        pila.pop();
                        pila.pop();
                        pila.pop();
                        if (pila.peek() == 10)
                            pila.push(11);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 16:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.EOF) {//reduccion de 13 (T1->idT2)
                        pila.pop();
                        pila.pop();
                        if (pila.peek() == 10)
                            pila.push(12);
                        else if(pila.peek() == 14)
                            pila.push(15);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 17:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.EOF) {//reduccion de 14 (T2->id)
                        pila.pop();
                        if (pila.peek() == 13)
                            pila.push(16);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 18:
                    if(preanalisis.tipo==TipoToken.FROM) {//reduccion de 2 (D->distinct P)
                        pila.pop();
                        pila.pop();
                        if (pila.peek() == 2)
                            pila.push(3);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 19:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.FROM) {//reduccion de 8 (A1->idA2)
                        pila.pop();
                        pila.pop();
                        if (pila.peek() == 2||pila.peek() == 4)
                            pila.push(8);
                        else if(pila.peek()==22)
                            pila.push(23);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 20:
                    if(preanalisis.tipo==TipoToken.IDENTIFICADOR) {
                        pila.push(21);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else
                        hayErrores=true;
                    break;

                case 21:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.FROM) {//reduccion de 9 (A2->.id)
                        pila.pop();
                        pila.pop();
                        if (pila.peek() == 9)
                            pila.push(19);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;

                case 22:
                    if(preanalisis.tipo==TipoToken.IDENTIFICADOR) {
                        pila.push(9);
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else
                        hayErrores=true;
                    break;

                case 23:
                    if(preanalisis.tipo==TipoToken.COMA||preanalisis.tipo==TipoToken.FROM) {//reduccion de 6 (A->A,A1)
                        pila.pop();
                        pila.pop();
                        pila.pop();
                        if (pila.peek() == 2||pila.peek() == 4)
                            pila.push(7);
                        else
                            hayErrores= true;
                    }
                    else
                        hayErrores=true;
                    break;
            }//FIN DE SWITCH

        }while (!hayErrores && !salir);


        if(hayErrores){
            System.out.println("Consulta invalida");
            return false;
        }
        else{
            System.out.println("Consulta correcta");
            return true;
        }
    }


}

