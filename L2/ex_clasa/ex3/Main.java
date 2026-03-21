import org.graalvm.polyglot.*;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        try (Context polyglot = Context.newBuilder().allowAllAccess(true).build())
        {
            // generare lista in Python
            polyglot.eval("python", """
                import random

                def genereaza():
                    return random.sample(range(1,101),20)
            """);

            Value genereaza = polyglot.getBindings("python").getMember("genereaza");
            Value result = genereaza.execute();

            List<Integer> lista = new ArrayList<>();
            for (int i = 0; i < result.getArraySize(); i++)
            {
                lista.add(result.getArrayElement(i).asInt());
            }

            // sortare in Python
            polyglot.eval("python", """
                def sortare(s):
                    return sorted(s)
            """);

            Value sortare = polyglot.getBindings("python").getMember("sortare");
            result = sortare.execute(lista);

            lista.clear();
            for (int i = 0; i < result.getArraySize(); i++)
            {
                lista.add(result.getArrayElement(i).asInt());
            }

            // eliminare 20% + media
            polyglot.eval("python", """
                def proceseaza(lista):
                    n = len(lista)
                    k = int(n * 0.2)

                    lista_filtrata = lista[k:n-k]

                    if len(lista_filtrata) == 0:
                        return [], 0

                    media = sum(lista_filtrata) / len(lista_filtrata)
                    return lista_filtrata, media
            """);

            Value proceseaza = polyglot.getBindings("python").getMember("proceseaza");
            result = proceseaza.execute(lista);

            // extrag lista filtrata
            Value listaFiltrataVal = result.getArrayElement(0);
            List<Integer> listaFiltrata = new ArrayList<>();
            for (int i = 0; i < listaFiltrataVal.getArraySize(); i++)
            {
                listaFiltrata.add(listaFiltrataVal.getArrayElement(i).asInt());
            }

            // extrag media
            double media = result.getArrayElement(1).asDouble();

            // afisare cu JavaScript
            polyglot.eval("js", """
                function afiseaza(lista)
                {
                    console.log("Lista este:");
                    console.log(lista);
                }
            """);

            Value afiseaza = polyglot.getBindings("js").getMember("afiseaza");

            System.out.println("Lista initiala sortata:");
            afiseaza.execute(lista);

            System.out.println("Lista dupa eliminare 20%:");
            afiseaza.execute(listaFiltrata);

            System.out.println("Media aritmetica: " + media);
        }
    }
}